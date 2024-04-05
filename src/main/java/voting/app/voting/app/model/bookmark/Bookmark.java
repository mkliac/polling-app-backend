package voting.app.voting.app.model.bookmark;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "bookmark")
@AllArgsConstructor
@NoArgsConstructor
public class Bookmark {
    @Id private BookmarkId bookmarkId;
}
