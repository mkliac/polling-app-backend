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
            "{$and: ["
                    + "{'isPrivate': false},"
                    + "{$or: ["
                    + "{'closedDate': {$gt: new Date()}},"
                    + "{'closedDate': null}"
                    + "]},"
                    + "{'$text' : { '$search' : ?0}}"
                    + "]}")
    List<Poll> findAllByPublic(String search, Pageable pageable);

    @Query(
            "{$and: ["
                    + "{'isPrivate': false},"
                    + "{$or: ["
                    + "{'closedDate': {$gt: new Date()}},"
                    + "{'closedDate': null}"
                    + "]}"
                    + "]}")
    List<Poll> findAllByPublic(Pageable pageable);

    @Query("{$and: [" + "{'createdBy.id': ?0}, " + "{'$text' : { '$search' : ?1}}" + "]}")
    List<Poll> findAllByCurrentUser(String userId, String search, Pageable pageable);

    @Query("{$and: [" + "{'createdBy.id': ?0}" + "]}")
    List<Poll> findAllByCurrentUser(String userId, Pageable pageable);

    @Query(
            "{$and: ["
                    + "{'createdBy.id': ?0}, "
                    + "{'isPrivate': false}, "
                    + "{'isAnonymous': false}, "
                    + "{'$text' : { '$search' : ?1}}"
                    + "]}")
    List<Poll> findAllByUser(String userId, String search, Pageable pageable);

    @Query(
            "{$and: ["
                    + "{'createdBy.id': ?0}, "
                    + "{'isPrivate': false}, "
                    + "{'isAnonymous': false}"
                    + "]}")
    List<Poll> findAllByUser(String userId, Pageable pageable);

    @Query("{$and: [" + "{'id': {$in: ?0}}, " + "{'$text' : { '$search' : ?1}}" + "]}")
    List<Poll> findAllByBookmark(List<String> pollIds, String search, Pageable pageable);

    @Query("{$and: [" + "{'id': {$in: ?0}}" + "]}")
    List<Poll> findAllByBookmark(List<String> pollIds, Pageable pageable);
}
