package voting.app.voting.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import voting.app.voting.app.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
}
