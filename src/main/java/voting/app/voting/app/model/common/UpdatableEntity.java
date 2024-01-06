package voting.app.voting.app.model.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class UpdatableEntity extends CreatableEntity {
    @LastModifiedDate
    private Instant updatedAt;
}
