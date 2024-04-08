package polling.app.polling.app.feignclient;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import polling.app.polling.app.dto.google.AuthResponse;

@FeignClient(name = "google", url = "https://oauth2.googleapis.com")
public interface IGoogleClient {
    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    AuthResponse getToken(Map<String, ?> data);

    @PostMapping(value = "/revoke", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void revokeToken(Map<String, ?> data);
}
