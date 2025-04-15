package com.example.xpathprediction.integration;

import com.example.xpathprediction.exception.XpathPredictionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Implementação do cliente para chamada ao OpenRouter.
 */
@Component
public class OpenRouterClientImpl implements OpenRouterClient {

    private static final Logger logger = LoggerFactory.getLogger(OpenRouterClientImpl.class);

    private final RestTemplate restTemplate;
    
    @Value("${openrouter.url}")
    private String openRouterUrl;
    
    @Value("${openrouter.api-key}")
    private String apiKey;
    
    @Value("${openrouter.site-url}")
    private String siteUrl;
    
    @Value("${openrouter.site-name}")
    private String siteName;
    
    @Value("${openrouter.model}")
    private String model;

    public OpenRouterClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String getSuggestedXpath(String errorXpath, String pageDOM) {
        try {
            // Construir o prompt para o modelo
            String prompt = buildPrompt(errorXpath, pageDOM);
            
            // Construir o payload para o OpenRouter
            var payload = new HashMap<String, Object>();
            payload.put("model", model);
            
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.add(userMessage);
            
            payload.put("messages", messages);
            
            // Configurar os headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("HTTP-Referer", siteUrl);
            headers.set("X-Title", siteName);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

            logger.info("Enviando dados para o OpenRouter: {}", openRouterUrl);
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                    openRouterUrl, requestEntity, Map.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
                String suggestedXpath = extractXPathFromResponse(responseEntity.getBody());
                logger.info("XPath sugerido recebido: {}", suggestedXpath);
                if (suggestedXpath == null || suggestedXpath.isBlank()) {
                    logger.warn("OpenRouter retornou um XPath vazio ou nulo.");
                    throw new XpathPredictionException("OpenRouter retornou uma sugestão inválida.");
                }
                return suggestedXpath;
            } else {
                logger.error("Erro na resposta do OpenRouter. Status: {}, Body: {}", 
                               responseEntity.getStatusCode(), responseEntity.getBody());
                throw new XpathPredictionException("Erro na resposta do OpenRouter: Status " + responseEntity.getStatusCode());
            }
        } catch (RestClientException ex) {
            logger.error("Erro de comunicação ao chamar o OpenRouter em {}", openRouterUrl, ex);
            throw new XpathPredictionException("Erro de comunicação ao chamar o OpenRouter", ex);
        } catch (Exception ex) {
            // Captura outras exceções inesperadas
            logger.error("Erro inesperado ao chamar o OpenRouter", ex);
            throw new XpathPredictionException("Erro inesperado ao chamar o OpenRouter", ex);
        }
    }
    
    /**
     * Constrói o prompt para enviar ao modelo de linguagem.
     *
     * @param errorXpath XPath com erro
     * @param pageDOM DOM da página
     * @return Prompt formatado
     */
    private String buildPrompt(String errorXpath, String pageDOM) {
        return String.format(
            "Você é um especialista em XPath para testes de automação web. " +
            "O seguinte XPath não está mais funcionando: %s\n\n" +
            "Abaixo está o DOM atual da página:\n%s\n\n" +
            "Por favor, forneça apenas um novo XPath válido que poderia substituir o XPath com erro, " +
            "sem explicações adicionais. Retorne apenas o novo XPath.",
            errorXpath, pageDOM
        );
    }
    
    /**
     * Extrai o XPath sugerido da resposta da API.
     *
     * @param response Resposta do OpenRouter
     * @return XPath sugerido
     */
    @SuppressWarnings("unchecked")
    private String extractXPathFromResponse(Map response) {
        try {
            List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
            if (choices != null && !choices.isEmpty()) {
                Map<String, Object> firstChoice = choices.get(0);
                Map<String, Object> message = (Map<String, Object>) firstChoice.get("message");
                if (message != null) {
                    String content = (String) message.get("content");
                    // Remover espaços em branco e garantir que seja um XPath válido
                    return content != null ? content.trim() : null;
                }
            }
            logger.warn("Formato de resposta inesperado do OpenRouter: {}", response);
            return null;
        } catch (Exception e) {
            logger.error("Erro ao extrair XPath da resposta", e);
            return null;
        }
    }
} 