package polling.app.polling.app.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import polling.app.polling.app.model.user.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    List<User> findAllByIdIn(List<String> ids);
}
