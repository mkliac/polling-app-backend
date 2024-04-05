package voting.app.voting.app.dto.user;

import java.time.Instant;
import lombok.Data;

@Data
public class UserDto {
    private String email;

    private String username;

    private String picture;

    private Instant createdAt;

    private Instant updatedAt;
}
