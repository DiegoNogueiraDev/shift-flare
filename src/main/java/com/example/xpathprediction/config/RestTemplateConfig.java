package com.example.xpathprediction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuração de beans auxiliares, como o RestTemplate.
 */
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // Poderia adicionar configurações aqui (timeouts, interceptors, etc.)
        return new RestTemplate();
    }
} 