package com.shiftflare.xpathprediction.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Serviço para enviar diferentes tipos de prompts para a API do OpenRouter.
 */
@Service
public class AiPromptService {

    private static final Logger logger = LoggerFactory.getLogger(AiPromptService.class);

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

    public AiPromptService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Envia código para análise e retorna sugestões de code review.
     *
     * @param code O código a ser analisado
     * @param language A linguagem de programação do código
     * @return Mapa contendo o resultado da análise
     */
    public Map<String, Object> codeReview(String code, String language) {
        String prompt = buildCodeReviewPrompt(code, language);
        return sendPromptToOpenRouter(prompt);
    }

    /**
     * Envia descrição para automatização e retorna scripts de teste.
     *
     * @param description Descrição do processo a ser automatizado
     * @param framework Framework de automação a ser utilizado
     * @return Mapa contendo o resultado da automação
     */
    public Map<String, Object> generateAutomation(String description, String framework) {
        String prompt = buildAutomationPrompt(description, framework);
        return sendPromptToOpenRouter(prompt);
    }

    /**
     * Envia código para migração de Q2 para Q3 e retorna o código migrado.
     *
     * @param code O código a ser migrado
     * @param componentType O tipo de componente a ser migrado
     * @return Mapa contendo o resultado da migração
     */
    public Map<String, Object> migrateCode(String code, String componentType) {
        String prompt = buildMigrationPrompt(code, componentType);
        return sendPromptToOpenRouter(prompt);
    }

    /**
     * Método principal para enviar prompts para o OpenRouter.
     *
     * @param prompt O prompt a ser enviado
     * @return Mapa contendo a resposta do OpenRouter
     */
    private Map<String, Object> sendPromptToOpenRouter(String prompt) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Construir o payload para o OpenRouter
            var payload = new HashMap<String, Object>();
            payload.put("model", model);
            
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.add(userMessage);
            
            payload.put("messages", messages);
            
            // Armazenar o payload de requisição
            ObjectMapper objectMapper = new ObjectMapper();
            String payloadStr = objectMapper.writeValueAsString(payload);
            result.put("requestPayload", payloadStr);
            
            // Configurar os headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + apiKey);
            headers.set("User-Agent", "ShiftFlare/1.0");
            
            // Adicionar headers opcionais
            if (siteUrl != null && !siteUrl.isEmpty()) {
                headers.set("HTTP-Referer", siteUrl);
            }
            
            if (siteName != null && !siteName.isEmpty()) {
                headers.set("X-Title", siteName);
            }
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);

            logger.info("Enviando prompt para o OpenRouter: {}", openRouterUrl);
            logger.debug("Usando modelo: {}", model);
            
            try {
                ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                        openRouterUrl, requestEntity, Map.class);

                // Processa e armazena a resposta
                if (responseEntity.getBody() != null) {
                    String responseStr = objectMapper.writeValueAsString(responseEntity.getBody());
                    result.put("responsePayload", responseStr);
                    
                    // Extrair a resposta do modelo LLM
                    try {
                        Map<String, Object> responseBody = responseEntity.getBody();
                        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                        if (choices != null && !choices.isEmpty()) {
                            Map<String, Object> choice = choices.get(0);
                            Map<String, Object> message = (Map<String, Object>) choice.get("message");
                            String content = (String) message.get("content");
                            result.put("response", content);
                        }
                    } catch (Exception e) {
                        logger.error("Erro ao extrair conteúdo da resposta", e);
                        result.put("error", "Erro ao processar resposta: " + e.getMessage());
                    }
                } else {
                    result.put("error", "Resposta vazia do OpenRouter");
                }
                
                result.put("success", true);
                return result;
                
            } catch (RestClientException ex) {
                logger.error("Erro de comunicação ao chamar o OpenRouter", ex);
                result.put("error", "Erro de comunicação: " + ex.getMessage());
                result.put("success", false);
                return result;
            }
        } catch (Exception ex) {
            logger.error("Erro inesperado ao chamar o OpenRouter", ex);
            result.put("error", "Erro inesperado: " + ex.getMessage());
            result.put("success", false);
            return result;
        }
    }

    /**
     * Constrói o prompt para análise de código.
     */
    private String buildCodeReviewPrompt(String code, String language) {
        return String.format(
            "Você é um assistente especialista em análise de código e code review. " +
            "Por favor, analise o seguinte código %s e forneça uma revisão detalhada, " +
            "identificando possíveis bugs, problemas de performance, melhorias de código, " +
            "e sugestões de boas práticas.\n\n" +
            "Código a ser analisado:\n```%s\n%s\n```\n\n" +
            "Organize sua resposta no seguinte formato:\n" +
            "1. Resumo da análise\n" +
            "2. Problemas críticos encontrados (se houver)\n" +
            "3. Avisos e possíveis melhorias\n" +
            "4. Sugestões de boas práticas\n" +
            "5. Pontuação geral de qualidade (de 0 a 100)",
            language.equals("auto") ? "" : language,
            language.equals("auto") ? "" : language,
            code
        );
    }

    /**
     * Constrói o prompt para automação.
     */
    private String buildAutomationPrompt(String description, String framework) {
        return String.format(
            "Você é um especialista em automação de testes usando %s. " +
            "Crie um script de automação com base na seguinte descrição:\n\n" +
            "%s\n\n" +
            "Gere um script completo e funcional que possa ser executado imediatamente. " +
            "Organize sua resposta no seguinte formato:\n" +
            "1. Nome do teste\n" +
            "2. Descrição do cenário\n" +
            "3. Etapas do teste (detalhadas)\n" +
            "4. Código completo do script\n" +
            "5. Observações adicionais (se necessário)",
            framework,
            description
        );
    }

    /**
     * Constrói o prompt para migração de código.
     */
    private String buildMigrationPrompt(String code, String componentType) {
        return String.format(
            "Você é um especialista em migração de projetos Angular Q2 para Q3. " +
            "Por favor, migre o seguinte código Q2 para a estrutura Q3, considerando " +
            "que é um %s:\n\n" +
            "```typescript\n%s\n```\n\n" +
            "Forneça o código migrado e explique as alterações realizadas. " +
            "Organize sua resposta no seguinte formato:\n" +
            "1. Código migrado (formato Q3)\n" +
            "2. Resumo das alterações\n" +
            "3. Detalhes das mudanças principais\n" +
            "4. Possíveis problemas de compatibilidade a verificar",
            componentType.equals("autodetect") ? "componente" : componentType,
            code
        );
    }
} 