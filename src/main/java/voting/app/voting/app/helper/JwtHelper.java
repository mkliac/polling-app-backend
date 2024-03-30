package voting.app.voting.app.helper;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import voting.app.voting.app.constant.JwtConstants;
import voting.app.voting.app.model.User;

@Component
public class JwtHelper {
    public String getByClaimName(String token, String claimName) {
        try {
            token = token.substring(JwtConstants.BEARER_PREFIX_LENGTH);
            JWT jwt = JWTParser.parse(token);
            return jwt.getJWTClaimsSet().getClaim(claimName).toString();
        } catch (Exception e) {
            return null;
        }
    }

    public String getCurrentRequestToken() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        return request.getHeader(JwtConstants.AUTHORIZATION_HEADER);
    }

    public String getCurrentUserId() {
        return getByClaimName(getCurrentRequestToken(), JwtConstants.CLAIM_EMAIL);
    }

    public User getUser(String token) {
        String email = getByClaimName(token, JwtConstants.CLAIM_EMAIL),
                name = getByClaimName(token, JwtConstants.CLAIM_NAME),
                picture = getByClaimName(token, JwtConstants.CLAIM_PICTURE);
        return User.builder().id(email).username(name).picture(picture).build();
    }

    public User getCurrentUser() {
        return getUser(getCurrentRequestToken());
    }
}
