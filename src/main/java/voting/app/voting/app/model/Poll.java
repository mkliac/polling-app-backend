package voting.app.voting.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import voting.app.voting.app.model.common.CreatableEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "poll")
public class Poll extends CreatableEntity {
    @Id
    private String id;

    private String title;

    private String description;

    @DocumentReference(lazy = true)
    private List<PollItem> items = new ArrayList<>();

    private Instant closedAt;

    private Boolean isPrivate;

    private Instant closedDate;

    private PollPriority priority;

    private Boolean isAnonymous;
}
