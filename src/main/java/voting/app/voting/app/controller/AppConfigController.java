package voting.app.voting.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import voting.app.voting.app.config.AppConfig;
import voting.app.voting.app.service.AppConfigService;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("/app-config")
@SecurityRequirement(name = "Bearer Token")
public class AppConfigController {
    private final AppConfigService appConfigService;

    @GetMapping
    @Operation(
            tags = "AppConfig",
            summary = "Get App Config",
            description = "This endpoint is used to get app config")
    @ApiResponse(responseCode = "200", description = "Return app config")
    public ResponseEntity<AppConfig> getAppConfig() {
        return new ResponseEntity<>(appConfigService.getAppConfig(), HttpStatus.OK);
    }
}
