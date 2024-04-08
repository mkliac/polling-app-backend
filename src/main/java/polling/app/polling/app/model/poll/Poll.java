package polling.app.polling.app.model.poll;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import polling.app.polling.app.model.common.CreatableEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "poll")
public class Poll extends CreatableEntity {
    @Id private String id;

    @TextIndexed private String title;

    @TextIndexed private String description;

    @DocumentReference(lazy = true)
    private List<PollItem> items = new ArrayList<>();

    private boolean isPrivate;

    private Instant closedDate;

    private PollPriority priority;

    private boolean isAnonymous;
}
