package voting.app.voting.app.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import voting.app.voting.app.dto.user.UserDto;
import voting.app.voting.app.dto.user.UserWithDetailDto;
import voting.app.voting.app.helper.PaginationHelper;
import voting.app.voting.app.mapper.UserMapper;
import voting.app.voting.app.model.follow.Follow;
import voting.app.voting.app.model.follow.FollowId;
import voting.app.voting.app.model.user.User;
import voting.app.voting.app.repository.FollowRepository;
import voting.app.voting.app.repository.PollRepository;
import voting.app.voting.app.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final FollowRepository followRepository;

    private final PollRepository pollRepository;

    private final UserMapper userMapper;

    private final PaginationHelper paginationHelper;

    public UserDto saveUser(User user) {
        return userMapper.toUserDto(userRepository.save(user));
    }

    public UserDto getUser(String userId) {
        return userMapper.toUserDto(findByUserIdOrElseThrow(userId));
    }

    public UserWithDetailDto getUserWithDetail(String currentUserId, String userId) {
        User user = findByUserIdOrElseThrow(userId);
        UserWithDetailDto userWithDetailDto =
                userMapper.toUserWithDetailDto(userMapper.toUserDto(user));
        userWithDetailDto.setPollCount(pollRepository.countByCreatedById(userId));
        userWithDetailDto.setFollowerCount(followRepository.countByFollowIdFolloweeId(userId));
        userWithDetailDto.setFollowingCount(followRepository.countByFollowIdFollowerId(userId));
        if (currentUserId.equals(userId)) return userWithDetailDto;

        userWithDetailDto.setFollowing(
                followRepository.existsById(new FollowId(currentUserId, userId)));

        return userWithDetailDto;
    }

    public void follow(String followerId, String followeeId) {
        findByUserIdOrElseThrow(followeeId);
        followRepository.save(new Follow(new FollowId(followerId, followeeId)));
    }

    public void unfollow(String followerId, String followeeId) {
        followRepository.deleteById(new FollowId(followerId, followeeId));
    }

    public List<UserDto> getFollowers(String userId, Integer pageNumber, Integer pageSize) {
        Pageable pageable =
                paginationHelper.getPageable(pageNumber, pageSize, true, "followId.followerId");
        return userMapper.toUserDtos(
                userRepository.findAllById(
                        followRepository.findAllByFollowIdFolloweeId(userId, pageable).stream()
                                .map(Follow::getFollowId)
                                .map(FollowId::getFollowerId)
                                .toList()));
    }

    public List<UserDto> getFollowing(String userId, Integer pageNumber, Integer pageSize) {
        Pageable pageable =
                paginationHelper.getPageable(pageNumber, pageSize, true, "followId.followeeId");
        return userMapper.toUserDtos(
                userRepository.findAllById(
                        followRepository.findAllByFollowIdFollowerId(userId, pageable).stream()
                                .map(Follow::getFollowId)
                                .map(FollowId::getFolloweeId)
                                .toList()));
    }

    private User findByUserIdOrElseThrow(String userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(
                        () ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "User with id=" + userId + " not found"));
    }
}
