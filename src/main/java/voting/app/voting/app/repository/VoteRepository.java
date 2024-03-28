package voting.app.voting.app.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import voting.app.voting.app.model.Vote;
import voting.app.voting.app.model.VoteId;

@Repository
public interface VoteRepository extends MongoRepository<Vote, VoteId> {
    @Transactional
    void deleteAllByVoteIdIn(List<VoteId> voteIds);

    List<Vote> findAllByVoteIdPollItemId(String pollItemId, Pageable pageable);

    Integer countByVoteIdPollItemId(String pollItemId);

    List<Vote> findAllByVoteIdIn(List<VoteId> voteIds);
}
