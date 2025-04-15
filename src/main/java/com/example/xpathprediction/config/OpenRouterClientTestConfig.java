package com.example.xpathprediction.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Configuração para testar a conexão com o OpenRouter durante a inicialização.
 */
@Configuration
public class OpenRouterClientTestConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(OpenRouterClientTestConfig.class);
    
    @Bean
    public CommandLineRunner testOpenRouterConnection(
            RestTemplate restTemplate,
            @Value("${openrouter.url}") String openRouterUrl,
            @Value("${openrouter.api-key}") String apiKey,
            @Value("${openrouter.model}") String model,
            @Value("${openrouter.site-url:}") String siteUrl,
            @Value("${openrouter.site-name:}") String siteName) {
        
        return args -> {
            logger.info("======== TESTE DE CONEXÃO COM OPENROUTER ========");
            logger.info("Testando conexão com: {}", openRouterUrl);
            logger.info("Usando modelo: {}", model);
            logger.info("API Key (início): {}", apiKey.substring(0, Math.min(10, apiKey.length())) + "...");
            
            try {
                // Preparar headers exatamente como no exemplo
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.set("Authorization", "Bearer " + apiKey);
                
                if (siteUrl != null && !siteUrl.isBlank()) {
                    headers.set("HTTP-Referer", siteUrl);
                    logger.info("HTTP-Referer: {}", siteUrl);
                }
                
                if (siteName != null && !siteName.isBlank()) {
                    headers.set("X-Title", siteName);
                    logger.info("X-Title: {}", siteName);
                }
                
                // Criar payload simples para teste
                Map<String, Object> payload = new HashMap<>();
                payload.put("model", model);
                
                List<Map<String, String>> messages = new ArrayList<>();
                Map<String, String> message = new HashMap<>();
                message.put("role", "user");
                message.put("content", "Hello, this is a test message");
                messages.add(message);
                
                payload.put("messages", messages);
                
                HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);
                
                logger.info("Enviando requisição de teste para o OpenRouter...");
                logger.debug("Payload: {}", payload);
                logger.debug("Headers: {}", headers);
                
                try {
                    ResponseEntity<Map> response = restTemplate.postForEntity(
                            openRouterUrl, requestEntity, Map.class);
                    
                    logger.info("Resposta recebida! Status: {}", response.getStatusCode());
                    if (response.getStatusCode() == HttpStatus.OK) {
                        logger.info("✓ Conexão com OpenRouter estabelecida com sucesso!");
                        if (response.getBody() != null) {
                            logger.debug("Resposta: {}", response.getBody());
                        }
                    } else {
                        logger.warn("✗ Conexão estabelecida, mas com código de status inesperado: {}", 
                                response.getStatusCode());
                    }
                } catch (Exception e) {
                    logger.error("✗ Falha ao testar conexão com OpenRouter", e);
                    logger.error("Detalhes do erro: {}", e.getMessage());
                    
                    // Instruções para solução de problemas
                    logger.info("");
                    logger.info("INSTRUÇÕES PARA SOLUÇÃO DE PROBLEMAS:");
                    logger.info("1. Verifique se a chave API está correta e ativa");
                    logger.info("2. Verifique se o formato da chave está correto (deve começar com 'sk-or-')");
                    logger.info("3. Verifique se o modelo '{}' está disponível na sua conta", model);
                    logger.info("4. Teste a chave usando curl ou Postman com o comando:");
                    logger.info("   curl -X POST {} \\", openRouterUrl);
                    logger.info("     -H \"Authorization: Bearer {}\" \\", "sk-or-****");
                    logger.info("     -H \"Content-Type: application/json\" \\");
                    logger.info("     -d '{\"model\":\"{}\",\"messages\":[{\"role\":\"user\",\"content\":\"Test\"}]}'", model);
                }
                
            } catch (Exception e) {
                logger.error("✗ Falha ao configurar teste de conexão", e);
            }
            
            logger.info("=================================================");
        };
    }
} 