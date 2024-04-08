package polling.app.polling.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "google-config")
@Component
@Data
public class GoogleConfig {
    private String clientId;

    private String clientSecret;
}
