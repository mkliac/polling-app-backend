package voting.app.voting.app.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "poll")
public class Poll {
    @Id
    private String id;

    private String title;

    private String description;

    @DocumentReference
    private List<PollItem> items = new ArrayList<>();

    private Instant createdAt;

    private Instant closedAt;

    private Boolean isPrivate;

    private Instant closedDate;

    private PollPriority priority;

    private Boolean isAnonymous;
}
