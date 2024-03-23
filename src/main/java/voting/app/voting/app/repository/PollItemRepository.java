package voting.app.voting.app.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import voting.app.voting.app.model.PollItem;
import voting.app.voting.app.model.User;

@Repository
public interface PollItemRepository extends MongoRepository<PollItem, String> {
    List<PollItem> findAllByIdInAndVotersContaining(List<String> ids, User voter);
}
