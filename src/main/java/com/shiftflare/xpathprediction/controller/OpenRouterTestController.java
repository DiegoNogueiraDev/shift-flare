package com.shiftflare.xpathprediction.controller;

import com.shiftflare.xpathprediction.domain.OpenRouterTestResult;
import com.shiftflare.xpathprediction.service.OpenRouterTestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para executar os testes do OpenRouter.
 */
@RestController
@RequestMapping("/api/test")
@Tag(name = "OpenRouter Test", description = "Endpoints para testar a integração com o OpenRouter")
public class OpenRouterTestController {

    private static final Logger logger = LoggerFactory.getLogger(OpenRouterTestController.class);
    private final OpenRouterTestService openRouterTestService;
    
    @Autowired
    private Environment environment;

    @Autowired
    public OpenRouterTestController(OpenRouterTestService openRouterTestService) {
        this.openRouterTestService = openRouterTestService;
    }

    /**
     * Endpoint para executar todos os cenários de teste.
     * @return Resultados dos testes executados
     */
    @GetMapping("/run-all")
    @Operation(summary = "Executar todos os cenários de teste", 
               description = "Executa todos os cenários de teste e retorna os resultados básicos")
    public ResponseEntity<List<OpenRouterTestResult>> executarTodosOsCenarios() {
        logger.info("Iniciando execução de todos os cenários de teste");
        List<OpenRouterTestResult> resultados = openRouterTestService.testarCenarios();
        logger.info("Testes concluídos com sucesso: {} cenários executados", resultados.size());
        
        // Exibe estatísticas básicas dos testes
        long sucessos = resultados.stream().filter(OpenRouterTestResult::isSucesso).count();
        long falhas = resultados.size() - sucessos;
        logger.info("Estatísticas: {} sucessos, {} falhas", sucessos, falhas);
        
        return ResponseEntity.ok(resultados);
    }
    
    /**
     * Endpoint para executar todos os cenários de teste e mostrar os resultados em formato detalhado
     * incluindo os payloads de requisição e resposta.
     * @return Resultados detalhados dos testes executados
     */
    @GetMapping("/run-all/detailed")
    @Operation(summary = "Executar todos os cenários com detalhes", 
               description = "Executa todos os cenários e retorna resultados detalhados incluindo payloads")
    public ResponseEntity<Map<String, Object>> executarTodosOsCenariosDetalhado() {
        logger.info("Iniciando execução detalhada de todos os cenários de teste");
        List<OpenRouterTestResult> resultados = openRouterTestService.testarCenarios();
        
        Map<String, Object> responseMap = new HashMap<>();
        
        // Estatísticas gerais
        long sucessos = resultados.stream().filter(OpenRouterTestResult::isSucesso).count();
        long falhas = resultados.size() - sucessos;
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCenarios", resultados.size());
        stats.put("sucessos", sucessos);
        stats.put("falhas", falhas);
        stats.put("taxaSucesso", String.format("%.2f%%", (float)sucessos/resultados.size()*100));
        
        responseMap.put("estatisticas", stats);
        
        // Resultados detalhados por cenário
        Map<String, Map<String, Object>> resultadosDetalhados = new HashMap<>();
        
        for (OpenRouterTestResult resultado : resultados) {
            Map<String, Object> detalheCenario = new HashMap<>();
            
            // Dados básicos
            detalheCenario.put("nome", resultado.getCenarioNome());
            detalheCenario.put("sucesso", resultado.isSucesso());
            detalheCenario.put("tempoExecucao", resultado.getTempoExecucao() + "ms");
            detalheCenario.put("observacao", resultado.getObservacao());
            
            // Dados de entrada e saída
            Map<String, Object> entrada = new HashMap<>();
            entrada.put("xpathOriginal", resultado.getXpathOriginal());
            entrada.put("dom", resultado.getDom());
            detalheCenario.put("entrada", entrada);
            
            Map<String, Object> saida = new HashMap<>();
            saida.put("xpathSugerido", resultado.getXpathSugerido());
            detalheCenario.put("saida", saida);
            
            // Payloads completos
            detalheCenario.put("payloadEnviado", resultado.getPayloadEnviado());
            detalheCenario.put("payloadRecebido", resultado.getPayloadRecebido());
            
            resultadosDetalhados.put(resultado.getCenarioNome(), detalheCenario);
        }
        
        responseMap.put("resultados", resultadosDetalhados);
        
        logger.info("Geração de relatório detalhado concluída");
        return ResponseEntity.ok(responseMap);
    }
    
    /**
     * Endpoint para executar um cenário específico de teste (1 a 5).
     * @param id ID do cenário (1 a 5)
     * @return Resultado detalhado do cenário específico
     */
    @GetMapping("/run/{id}")
    @Operation(summary = "Executar cenário específico", 
               description = "Executa apenas um cenário de teste específico pelo ID (1-5)")
    public ResponseEntity<Map<String, Object>> executarCenarioEspecifico(@PathVariable int id) {
        if (id < 1 || id > 5) {
            return ResponseEntity.badRequest().body(
                Map.of("erro", "ID de cenário inválido. Escolha um valor entre 1 e 5.")
            );
        }
        
        logger.info("Iniciando execução do cenário específico: {}", id);
        List<OpenRouterTestResult> todosResultados = openRouterTestService.testarCenarios();
        
        // Os cenários são indexados de 0 a 4 na lista, mas solicitados como 1 a 5 na API
        OpenRouterTestResult resultado = todosResultados.get(id - 1);
        
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("cenarioId", id);
        responseMap.put("nome", resultado.getCenarioNome());
        responseMap.put("sucesso", resultado.isSucesso());
        responseMap.put("tempoExecucao", resultado.getTempoExecucao() + "ms");
        responseMap.put("observacao", resultado.getObservacao());
        
        // Dados de entrada
        Map<String, Object> entrada = new HashMap<>();
        entrada.put("xpathOriginal", resultado.getXpathOriginal());
        entrada.put("dom", resultado.getDom());
        responseMap.put("entrada", entrada);
        
        // Dados de saída
        Map<String, Object> saida = new HashMap<>();
        saida.put("xpathSugerido", resultado.getXpathSugerido());
        responseMap.put("saida", saida);
        
        // Payloads completos
        responseMap.put("payloadEnviado", resultado.getPayloadEnviado());
        responseMap.put("payloadRecebido", resultado.getPayloadRecebido());
        
        logger.info("Execução do cenário {} finalizada", id);
        return ResponseEntity.ok(responseMap);
    }

    /**
     * Endpoint para testar diretamente a conexão com o OpenRouter.
     * Usa exatamente o mesmo formato do exemplo da documentação.
     * 
     * @return Resultado do teste de conexão
     */
    @GetMapping("/test-openrouter-connection")
    @Operation(summary = "Testar conexão com OpenRouter", 
               description = "Testa a conexão direta com a API do OpenRouter e retorna detalhes da comunicação")
    public ResponseEntity<Map<String, Object>> testarConexaoOpenRouter() {
        logger.info("Iniciando teste direto de conexão com OpenRouter");
        
        Map<String, Object> responseData = new HashMap<>();
        
        try {
            // Preparar headers exatamente como no exemplo
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + environment.getProperty("openrouter.api-key"));
            
            String siteUrl = environment.getProperty("openrouter.site-url");
            if (siteUrl != null && !siteUrl.isBlank()) {
                headers.set("HTTP-Referer", siteUrl);
            }
            
            String siteName = environment.getProperty("openrouter.site-name");
            if (siteName != null && !siteName.isBlank()) {
                headers.set("X-Title", siteName);
            }
            
            // Criar payload exatamente como no exemplo
            Map<String, Object> payload = new HashMap<>();
            payload.put("model", environment.getProperty("openrouter.model"));
            
            List<Map<String, String>> messages = new ArrayList<>();
            Map<String, String> message = new HashMap<>();
            message.put("role", "user");
            message.put("content", "What is the meaning of life?");
            messages.add(message);
            
            payload.put("messages", messages);
            
            // Detalhes da requisição para diagnóstico
            responseData.put("requestUrl", environment.getProperty("openrouter.url"));
            responseData.put("requestHeaders", headers.toString());
            responseData.put("requestBody", payload);
            
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);
            
            logger.info("Enviando requisição de teste para o OpenRouter...");
            
            // Usar RestTemplate diretamente
            RestTemplate restTemplate = new RestTemplate();
            
            try {
                ResponseEntity<Map> response = restTemplate.postForEntity(
                        environment.getProperty("openrouter.url"), 
                        requestEntity, 
                        Map.class);
                
                logger.info("Resposta recebida! Status: {}", response.getStatusCode());
                
                responseData.put("success", response.getStatusCode().is2xxSuccessful());
                responseData.put("status", response.getStatusCode().toString());
                responseData.put("response", response.getBody());
                
            } catch (Exception e) {
                logger.error("Falha ao testar conexão com OpenRouter", e);
                
                responseData.put("success", false);
                responseData.put("error", e.getMessage());
                
                // Para erros HTTP, obter mais detalhes
                if (e instanceof org.springframework.web.client.HttpClientErrorException) {
                    org.springframework.web.client.HttpClientErrorException httpEx = 
                        (org.springframework.web.client.HttpClientErrorException) e;
                    
                    responseData.put("errorStatus", httpEx.getStatusCode().toString());
                    responseData.put("errorBody", httpEx.getResponseBodyAsString());
                    
                    // Instruções para erro 401
                    if (httpEx.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                        responseData.put("solutionTips", Arrays.asList(
                            "Verifique se a chave API está correta",
                            "Confirme se a chave está ativa na sua conta OpenRouter",
                            "Teste o formato da chave (deve começar com 'sk-or-')",
                            "Verifique se você tem créditos suficientes na sua conta"
                        ));
                    }
                }
            }
            
        } catch (Exception e) {
            logger.error("Erro ao configurar teste de conexão", e);
            responseData.put("success", false);
            responseData.put("error", "Erro ao configurar teste: " + e.getMessage());
        }
        
        return ResponseEntity.ok(responseData);
    }
} 