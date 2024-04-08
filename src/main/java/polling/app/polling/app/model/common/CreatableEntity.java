package polling.app.polling.app.model.common;

import java.time.Instant;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import polling.app.polling.app.model.user.User;

@Data
public abstract class CreatableEntity {
    @CreatedDate private Instant createdAt;

    @DocumentReference private User createdBy;
}
