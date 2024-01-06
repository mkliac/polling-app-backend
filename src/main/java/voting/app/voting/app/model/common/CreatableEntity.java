package voting.app.voting.app.model.common;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import voting.app.voting.app.model.User;

import java.time.Instant;

@Data
public abstract class CreatableEntity {
    @CreatedDate
    private Instant createdAt;

    @DocumentReference
    private User createdBy;
}
