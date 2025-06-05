package co.edu.icesi.pollafutbolera.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.parameters.Parameter;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Popoya API")
                        .version("1.0")
                        .description("API documentation using Swagger in Spring Boot"));
    }

    @Bean
    public OperationCustomizer tenantHeaderCustomizer() {
        return (operation, handlerMethod) -> {
            Parameter tenantHeader = new Parameter()
                    .in("header")
                    .name("X-TenantId")
                    .description("Company/Tenant ID")
                    .required(true)
                    .schema(new io.swagger.v3.oas.models.media.Schema().type("long"));
            operation.addParametersItem(tenantHeader);
            return operation;
        };
    }

}
