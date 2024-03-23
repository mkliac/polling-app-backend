package voting.app.voting.app.model.common;

import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.LastModifiedDate;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class UpdatableEntity extends CreatableEntity {
    @LastModifiedDate private Instant updatedAt;
}
