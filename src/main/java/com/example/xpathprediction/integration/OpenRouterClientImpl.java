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
    
    @Value("${openrouter.use-fallback:true}")
    private boolean useFallback;

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
            
            // Adicionar headers opcionais apenas se estiverem presentes
            if (siteUrl != null && !siteUrl.isEmpty()) {
                headers.set("HTTP-Referer", siteUrl);
            }
            
            if (siteName != null && !siteName.isEmpty()) {
                headers.set("X-Title", siteName);
            }
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

            logger.info("Enviando dados para o OpenRouter: {}", openRouterUrl);
            logger.debug("Usando modelo: {}", model);
            
            try {
                ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                        openRouterUrl, requestEntity, Map.class);
    
                if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
                    String suggestedXpath = extractXPathFromResponse(responseEntity.getBody());
                    logger.info("XPath sugerido recebido: {}", suggestedXpath);
                    if (suggestedXpath == null || suggestedXpath.isBlank()) {
                        logger.warn("OpenRouter retornou um XPath vazio ou nulo.");
                        if (useFallback) {
                            return fallbackPrediction(errorXpath, pageDOM);
                        }
                        throw new XpathPredictionException("OpenRouter retornou uma sugestão inválida.");
                    }
                    return suggestedXpath;
                } else {
                    logger.error("Erro na resposta do OpenRouter. Status: {}, Body: {}", 
                                 responseEntity.getStatusCode(), responseEntity.getBody());
                    if (useFallback) {
                        return fallbackPrediction(errorXpath, pageDOM);
                    }
                    throw new XpathPredictionException("Erro na resposta do OpenRouter: Status " + responseEntity.getStatusCode());
                }
            } catch (RestClientException ex) {
                logger.error("Erro de comunicação ao chamar o OpenRouter em {}", openRouterUrl, ex);
                if (useFallback) {
                    return fallbackPrediction(errorXpath, pageDOM);
                }
                throw new XpathPredictionException("Erro de comunicação ao chamar o OpenRouter", ex);
            }
        } catch (Exception ex) {
            // Captura outras exceções inesperadas
            logger.error("Erro inesperado ao chamar o OpenRouter", ex);
            if (useFallback) {
                return fallbackPrediction(errorXpath, pageDOM);
            }
            throw new XpathPredictionException("Erro inesperado ao chamar o OpenRouter", ex);
        }
    }
    
    /**
     * Implementação de fallback para sugerir XPaths quando a API externa falha
     */
    private String fallbackPrediction(String errorXpath, String pageDOM) {
        logger.info("Usando lógica de fallback para predição de XPath");
        // Implementação simples para analisar o HTML e sugerir um XPath
        try {
            // Se o XPath contém @id, procurar um novo id no DOM
            if (errorXpath.contains("@id")) {
                // Extrair o padrão do XPath original (div, span, etc.)
                String tag = extractTagFromXPath(errorXpath);
                
                // Procurar por IDs no DOM
                List<String> ids = extractIdsFromDOM(pageDOM);
                if (!ids.isEmpty()) {
                    // Retornar um novo XPath com o ID encontrado
                    return "//" + tag + "[@id=\"" + ids.get(0) + "\"]";
                }
            }
            
            // Se o XPath contém @class, procurar uma nova classe no DOM
            if (errorXpath.contains("@class")) {
                // Extrair o padrão do XPath original (div, span, etc.)
                String tag = extractTagFromXPath(errorXpath);
                
                // Procurar por classes no DOM
                List<String> classes = extractClassesFromDOM(pageDOM);
                if (!classes.isEmpty()) {
                    // Retornar um novo XPath com a classe encontrada
                    return "//" + tag + "[@class=\"" + classes.get(0) + "\"]";
                }
            }
            
            // Fallback genérico
            return "//div[1]";
        } catch (Exception e) {
            logger.error("Erro ao gerar fallback para XPath", e);
            return "//div";
        }
    }
    
    private String extractTagFromXPath(String xpath) {
        // Extrai o nome da tag de um XPath (ex: //div[@id='foo'] -> div)
        if (xpath.contains("//")) {
            String[] parts = xpath.split("//");
            if (parts.length > 1) {
                String tagPart = parts[1];
                if (tagPart.contains("[")) {
                    return tagPart.substring(0, tagPart.indexOf("["));
                } else if (tagPart.contains("/")) {
                    return tagPart.substring(0, tagPart.indexOf("/"));
                } else {
                    return tagPart;
                }
            }
        }
        return "div"; // tag padrão
    }
    
    private List<String> extractIdsFromDOM(String dom) {
        List<String> ids = new ArrayList<>();
        int index = dom.indexOf("id=\"");
        while (index != -1) {
            int endIndex = dom.indexOf("\"", index + 4);
            if (endIndex != -1) {
                String id = dom.substring(index + 4, endIndex);
                ids.add(id);
            }
            index = dom.indexOf("id=\"", endIndex);
        }
        return ids;
    }
    
    private List<String> extractClassesFromDOM(String dom) {
        List<String> classes = new ArrayList<>();
        int index = dom.indexOf("class=\"");
        while (index != -1) {
            int endIndex = dom.indexOf("\"", index + 7);
            if (endIndex != -1) {
                String className = dom.substring(index + 7, endIndex);
                classes.add(className);
            }
            index = dom.indexOf("class=\"", endIndex);
        }
        return classes;
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