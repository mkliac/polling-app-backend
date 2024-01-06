package voting.app.voting.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import voting.app.voting.app.model.Poll;

@Repository
public interface PollRepository extends MongoRepository<Poll, String> {
}
