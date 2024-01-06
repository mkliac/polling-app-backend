package voting.app.voting.app.helper;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import org.springframework.stereotype.Component;
import voting.app.voting.app.constant.JwtConstants;

@Component
public class JwtHelper {
    public String getByClaimName(String token, String claimName) {
        token = token.substring(JwtConstants.BEARER_PREFIX_LENGTH);
        String value;
        try {
            JWT jwt = JWTParser.parse(token);
            value = jwt.getJWTClaimsSet().getClaim(claimName).toString();
        } catch (Exception e) {
            return null;
        }

        return value;
    }
}
