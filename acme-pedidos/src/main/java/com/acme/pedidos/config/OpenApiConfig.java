package com.acme.pedidos.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI acmeOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ACME Pedidos API")
                        .description("API REST para ciclo de abastecimiento. Transforma JSON → SOAP XML → JSON.\n\n"
                                + "**Perfiles:**\n"
                                + "- `default`: usa cliente SOAP simulado (mock)\n"
                                + "- `prod`: usa el servicio SOAP real")
                        .version("1.0.0"));
    }
}
