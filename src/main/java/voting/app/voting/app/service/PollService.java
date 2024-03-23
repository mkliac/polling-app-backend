package voting.app.voting.app.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import voting.app.voting.app.config.AppConfig;
import voting.app.voting.app.dto.*;
import voting.app.voting.app.helper.OwnerValidator;
import voting.app.voting.app.helper.PaginationHelper;
import voting.app.voting.app.mapper.PollMapper;
import voting.app.voting.app.mapper.UserMapper;
import voting.app.voting.app.model.Poll;
import voting.app.voting.app.model.PollItem;
import voting.app.voting.app.model.PollPriority;
import voting.app.voting.app.model.User;
import voting.app.voting.app.repository.PollItemRepository;
import voting.app.voting.app.repository.PollRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class PollService {
    private final PollRepository pollRepository;

    private final PollItemRepository pollItemRepository;

    private final PollMapper pollMapper;

    private final UserMapper userMapper;

    private final OwnerValidator ownerValidator;

    private final AppConfig appConfig;

    private final PaginationHelper paginationHelper;

    public PollDto getPoll(String id) {
        return pollMapper.toPollDto(findPollByIdOrElseThrow(id));
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
            default -> polls = List.of();
        }

        return pollMapper.toPollDtos(polls);
    }

    public void deletePoll(User user, String id) {
        Poll poll = findPollByIdOrElseThrow(id);
        ownerValidator.isOwnerOrElseThrow(user, poll);

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
        if (request.getClosedDate() != null && request.getClosedDate().isBefore(Instant.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Closed date must be in the future");
        }

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

    public PollDto addPollItems(User user, String pollId, AddPollItemsRequest request) {
        Poll poll = findPollByIdOrElseThrow(pollId);
        ownerValidator.isOwner(user, poll);

        List<PollItem> pollItems = pollMapper.fromItemNames(request.getTexts());
        poll.getItems().addAll(pollItems);

        pollItemRepository.saveAll(pollItems);
        poll = pollRepository.save(poll);

        return pollMapper.toPollDto(poll);
    }

    public PollDto deletePollItems(User user, String pollId, DeletePollItemsRequest request) {
        Poll poll = findPollByIdOrElseThrow(pollId);
        ownerValidator.isOwner(user, poll);

        List<String> itemIds = request.getIds();
        poll.getItems().removeIf(item -> itemIds.contains(item.getId()));

        pollItemRepository.deleteAllById(itemIds);
        poll = pollRepository.save(poll);

        return pollMapper.toPollDto(poll);
    }

    public PollDto updatePollItem(User user, String pollId, String pollItemId, String newText) {
        Poll poll = findPollByIdOrElseThrow(pollId);
        ownerValidator.isOwner(user, poll);

        PollItem pollItem = findPollItemByIdOrElseThrow(pollItemId);

        pollItem.setText(newText);
        pollItemRepository.save(pollItem);

        return pollMapper.toPollDto(poll);
    }

    private PollItem findPollItemByIdOrElseThrow(String id) {
        return pollItemRepository
                .findById(id)
                .orElseThrow(
                        () ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Poll item with id=" + id + " not found"));
    }

    public PollDto closePoll(User user, String pollId) {
        Poll poll = findPollByIdOrElseThrow(pollId);
        ownerValidator.isOwner(user, poll);

        poll.setClosedDate(Instant.now());
        poll = pollRepository.save(poll);

        return pollMapper.toPollDto(poll);
    }

    public PollDto vote(User user, String pollId, String pollItemId) {
        Poll poll = findPollByIdOrElseThrow(pollId);
        if (poll.getClosedDate() != null && poll.getClosedDate().isBefore(Instant.now())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Poll with id=" + pollId + " is closed");
        }

        List<PollItem> pollItems =
                pollItemRepository.findAllByIdInAndVotersContaining(
                        poll.getItems().stream().map(PollItem::getId).toList(), user);
        pollItems.forEach(p -> p.deleteVoter(user));

        PollItem pollItemToVote = findPollItemByIdOrElseThrow(pollItemId);
        pollItemToVote.addVoter(user);

        pollItemRepository.saveAll(
                Stream.concat(pollItems.stream(), Stream.of(pollItemToVote)).toList());

        return pollMapper.toPollDto(findPollByIdOrElseThrow(pollId));
    }

    public List<UserDto> getVoters(String itemId) {
        PollItem pollItem = findPollItemByIdOrElseThrow(itemId);
        return userMapper.toUserDtos(pollItem.getVoters().stream().toList());
    }
}
