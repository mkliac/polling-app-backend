package polling.app.polling.app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(
                        new Components()
                                .addSecuritySchemes(
                                        "BEARER TOKEN",
                                        new SecurityScheme()
                                                .scheme("bearer")
                                                .type(SecurityScheme.Type.HTTP)
                                                .in(SecurityScheme.In.HEADER)
                                                .bearerFormat("JWT")
                                                .name("BEARER TOKEN")));
    }
}
