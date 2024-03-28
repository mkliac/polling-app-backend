package voting.app.voting.app.dto;

import java.time.Instant;
import lombok.Data;

@Data
public class UserDto {
    private String email;

    private String username;

    private Instant createdAt;

    private Instant updatedAt;
}
