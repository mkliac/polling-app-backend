package voting.app.voting.app.dto.google;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class AuthResponse {
    @JsonAlias("access_token")
    private String accessToken;

    @JsonAlias("id_token")
    private String idToken;

    @JsonAlias("refresh_token")
    private String refreshToken;

    @JsonAlias("expires_in")
    private Integer expiresIn;
}
