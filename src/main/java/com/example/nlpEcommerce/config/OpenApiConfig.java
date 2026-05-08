package com.example.nlpEcommerce.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ecommerceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce Website")
                        .description("Natural Language E-Commerce Website")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Orkun Uyanık")
                                .email("orkunuyanik0@gmail.com")));
    }
}
