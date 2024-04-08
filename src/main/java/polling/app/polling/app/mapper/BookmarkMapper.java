package polling.app.polling.app.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import polling.app.polling.app.model.bookmark.Bookmark;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BookmarkMapper {
    @Mapping(source = "pollId", target = "bookmarkId.pollId")
    @Mapping(source = "userId", target = "bookmarkId.userId")
    Bookmark fromIds(String pollId, String userId);
}
