package polling.app.polling.app.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;
import polling.app.polling.app.model.bookmark.Bookmark;
import polling.app.polling.app.model.bookmark.BookmarkId;

public interface BookmarkRepository extends MongoRepository<Bookmark, BookmarkId> {
    List<Bookmark> findAllByBookmarkIdIn(List<BookmarkId> bookmarkIds);

    List<Bookmark> findAllByBookmarkIdUserId(String userId);

    @Transactional
    void deleteAllByBookmarkIdPollId(String pollId);
}
