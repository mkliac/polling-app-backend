package voting.app.voting.app.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;
import voting.app.voting.app.model.Bookmark;
import voting.app.voting.app.model.BookmarkId;

public interface BookmarkRepository extends MongoRepository<Bookmark, BookmarkId> {
    List<Bookmark> findAllByBookmarkIdIn(List<BookmarkId> bookmarkIds);

    List<Bookmark> findAllByBookmarkIdUserId(String userId);

    @Transactional
    void deleteAllByBookmarkIdPollId(String pollId);
}