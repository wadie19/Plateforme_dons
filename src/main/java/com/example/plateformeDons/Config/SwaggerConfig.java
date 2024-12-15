package com.example.plateformeDons.Config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setEmail("plateforme@gmail.com");
        contact.setName("Plateforme Dons");
        contact.setUrl("https://www.plateforme-dons.com");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Plateforme de Dons API")
                .version("1.0")
                .contact(contact)
                .description("Cette API expose les endpoints de la plateforme de dons.")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}