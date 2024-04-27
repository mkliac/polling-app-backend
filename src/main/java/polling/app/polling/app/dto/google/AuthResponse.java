package polling.app.polling.app.dto.google;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AuthResponse {
    @JsonAlias("access_token")
    @Schema(description = "Access token of the user")
    private String accessToken;

    @JsonAlias("id_token")
    @Schema(description = "Id token of the user")
    private String idToken;

    @JsonAlias("refresh_token")
    @Schema(description = "Refresh token of the user")
    private String refreshToken;

    @JsonAlias("expires_in")
    @Schema(description = "Expiry time of the token")
    private Integer expiresIn;
}
