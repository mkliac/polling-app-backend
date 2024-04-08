package polling.app.polling.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import polling.app.polling.app.model.poll.PollItem;

@Repository
public interface PollItemRepository extends MongoRepository<PollItem, String> {}
