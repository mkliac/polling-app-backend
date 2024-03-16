package voting.app.voting.app.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app-config")
@Component
@Data
public class AppConfig {
    private PollConfig pollConfig;

    @Data
    public static class PollConfig {
        private Integer maxPollItems;

        private Integer maxDescriptionLength;

        private Integer minTitleLength;

        private Integer maxTitleLength;

        private Integer maxItemTextLength;
    }
}
