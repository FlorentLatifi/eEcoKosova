package eco.kosova.presentation.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ecoKosovaOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("EcoKosova API")
                .description("API për menaxhimin e mbeturinave (containers, zones, routes, reports)")
                .version("v1.0.0")
                .license(new License().name("Academic Use").url("https://ubt-uni.net")))
            .externalDocs(new ExternalDocumentation()
                .description("EcoKosova Documentation")
                .url("https://github.com/your-org/ecokosova"));
    }
}


