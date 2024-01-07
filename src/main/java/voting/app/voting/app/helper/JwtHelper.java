package voting.app.voting.app.helper;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
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

    public String getCurrentRequestToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader(JwtConstants.AUTHORIZATION_HEADER);
    }
}
