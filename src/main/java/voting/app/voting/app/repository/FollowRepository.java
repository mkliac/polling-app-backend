package voting.app.voting.app.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import voting.app.voting.app.model.Follow;
import voting.app.voting.app.model.FollowId;

public interface FollowRepository extends MongoRepository<Follow, FollowId> {
    List<Follow> findAllByFollowIdFollowerId(String followerId, Pageable pageable);

    List<Follow> findAllByFollowIdFolloweeId(String followeeId, Pageable pageable);

    Integer countByFollowIdFollowerId(String followerId);

    Integer countByFollowIdFolloweeId(String followeeId);
}
