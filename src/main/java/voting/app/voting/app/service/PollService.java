package voting.app.voting.app.service;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import voting.app.voting.app.config.AppConfig;
import voting.app.voting.app.dto.poll.PollDto;
import voting.app.voting.app.dto.poll.PollFilterType;
import voting.app.voting.app.dto.poll.SavePollRequest;
import voting.app.voting.app.dto.poll.UpdatePollRequest;
import voting.app.voting.app.dto.user.UserDto;
import voting.app.voting.app.helper.PaginationHelper;
import voting.app.voting.app.mapper.PollMapper;
import voting.app.voting.app.mapper.UserMapper;
import voting.app.voting.app.model.bookmark.Bookmark;
import voting.app.voting.app.model.bookmark.BookmarkId;
import voting.app.voting.app.model.poll.Poll;
import voting.app.voting.app.model.poll.PollItem;
import voting.app.voting.app.model.poll.PollPriority;
import voting.app.voting.app.model.user.User;
import voting.app.voting.app.model.vote.SingleVote;
import voting.app.voting.app.model.vote.VoteId;
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
            User currentUser,
            String userId,
            PollFilterType filterType,
            String search,
            boolean isAscending,
            String sortBy,
            Integer pageNumber,
            Integer pageSize) {
        Pageable pageable = paginationHelper.getPageable(pageNumber, pageSize, isAscending, sortBy);

        List<Poll> polls;
        switch (filterType) {
            case PUBLIC -> polls =
                    search.isEmpty()
                            ? pollRepository.findAllByPublic(pageable)
                            : pollRepository.findAllByPublic(search, pageable);
            case MY_POLLS -> polls =
                    search.isEmpty()
                            ? pollRepository.findAllByCurrentUser(currentUser.getId(), pageable)
                            : pollRepository.findAllByCurrentUser(
                                    currentUser.getId(), search, pageable);
            case USER -> polls =
                    search.isEmpty()
                            ? pollRepository.findAllByUser(userId, pageable)
                            : pollRepository.findAllByUser(userId, search, pageable);
            case BOOKMARKED -> {
                // NOTE: Might not be the most efficient way to get bookmarked polls by page
                List<String> bookmarkedPollIds =
                        bookmarkRepository.findAllByBookmarkIdUserId(currentUser.getId()).stream()
                                .map(Bookmark::getBookmarkId)
                                .map(BookmarkId::getPollId)
                                .toList();
                polls =
                        search.isEmpty()
                                ? pollRepository.findAllByBookmark(bookmarkedPollIds, pageable)
                                : pollRepository.findAllByBookmark(
                                        bookmarkedPollIds, search, pageable);
            }
            default -> polls = List.of();
        }

        return pollMapper.toPollDtos(polls, currentUser);
    }

    public void deletePoll(User user, String id) {
        Poll poll = findPollByIdOrElseThrow(id);
        ownerValidator.isOwnerOrElseThrow(user, poll);

        voteRepository.deleteAllByVoteIdPollId(id);
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
        validateWithPollConfig(request.getTitle(), request.getDescription());

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

    private void validateWithPollConfig(String title, String description) {
        AppConfig.PollConfig pollConfig = appConfig.getPollConfig();
        if (title.length() < pollConfig.getMinTitleLength()
                || title.length() > pollConfig.getMaxTitleLength()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Title length must be between "
                            + pollConfig.getMinTitleLength()
                            + " and "
                            + pollConfig.getMaxTitleLength());
        }

        if (description.length() > pollConfig.getMaxDescriptionLength()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Description length must be less than " + pollConfig.getMaxDescriptionLength());
        }
    }

    private void validateUpdatePollRequest(Poll poll, UpdatePollRequest request) {
        validateWithPollConfig(request.getTitle(), request.getDescription());

        Set<String> texts =
                poll.getItems().stream()
                        .filter(pollItem -> !request.getRemoveItemIds().contains(pollItem.getId()))
                        .map(PollItem::getText)
                        .collect(Collectors.toSet());
        texts.addAll(request.getAddItemTexts());
        if (texts.size()
                != poll.getItems().size()
                        - request.getRemoveItemIds().size()
                        + request.getAddItemTexts().size()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Poll items must be unique");
        }
        if (texts.size() > appConfig.getPollConfig().getMaxPollItems()) {
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

        voteRepository.deleteAllByPollItemIdIn(ids);
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

        if (!pollItemRepository.existsById(pollItemId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Poll item with id=" + pollItemId + " not found");
        }

        VoteId voteId = new VoteId(user.getId(), pollId);
        SingleVote vote =
                voteRepository.findById(voteId).orElse(new SingleVote(voteId, pollItemId));
        vote.setPollItemId(pollItemId);
        voteRepository.save(vote);

        return pollMapper.toPollDto(poll, user);
    }

    public List<UserDto> getVoters(
            String pollId, String itemId, Integer pageNumber, Integer pageSize) {
        if (findPollByIdOrElseThrow(pollId).isAnonymous()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Poll with id=" + pollId + " is anonymous");
        }

        Pageable pageable = paginationHelper.getPageable(pageNumber, pageSize, true);
        List<SingleVote> votes = voteRepository.findAllByPollItemId(itemId, pageable);
        List<User> users =
                userRepository.findAllByIdIn(
                        votes.stream().map(SingleVote::getVoteId).map(VoteId::getUserId).toList());

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
