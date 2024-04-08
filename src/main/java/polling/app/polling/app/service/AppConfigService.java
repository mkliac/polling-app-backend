package polling.app.polling.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import polling.app.polling.app.config.AppConfig;

@Service
@RequiredArgsConstructor
public class AppConfigService {
    private final AppConfig appConfig;

    public AppConfig getAppConfig() {
        return appConfig;
    }
}
