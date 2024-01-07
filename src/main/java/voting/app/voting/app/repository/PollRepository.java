package voting.app.voting.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import voting.app.voting.app.model.Poll;
import voting.app.voting.app.model.User;

import java.util.List;

@Repository
public interface PollRepository extends MongoRepository<Poll, String> {
    List<Poll> findAllByCreatedBy(User user);
}
