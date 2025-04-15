package com.shiftflare.xpathprediction.integration;

import com.shiftflare.xpathprediction.exception.XpathPredictionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

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
            headers.set("User-Agent", "XpathPrediction/1.0");
            
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
            logger.debug("Headers de autenticação: Bearer {}", 
                    apiKey != null ? apiKey.substring(0, Math.min(10, apiKey.length())) + "..." : "null");
            
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
        // Implementação mais inteligente para analisar o HTML e sugerir um XPath
        try {
            // Verifica o tipo de elemento original para manter consistente
            String tagOriginal = extractTagFromXPath(errorXpath);
            logger.debug("Tag original do XPath: {}", tagOriginal);
            
            // Se o XPath contém @id, procurar um novo id no DOM
            if (errorXpath.contains("@id")) {
                // Extrair o ID antigo do XPath original
                String oldId = extractAttributeValueFromXPath(errorXpath, "id");
                logger.debug("ID antigo: {}", oldId);
                
                // Procurar por IDs no DOM
                List<String> ids = extractIdsFromDOM(pageDOM);
                if (!ids.isEmpty()) {
                    logger.debug("Novo ID encontrado: {}", ids.get(0));
                    // Retornar um novo XPath com o ID encontrado
                    return "//" + tagOriginal + "[@id='" + ids.get(0) + "']";
                }
            }
            
            // Se o XPath contém @class, procurar uma nova classe no DOM
            if (errorXpath.contains("@class")) {
                // Extrair a classe antiga do XPath original
                String oldClass = extractAttributeValueFromXPath(errorXpath, "class");
                logger.debug("Classe antiga: {}", oldClass);
                
                // Procurar por classes no DOM
                List<String> classes = extractClassesFromDOM(pageDOM);
                if (!classes.isEmpty()) {
                    logger.debug("Nova classe encontrada: {}", classes.get(0));
                    // Retornar um novo XPath com a classe encontrada
                    return "//" + tagOriginal + "[@class='" + classes.get(0) + "']";
                }
            }
            
            // Se o XPath contém text(), procurar texto semelhante no DOM
            if (errorXpath.contains("text()")) {
                // Extrair o texto antigo do XPath original
                String oldText = extractTextFromXPath(errorXpath);
                logger.debug("Texto antigo: {}", oldText);
                
                // Procurar elementos do mesmo tipo com texto no DOM
                List<String> textsForTag = extractTextsFromDOM(pageDOM, tagOriginal);
                if (!textsForTag.isEmpty()) {
                    logger.debug("Novo texto encontrado: {}", textsForTag.get(0));
                    // Retornar um novo XPath com o texto encontrado
                    return "//" + tagOriginal + "[text()='" + textsForTag.get(0) + "']";
                }
            }
            
            // Para XPaths com estrutura complexa (com vários níveis)
            if (errorXpath.contains("/")) {
                // Tenta construir um XPath mais específico baseado na estrutura do DOM
                String lastTag = extractLastTagFromXPath(errorXpath);
                if (!"div".equals(lastTag)) {
                    // Se o último elemento não for div, pode ser mais único (button, span, etc.)
                    return "//" + lastTag;
                }
                
                // Se temos um elemento complexo como ul/li/a, tenta ser mais específico
                if (pageDOM.contains("<" + lastTag)) {
                    logger.debug("Elemento encontrado no DOM: {}", lastTag);
                    
                    // Tenta encontrar um índice para o elemento
                    int count = countTagInDOM(pageDOM, lastTag);
                    if (count > 1) {
                        // Se houver múltiplos, tenta encontrar um índice no XPath original
                        Integer index = extractIndexFromXPath(errorXpath, lastTag);
                        if (index != null && index <= count) {
                            return "//" + lastTag + "[" + index + "]";
                        } else {
                            return "//" + lastTag + "[1]";
                        }
                    } else {
                        return "//" + lastTag;
                    }
                }
            }
            
            // Se chegamos aqui, recaímos para o caso mais genérico
            // Mas usamos o tag original para manter a consistência
            return "//" + tagOriginal + "[1]";
        } catch (Exception e) {
            logger.error("Erro ao gerar fallback para XPath", e);
            return "//div[1]";
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

    @Override
    public Map<String, Object> getSuggestedXpathWithPayloads(String errorXpath, String pageDOM) {
        Map<String, Object> result = new HashMap<>();
        
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
            
            // Armazenar o payload de requisição como string
            ObjectMapper objectMapper = new ObjectMapper();
            String payloadStr = objectMapper.writeValueAsString(payload);
            result.put("requestPayload", payloadStr);
            
            // Configurar os headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("User-Agent", "XpathPrediction/1.0");
            
            // Adicionar headers opcionais apenas se estiverem presentes
            if (siteUrl != null && !siteUrl.isEmpty()) {
                headers.set("HTTP-Referer", siteUrl);
            }
            
            if (siteName != null && !siteName.isEmpty()) {
                headers.set("X-Title", siteName);
            }
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

            logger.info("Enviando dados para o OpenRouter (com captura de payload): {}", openRouterUrl);
            logger.debug("Usando modelo: {}", model);
            logger.debug("Headers de autenticação: Bearer {}", 
                    apiKey != null ? apiKey.substring(0, Math.min(10, apiKey.length())) + "..." : "null");
            
            // LOG para depuração
            logger.debug("REQUEST HEADERS: {}", headers);
            logger.debug("REQUEST BODY: {}", payload);
            
            try {
                logger.debug("Iniciando chamada HTTP para OpenRouter...");
                ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                        openRouterUrl, requestEntity, Map.class);
                logger.debug("Resposta recebida. Status: {}", responseEntity.getStatusCode());

                // Armazenar o payload de resposta
                if (responseEntity.getBody() != null) {
                    String responseStr = objectMapper.writeValueAsString(responseEntity.getBody());
                    result.put("responsePayload", responseStr);
                    logger.debug("RESPONSE BODY: {}", responseStr);
                } else {
                    result.put("responsePayload", "Resposta nula");
                    logger.warn("Resposta com corpo nulo recebida do OpenRouter");
                }

                if (responseEntity.getStatusCode() == HttpStatus.OK && responseEntity.getBody() != null) {
                    String suggestedXpath = extractXPathFromResponse(responseEntity.getBody());
                    logger.info("XPath sugerido recebido: {}", suggestedXpath);
                    if (suggestedXpath == null || suggestedXpath.isBlank()) {
                        logger.warn("OpenRouter retornou um XPath vazio ou nulo.");
                        if (useFallback) {
                            String fallbackXpath = fallbackPrediction(errorXpath, pageDOM);
                            result.put("suggestedXpath", fallbackXpath);
                            result.put("isFallback", true);
                            return result;
                        }
                        throw new XpathPredictionException("OpenRouter retornou uma sugestão inválida.");
                    }
                    result.put("suggestedXpath", suggestedXpath);
                    result.put("isFallback", false);
                    return result;
                } else {
                    logger.error("Erro na resposta do OpenRouter. Status: {}, Body: {}", 
                                 responseEntity.getStatusCode(), responseEntity.getBody());
                    if (useFallback) {
                        String fallbackXpath = fallbackPrediction(errorXpath, pageDOM);
                        result.put("suggestedXpath", fallbackXpath);
                        result.put("isFallback", true);
                        result.put("error", "Erro na resposta: " + responseEntity.getStatusCode());
                        return result;
                    }
                    throw new XpathPredictionException("Erro na resposta do OpenRouter: Status " + responseEntity.getStatusCode());
                }
            } catch (RestClientException ex) {
                logger.error("Erro de comunicação ao chamar o OpenRouter em {}", openRouterUrl, ex);
                
                // Detalhando o erro para melhor diagnóstico
                if (ex instanceof org.springframework.web.client.HttpClientErrorException) {
                    org.springframework.web.client.HttpClientErrorException httpEx = 
                        (org.springframework.web.client.HttpClientErrorException) ex;
                    logger.error("Status do erro HTTP: {}", httpEx.getStatusCode());
                    logger.error("Corpo da resposta de erro: {}", httpEx.getResponseBodyAsString());
                    result.put("errorStatus", httpEx.getStatusCode().toString());
                    result.put("errorBody", httpEx.getResponseBodyAsString());
                }
                
                result.put("error", "Erro de comunicação: " + ex.getMessage());
                if (useFallback) {
                    String fallbackXpath = fallbackPrediction(errorXpath, pageDOM);
                    result.put("suggestedXpath", fallbackXpath);
                    result.put("isFallback", true);
                    return result;
                }
                throw new XpathPredictionException("Erro de comunicação ao chamar o OpenRouter", ex);
            }
        } catch (Exception ex) {
            // Captura outras exceções inesperadas
            logger.error("Erro inesperado ao chamar o OpenRouter", ex);
            result.put("error", "Erro inesperado: " + ex.getMessage());
            if (useFallback) {
                String fallbackXpath = fallbackPrediction(errorXpath, pageDOM);
                result.put("suggestedXpath", fallbackXpath);
                result.put("isFallback", true);
                return result;
            }
            throw new XpathPredictionException("Erro inesperado ao chamar o OpenRouter", ex);
        }
    }

    // Extrai o valor de um atributo de um XPath
    private String extractAttributeValueFromXPath(String xpath, String attributeName) {
        try {
            String pattern = "@" + attributeName + "=['\"](.*?)['\"]";
            java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = r.matcher(xpath);
            if (m.find()) {
                return m.group(1);
            }
        } catch (Exception e) {
            logger.debug("Não foi possível extrair o valor do atributo {} do XPath", attributeName);
        }
        return null;
    }
    
    // Extrai o texto de um XPath com text()
    private String extractTextFromXPath(String xpath) {
        try {
            String pattern = "text\\(\\)=['\"](.*?)['\"]";
            java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = r.matcher(xpath);
            if (m.find()) {
                return m.group(1);
            }
        } catch (Exception e) {
            logger.debug("Não foi possível extrair o texto do XPath");
        }
        return null;
    }
    
    // Extrai a tag mais específica (última) de um XPath complexo
    private String extractLastTagFromXPath(String xpath) {
        try {
            String[] parts = xpath.split("/");
            for (int i = parts.length - 1; i >= 0; i--) {
                String part = parts[i];
                if (!part.isEmpty() && !part.startsWith("@") && !part.startsWith("[")) {
                    // Remover qualquer seletor ou predicado
                    if (part.contains("[")) {
                        part = part.substring(0, part.indexOf("["));
                    }
                    return part;
                }
            }
        } catch (Exception e) {
            logger.debug("Não foi possível extrair a última tag do XPath");
        }
        return "div"; // tag padrão
    }
    
    // Extrai textos de um determinado tipo de elemento do DOM
    private List<String> extractTextsFromDOM(String dom, String tagName) {
        List<String> texts = new ArrayList<>();
        try {
            String openTag = "<" + tagName;
            String closeTag = "</" + tagName + ">";
            
            int index = dom.indexOf(openTag);
            while (index != -1) {
                int tagEnd = dom.indexOf(">", index);
                if (tagEnd != -1) {
                    int closeIndex = dom.indexOf(closeTag, tagEnd);
                    if (closeIndex != -1) {
                        String text = dom.substring(tagEnd + 1, closeIndex).trim();
                        if (!text.isEmpty()) {
                            texts.add(text);
                        }
                    }
                }
                index = dom.indexOf(openTag, tagEnd);
            }
        } catch (Exception e) {
            logger.debug("Erro ao extrair textos do DOM", e);
        }
        return texts;
    }
    
    // Conta quantas vezes uma tag aparece no DOM
    private int countTagInDOM(String dom, String tagName) {
        int count = 0;
        int index = dom.indexOf("<" + tagName);
        while (index != -1) {
            count++;
            index = dom.indexOf("<" + tagName, index + 1);
        }
        return count;
    }
    
    // Extrai o índice de um elemento em um XPath
    private Integer extractIndexFromXPath(String xpath, String tagName) {
        try {
            String pattern = tagName + "\\[(\\d+)\\]";
            java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = r.matcher(xpath);
            if (m.find()) {
                return Integer.parseInt(m.group(1));
            }
        } catch (Exception e) {
            logger.debug("Não foi possível extrair o índice do XPath");
        }
        return null;
    }
} 