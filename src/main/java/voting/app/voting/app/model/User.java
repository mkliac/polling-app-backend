package voting.app.voting.app.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import voting.app.voting.app.model.common.UpdatableEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "user")
@Builder
public class User extends UpdatableEntity {
    @Id
    private String id;

    @Indexed
    private String email;

    private String username;
}
