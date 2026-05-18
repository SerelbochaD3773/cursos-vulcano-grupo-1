package com.grupo1.cursosvulcano.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI vulcanoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Vúlcano")
                        .description("""
                                Backend para un sistema de refuerzo educativo en programación.
                                
                                Permite administrar **Cursos**, **Modulos**, **Usuarios**, **Agendas de Clases** y **Opiniones** con:
                                - Herencia JPA (`@MappedSuperclass`)
                                - Enumeraciones (`@Enumerated`)
                                - Auditoría automática (`@CreatedDate` / `@LastModifiedDate`)
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo de Desarrollo")
                                .email("dev@vulcano.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
