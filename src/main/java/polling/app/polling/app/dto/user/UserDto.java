package polling.app.polling.app.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.Data;

@Data
public class UserDto {
    @Schema(description = "Email of the user")
    private String email;

    @Schema(description = "Username of the user")
    private String username;

    @Schema(description = "Picture url of the user")
    private String picture;

    @Schema(description = "Created time of the user")
    private Instant createdAt;

    @Schema(description = "Updated time of the user")
    private Instant updatedAt;
}
