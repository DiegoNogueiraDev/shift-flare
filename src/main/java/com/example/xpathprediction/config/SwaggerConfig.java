package com.example.xpathprediction.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do Swagger/OpenAPI para documentação da API.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI xpathPredictionOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Xpath Prediction API")
                .description("API para predição de novos XPaths com integração ao OpenRouter.")
                .version("v1.0.0") // Convenção comum usar 'v' no início
                .license(new License().name("Apache 2.0").url("http://springdoc.org"))) // Exemplo de licença
                .externalDocs(new ExternalDocumentation()
                .description("Documentação Completa (Exemplo)")
                .url("https://example.com/docs")); // URL de exemplo para documentação externa
    }
} 