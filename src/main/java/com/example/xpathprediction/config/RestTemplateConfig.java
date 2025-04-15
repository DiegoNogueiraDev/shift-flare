package com.example.xpathprediction.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configuração do RestTemplate para chamadas HTTP.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Configura um RestTemplate com timeout otimizado para chamadas à APIs externas.
     * 
     * @return RestTemplate configurado
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // 5 segundos para timeout de conexão
        factory.setReadTimeout(30000);   // 30 segundos para timeout de leitura
        
        return new RestTemplate(factory);
    }
} 