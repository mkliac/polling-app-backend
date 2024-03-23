package voting.app.voting.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import voting.app.voting.app.dto.UserDto;
import voting.app.voting.app.mapper.UserMapper;
import voting.app.voting.app.model.User;
import voting.app.voting.app.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserDto saveUser(User user) {
        return userMapper.toUserDto(userRepository.save(user));
    }
}
