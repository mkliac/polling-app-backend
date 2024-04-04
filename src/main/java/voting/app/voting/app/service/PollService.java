package voting.app.voting.app.service;

import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import voting.app.voting.app.config.AppConfig;
import voting.app.voting.app.dto.*;
import voting.app.voting.app.helper.PaginationHelper;
import voting.app.voting.app.mapper.PollMapper;
import voting.app.voting.app.mapper.UserMapper;
import voting.app.voting.app.model.*;
import voting.app.voting.app.repository.*;
import voting.app.voting.app.validation.OwnerValidator;

@Service
@RequiredArgsConstructor
@Slf4j
public class PollService {
    private final PollRepository pollRepository;

    private final PollItemRepository pollItemRepository;

    private final VoteRepository voteRepository;

    private final UserRepository userRepository;

    private final BookmarkRepository bookmarkRepository;

    private final PollMapper pollMapper;

    private final UserMapper userMapper;

    private final OwnerValidator ownerValidator;

    private final AppConfig appConfig;

    private final PaginationHelper paginationHelper;

    public PollDto getPoll(User user, String id) {
        return pollMapper.toPollDto(findPollByIdOrElseThrow(id), user);
    }

    public List<PollDto> getPolls(
            User user,
            PollFilterType filterType,
            String search,
            boolean isAscending,
            String sortBy,
            Integer pageNumber,
            Integer pageSize) {
        Pageable pageable = paginationHelper.getPageable(pageNumber, pageSize, isAscending, sortBy);

        List<Poll> polls;
        switch (filterType) {
            case ALL -> polls = pollRepository.findAll(search, pageable);
            case USER -> polls = pollRepository.findAllByCreatedBy(user.getId(), search, pageable);
            case BOOKMARKED -> {
                // NOTE: Might not be the most efficient way to get bookmarked polls by page
                List<String> bookmarkedPollIds =
                        bookmarkRepository.findAllByBookmarkIdUserId(user.getId()).stream()
                                .map(Bookmark::getBookmarkId)
                                .map(BookmarkId::getPollId)
                                .toList();
                polls = pollRepository.findAllByBookmark(bookmarkedPollIds, search, pageable);
            }
            default -> polls = List.of();
        }

        return pollMapper.toPollDtos(polls, user);
    }

    public void deletePoll(User user, String id) {
        Poll poll = findPollByIdOrElseThrow(id);
        ownerValidator.isOwnerOrElseThrow(user, poll);

        voteRepository.deleteAllByVoteIdPollItemIdIn(
                poll.getItems().stream().map(PollItem::getId).toList());
        bookmarkRepository.deleteAllByBookmarkIdPollId(id);
        pollItemRepository.deleteAll(poll.getItems());
        pollRepository.delete(poll);
    }

    private Poll findPollByIdOrElseThrow(String id) {
        return pollRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND, "Poll with id=" + id + " not found"));
    }

    private void validateSavePollRequest(SavePollRequest request) {
        AppConfig.PollConfig pollConfig = appConfig.getPollConfig();
        if (request.getTitle().length() < pollConfig.getMinTitleLength()
                || request.getTitle().length() > pollConfig.getMaxTitleLength()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Title length must be between "
                            + pollConfig.getMinTitleLength()
                            + " and "
                            + pollConfig.getMaxTitleLength());
        }

        if (request.getDescription().length() > pollConfig.getMaxDescriptionLength()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Description length must be less than " + pollConfig.getMaxDescriptionLength());
        }

        if (request.getItems().size() > pollConfig.getMaxPollItems()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Max poll items is " + pollConfig.getMaxPollItems());
        }
    }

    public PollDto savePoll(User createdBy, SavePollRequest request) {
        validateSavePollRequest(request);
        Poll poll = pollMapper.fromSavePollRequest(createdBy, request);
        poll.setPriority(PollPriority.NORMAL);

        pollItemRepository.saveAll(poll.getItems());
        poll = pollRepository.save(poll);
        return pollMapper.toPollDto(poll);
    }

    public PollDto updatePoll(User user, String id, UpdatePollRequest request) {
        Poll poll = findPollByIdOrElseThrow(id);
        ownerValidator.isOwner(user, poll);

        validateUpdatePollRequest(poll, request);
        deletePollItemByIds(request.getRemoveItemIds().stream().toList());
        pollMapper.updatePoll(request, poll);

        pollItemRepository.saveAll(poll.getItems());
        poll = pollRepository.save(poll);
        return pollMapper.toPollDto(poll, user);
    }

    private void validateUpdatePollRequest(Poll poll, UpdatePollRequest request) {
        if (poll.getItems().size()
                        - request.getRemoveItemIds().size()
                        + request.getAddItemTexts().size()
                > appConfig.getPollConfig().getMaxPollItems()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Max poll items is " + appConfig.getPollConfig().getMaxPollItems());
        }
    }

    private void deletePollItemByIds(List<String> ids) {
        List<PollItem> items = pollItemRepository.findAllById(ids);

        if (items.size() != ids.size()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll item not found");
        }

        voteRepository.deleteAllByVoteIdPollItemIdIn(ids);
        pollItemRepository.deleteAllById(ids);
    }

    public PollDto closePoll(User user, String pollId) {
        Poll poll = findPollByIdOrElseThrow(pollId);
        ownerValidator.isOwner(user, poll);

        poll.setClosedDate(Instant.now());
        poll = pollRepository.save(poll);

        return pollMapper.toPollDto(poll, user);
    }

    public PollDto vote(User user, String pollId, String pollItemId) {
        Poll poll = findPollByIdOrElseThrow(pollId);
        if (poll.getClosedDate() != null && poll.getClosedDate().isBefore(Instant.now())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Poll with id=" + pollId + " is closed");
        }

        voteRepository.deleteAllByVoteIdIn(
                poll.getItems().stream()
                        .map(item -> new VoteId(user.getId(), item.getId()))
                        .toList());
        voteRepository.save(new Vote(new VoteId(user.getId(), pollItemId)));
        return pollMapper.toPollDto(poll, user);
    }

    public List<UserDto> getVoters(
            String pollId, String itemId, Integer pageNumber, Integer pageSize) {
        if (findPollByIdOrElseThrow(pollId).isAnonymous()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Poll with id=" + pollId + " is anonymous");
        }

        Pageable pageable = paginationHelper.getPageable(pageNumber, pageSize, true);
        List<Vote> votes = voteRepository.findAllByVoteIdPollItemId(itemId, pageable);
        List<User> users =
                userRepository.findAllByIdIn(
                        votes.stream().map(Vote::getVoteId).map(VoteId::getUserId).toList());

        return userMapper.toUserDtos(users);
    }

    public void bookmarkPoll(User user, String pollId, boolean isBookmark) {
        Bookmark bookmark = new Bookmark(new BookmarkId(user.getId(), pollId));

        if (isBookmark) {
            findPollByIdOrElseThrow(pollId);
            bookmarkRepository.save(bookmark);
        } else {
            bookmarkRepository.delete(bookmark);
        }
    }
}
