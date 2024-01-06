package voting.app.voting.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import voting.app.voting.app.model.Poll;
import voting.app.voting.app.model.PollItem;
import voting.app.voting.app.model.PollPriority;
import voting.app.voting.app.repository.PollItemRepository;
import voting.app.voting.app.repository.PollRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PollService {
    private final PollRepository pollRepository;
    private final PollItemRepository pollItemRepository;

    public Poll getPoll(String id) {
        return pollRepository.findById(id).orElseThrow();
    }

    public void deletePoll(String id) {
        Poll poll = pollRepository.findById(id).orElseThrow();

        pollItemRepository.deleteAll(poll.getItems());
        pollRepository.delete(poll);
    }

    //TODO: 1.update input parameters,
    //      2.use mapstruct to convert request -> poll & pollItem
    //      3.find an advance way to update createdAt & create CreatableEntity + UpdatableEntity
    //      4.use AOP to update user table when new user comes in, Custom Annotation for extract User Entity from DB

    public Poll savePoll() {
        Poll poll = new Poll();
        poll.setId(UUID.randomUUID().toString());
        poll.setPriority(PollPriority.HIGH);

        PollItem pollItem = new PollItem();
        pollItem.setId(UUID.randomUUID().toString());

        poll.getItems().add(pollItem);
        pollRepository.save(poll);
        pollItemRepository.save(pollItem);

        return poll;
    }
}
