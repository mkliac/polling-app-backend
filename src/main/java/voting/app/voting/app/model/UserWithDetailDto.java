package voting.app.voting.app.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import voting.app.voting.app.dto.UserDto;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserWithDetailDto extends UserDto {
    private Integer pollCount;

    private Integer followerCount;

    private Integer followingCount;

    private boolean isFollowing;
}
