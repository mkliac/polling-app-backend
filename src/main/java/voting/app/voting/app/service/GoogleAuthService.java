package voting.app.voting.app.service;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import voting.app.voting.app.config.GoogleConfig;
import voting.app.voting.app.dto.google.AuthResponse;
import voting.app.voting.app.feignclient.IGoogleClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAuthService {
    private final IGoogleClient googleClient;

    private final GoogleConfig googleConfig;

    private Map<String, String> getClientSecrets() {
        return new HashMap<>(
                Map.of(
                        "client_id", googleConfig.getClientId(),
                        "client_secret", googleConfig.getClientSecret()));
    }

    public AuthResponse getTokens(String code, String redirectUri) {
        Map<String, String> data = getClientSecrets();
        data.put("code", code);
        data.put("redirect_uri", redirectUri);
        data.put("grant_type", "authorization_code");
        AuthResponse response;
        try {
            response = googleClient.getToken(data);
        } catch (Exception e) {
            log.error("Error while getting tokens from google", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return response;
    }

    public AuthResponse refreshToken(String refreshToken) {
        Map<String, String> data = getClientSecrets();
        data.put("refresh_token", refreshToken);
        data.put("grant_type", "refresh_token");
        AuthResponse response;
        try {
            response = googleClient.getToken(data);
        } catch (Exception e) {
            log.error("Error while refreshing tokens from google", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        return response;
    }

    public void revokeToken(String token) {
        Map<String, String> data = Map.of("token", token);
        try {
            googleClient.revokeToken(data);
        } catch (Exception e) {
            log.error("Error while revoking token from google", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
