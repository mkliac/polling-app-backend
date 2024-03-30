package voting.app.voting.app.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtConstants {
    public static final Integer BEARER_PREFIX_LENGTH = 7;

    public static final String BEARER_PREFIX = "bearer ";

    public static final String CLAIM_EMAIL = "email";

    public static final String CLAIM_NAME = "name";

    public static final String CLAIM_PICTURE = "picture";

    public static final String AUTHORIZATION_HEADER = "Authorization";
}
