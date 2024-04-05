package voting.app.voting.app.model.poll;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "poll_item")
public class PollItem {
    @Id private String id;

    private String text;
}
