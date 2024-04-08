package polling.app.polling.app.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import polling.app.polling.app.model.vote.SingleVote;
import polling.app.polling.app.model.vote.VoteId;

@Repository
public interface VoteRepository extends MongoRepository<SingleVote, VoteId> {
    @Transactional
    void deleteAllByVoteIdPollId(String pollId);

    @Transactional
    void deleteAllByPollItemIdIn(List<String> itemIds);

    List<SingleVote> findAllByPollItemId(String pollItemId, Pageable pageable);

    List<SingleVote> findAllByVoteIdIn(List<VoteId> voteIds);
}
