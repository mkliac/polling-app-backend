package voting.app.voting.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import voting.app.voting.app.config.AppConfig;

@Service
@RequiredArgsConstructor
public class AppConfigService {
    private final AppConfig appConfig;

    public AppConfig getAppConfig() {
        return appConfig;
    }
}
