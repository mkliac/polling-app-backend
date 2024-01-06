package voting.app.voting.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import voting.app.voting.app.model.common.UpdatableEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "user")
@AllArgsConstructor
public class User extends UpdatableEntity {
    @Id
    private String email;

    private String username;
}
