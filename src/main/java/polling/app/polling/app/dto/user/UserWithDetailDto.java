package polling.app.polling.app.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserWithDetailDto extends UserDto {
    @Schema(description = "Poll count of the user")
    private Integer pollCount;

    @Schema(description = "Followers count of the user")
    private Integer followerCount;

    @Schema(description = "Following count of the user")
    private Integer followingCount;

    @Schema(description = "Is the current user following this user")
    private boolean isFollowing;
}
