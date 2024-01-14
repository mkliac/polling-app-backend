package voting.app.voting.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import voting.app.voting.app.model.PollItem;
import voting.app.voting.app.model.User;

import java.util.List;

@Repository
public interface PollItemRepository extends MongoRepository<PollItem, String> {
    List<PollItem> findAllByIdInAndVotersContaining(List<String> ids, User voter);
}
