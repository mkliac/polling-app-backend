package voting.app.voting.app.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtConstants {
    public static final Integer BEARER_PREFIX_LENGTH = 7;

    public static final String BEARER_PREFIX = "bearer ";

    public static final String CLAIM_EMAIL = "email";

    public static final String CLAIM_NAME = "name";
}
