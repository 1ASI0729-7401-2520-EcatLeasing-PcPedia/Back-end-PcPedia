package com.pcpedia.api.shared.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Value("${WEBSITE_HOSTNAME:}")
    private String azureHostname;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        Server server;
        if (azureHostname != null && !azureHostname.isEmpty()) {
            server = new Server()
                    .url("https://" + azureHostname)
                    .description("Production Server");
        } else {
            server = new Server()
                    .url("http://localhost:" + serverPort)
                    .description("Development Server");
        }

        return new OpenAPI()
                .info(new Info()
                        .title("PcPedia API")
                        .version("1.0.0")
                        .description("Sistema de Arrendamiento Tecnologico Inteligente - ECAT Leasing")
                        .contact(new Contact()
                                .name("ECAT Leasing")
                                .email("contacto@ecatleasing.com")
                                .url("https://www.ecatleasing.com"))
                        .license(new License()
                                .name("Proprietary")
                                .url("https://www.ecatleasing.com/license")))
                .servers(List.of(server))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Enter JWT token")));
    }
}
