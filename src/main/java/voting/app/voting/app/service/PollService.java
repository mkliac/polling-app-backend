package voting.app.voting.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import voting.app.voting.app.dto.SavePollRequest;
import voting.app.voting.app.mapper.PollMapper;
import voting.app.voting.app.model.Poll;
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

    public Poll getPoll(String id) {
        return pollRepository.findById(id).orElseThrow();
    }

    public void deletePoll(String id) {
        Poll poll = pollRepository.findById(id).orElseThrow();

        pollItemRepository.deleteAll(poll.getItems());
        pollRepository.delete(poll);
    }

    public Poll savePoll(User createdBy, SavePollRequest request) {
        Poll poll = pollMapper.fromSavePollRequest(createdBy, request);
        poll.setPriority(PollPriority.NORMAL);

        poll = pollRepository.save(poll);
        pollItemRepository.saveAll(poll.getItems());

        return poll;
    }
}
