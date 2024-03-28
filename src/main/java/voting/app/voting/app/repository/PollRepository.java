package voting.app.voting.app.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import voting.app.voting.app.model.Poll;

@Repository
public interface PollRepository extends MongoRepository<Poll, String> {
    @Query(
            "{$or: ["
                    + "{'title': {$regex: ?0, $options: 'i'}}, "
                    + "{'description': {$regex: ?0, $options: 'i'}}"
                    + "]}")
    List<Poll> findAll(String filter, Pageable pageable);

    @Query(
            "{$and: ["
                    + "{'createdBy.id': ?0}, "
                    + "{$or: ["
                    + "{'title': {$regex: ?1, $options: 'i'}}, "
                    + "{'description': {$regex: ?1, $options: 'i'}}"
                    + "]}"
                    + "]}")
    List<Poll> findAllByCreatedBy(String userId, String filter, Pageable pageable);

    @Query(
            "{$and: ["
                    + "{'id': {$in: ?0}}, "
                    + "{'createdBy.id': ?1}, "
                    + "{$or: ["
                    + "{'title': {$regex: ?2, $options: 'i'}}, "
                    + "{'description': {$regex: ?2, $options: 'i'}}"
                    + "]}"
                    + "]}")
    List<Poll> findAllByBookmark(
            List<String> pollIds, String userId, String filter, Pageable pageable);
}
