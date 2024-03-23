package voting.app.voting.app.model.common;

import java.time.Instant;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import voting.app.voting.app.model.User;

@Data
public abstract class CreatableEntity {
    @CreatedDate private Instant createdAt;

    @DocumentReference private User createdBy;
}
