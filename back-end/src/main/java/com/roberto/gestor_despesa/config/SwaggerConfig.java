package com.roberto.gestor_despesa.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(info =
@Info(title = "Expense Managment APi",
        version = "v1",
        description = "The documentation of Expense Managment APi"))
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPi() {
        return new OpenAPI()
                .components(new Components())
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Expense Managment")
                        .version("v1"));
    }
}
