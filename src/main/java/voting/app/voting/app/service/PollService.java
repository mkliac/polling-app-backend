package voting.app.voting.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import voting.app.voting.app.dto.SavePollRequest;
import voting.app.voting.app.mapper.PollMapper;
import voting.app.voting.app.model.Poll;
import voting.app.voting.app.model.PollItem;
import voting.app.voting.app.model.PollPriority;
import voting.app.voting.app.model.User;
import voting.app.voting.app.repository.PollItemRepository;
import voting.app.voting.app.repository.PollRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PollService {
    private final PollRepository pollRepository;
    private final PollItemRepository pollItemRepository;

    private final PollMapper pollMapper;

    public Poll getPoll(String id) {
        return pollRepository.findById(id).orElseThrow();
    }

    public List<Poll> getPolls(User user) {
        return pollRepository.findAllByCreatedBy(user);
    }

    //TODO: 1.add validations e.g. updater check, error msg, input parameters...
    //      2.other APIs to update Poll statuses

    public void deletePoll(String id) {
        Poll poll = pollRepository.findById(id).orElseThrow();

        pollItemRepository.deleteAll(poll.getItems());
        pollRepository.delete(poll);
    }

    public Poll savePoll(User createdBy, SavePollRequest request) {
        Poll poll = pollMapper.fromSavePollRequest(createdBy, request);
        poll.setPriority(PollPriority.NORMAL);

        pollItemRepository.saveAll(poll.getItems());
        poll = pollRepository.save(poll);
        return poll;
    }

    public Poll addPollItems(String pollId, List<String> itemTexts) {
        Poll poll = pollRepository.findById(pollId).orElseThrow();
        List<PollItem> pollItems = pollMapper.fromItemNames(itemTexts);
        poll.getItems().addAll(pollItems);

        pollItemRepository.saveAll(pollItems);
        poll = pollRepository.save(poll);

        return poll;
    }

    public Poll deletePollItems(String pollId, List<String> itemIds) {
        Poll poll = pollRepository.findById(pollId).orElseThrow();
        poll.getItems().removeIf(item -> itemIds.contains(item.getId()));

        pollItemRepository.deleteAllById(itemIds);
        poll = pollRepository.save(poll);

        return poll;
    }

    public Poll updatePollItem(String pollId, String pollItemId, String newText) {
        PollItem pollItem = pollItemRepository.findById(pollItemId).orElseThrow();
        pollItem.setText(newText);
        pollItemRepository.save(pollItem);

        return pollRepository.findById(pollId).orElseThrow();
    }
}
