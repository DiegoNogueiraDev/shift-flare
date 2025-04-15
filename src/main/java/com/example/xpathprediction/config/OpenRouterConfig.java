package com.example.xpathprediction.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração e validação dos parâmetros do OpenRouter.
 */
@Configuration
public class OpenRouterConfig implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(OpenRouterConfig.class);
    
    @Value("${openrouter.url}")
    private String openRouterUrl;
    
    @Value("${openrouter.api-key}")
    private String apiKey;
    
    @Value("${openrouter.model}")
    private String model;
    
    @Value("${openrouter.use-fallback:true}")
    private boolean useFallback;
    
    /**
     * Valida as configurações do OpenRouter ao iniciar a aplicação.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("Validando configurações do OpenRouter");
        
        if (openRouterUrl == null || openRouterUrl.isBlank()) {
            logger.error("URL do OpenRouter não configurada");
            throw new IllegalStateException("URL do OpenRouter não configurada");
        }
        
        if (apiKey == null || apiKey.isBlank()) {
            logger.error("API Key do OpenRouter não configurada");
            throw new IllegalStateException("API Key do OpenRouter não configurada");
        }
        
        if (!apiKey.startsWith("sk-or-")) {
            logger.warn("API Key do OpenRouter pode estar em formato incorreto. Deve começar com 'sk-or-'");
        }
        
        if (model == null || model.isBlank()) {
            logger.error("Modelo do OpenRouter não configurado");
            throw new IllegalStateException("Modelo do OpenRouter não configurado");
        }
        
        logger.info("Configurações do OpenRouter validadas com sucesso");
        logger.info("URL: {}", openRouterUrl);
        logger.info("API Key (início): {}", apiKey.substring(0, Math.min(10, apiKey.length())) + "...");
        logger.info("Modelo: {}", model);
        logger.info("Uso de fallback: {}", useFallback);
    }
} 