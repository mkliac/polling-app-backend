package voting.app.voting.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import voting.app.voting.app.dto.AddPollItemsRequest;
import voting.app.voting.app.dto.DeletePollItemsRequest;
import voting.app.voting.app.dto.SavePollRequest;
import voting.app.voting.app.helper.OwnerValidator;
import voting.app.voting.app.mapper.PollMapper;
import voting.app.voting.app.model.Poll;
import voting.app.voting.app.model.PollItem;
import voting.app.voting.app.model.PollPriority;
import voting.app.voting.app.model.User;
import voting.app.voting.app.repository.PollItemRepository;
import voting.app.voting.app.repository.PollRepository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class PollService {
    private final PollRepository pollRepository;
    private final PollItemRepository pollItemRepository;

    private final PollMapper pollMapper;

    private final OwnerValidator ownerValidator;

    public Poll getPoll(String id) {
        return findPollByIdOrElseThrow(id);
    }

    public List<Poll> getPolls(User user) {
        return pollRepository.findAllByCreatedBy(user);
    }

    public void deletePoll(User user, String id) {
        Poll poll = findPollByIdOrElseThrow(id);
        ownerValidator.isOwnerOrElseThrow(user, poll);

        pollItemRepository.deleteAll(poll.getItems());
        pollRepository.delete(poll);
    }

    private Poll findPollByIdOrElseThrow(String id) {
        return pollRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                            "Poll with id=" + id + " not found"));
    }

    public Poll savePoll(User createdBy, SavePollRequest request) {
        Poll poll = pollMapper.fromSavePollRequest(createdBy, request);
        poll.setPriority(PollPriority.NORMAL);

        pollItemRepository.saveAll(poll.getItems());
        poll = pollRepository.save(poll);
        return poll;
    }

    public Poll addPollItems(User user, String pollId, AddPollItemsRequest request) {
        Poll poll = findPollByIdOrElseThrow(pollId);
        ownerValidator.isOwner(user, poll);

        List<PollItem> pollItems = pollMapper.fromItemNames(request.getTexts());
        poll.getItems().addAll(pollItems);

        pollItemRepository.saveAll(pollItems);
        poll = pollRepository.save(poll);

        return poll;
    }

    public Poll deletePollItems(User user, String pollId, DeletePollItemsRequest request) {
        Poll poll = findPollByIdOrElseThrow(pollId);
        ownerValidator.isOwner(user, poll);

        List<String> itemIds = request.getIds();
        poll.getItems().removeIf(item -> itemIds.contains(item.getId()));

        pollItemRepository.deleteAllById(itemIds);
        poll = pollRepository.save(poll);

        return poll;
    }

    public Poll updatePollItem(User user, String pollId, String pollItemId, String newText) {
        Poll poll = findPollByIdOrElseThrow(pollId);
        ownerValidator.isOwner(user, poll);

        PollItem pollItem = findPollItemByIdOrElseThrow(pollItemId);

        pollItem.setText(newText);
        pollItemRepository.save(pollItem);

        return poll;
    }

    private PollItem findPollItemByIdOrElseThrow(String id) {
        return pollItemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Poll item with id=" + id + " not found"));
    }

    public Poll closePoll(User user, String pollId) {
        Poll poll = findPollByIdOrElseThrow(pollId);
        ownerValidator.isOwner(user, poll);

        poll.setClosedDate(Instant.now());
        poll = pollRepository.save(poll);

        return poll;
    }

    public Poll vote(User user, String pollId, String pollItemId) {
        Poll poll = findPollByIdOrElseThrow(pollId);
        if (poll.getClosedDate() != null && poll.getClosedDate().isBefore(Instant.now())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll with id=" + pollId + " is closed");
        }

        List<PollItem> pollItems = pollItemRepository.findAllByIdInAndVotersContaining(poll.getItems().stream().map(PollItem::getId).toList(), user);
        pollItems.forEach(p -> p.deleteVoter(user));

        PollItem pollItemToVote = findPollItemByIdOrElseThrow(pollItemId);
        pollItemToVote.addVoter(user);

        pollItemRepository.saveAll(Stream.concat(pollItems.stream(), Stream.of(pollItemToVote)).toList());

        return findPollByIdOrElseThrow(pollId);
    }
}
