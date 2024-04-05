package voting.app.voting.app.model.user;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import voting.app.voting.app.model.common.UpdatableEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "user")
@Builder
public class User extends UpdatableEntity {
    @Id private String id;

    private String username;

    private String picture;
}
