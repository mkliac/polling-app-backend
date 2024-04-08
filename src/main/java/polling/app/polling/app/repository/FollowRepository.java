package polling.app.polling.app.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import polling.app.polling.app.model.follow.Follow;
import polling.app.polling.app.model.follow.FollowId;

public interface FollowRepository extends MongoRepository<Follow, FollowId> {
    List<Follow> findAllByFollowIdFollowerId(String followerId, Pageable pageable);

    List<Follow> findAllByFollowIdFolloweeId(String followeeId, Pageable pageable);

    Integer countByFollowIdFollowerId(String followerId);

    Integer countByFollowIdFolloweeId(String followeeId);
}
