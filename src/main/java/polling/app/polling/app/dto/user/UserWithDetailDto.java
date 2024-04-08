package polling.app.polling.app.dto.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserWithDetailDto extends UserDto {
    private Integer pollCount;

    private Integer followerCount;

    private Integer followingCount;

    private boolean isFollowing;
}
