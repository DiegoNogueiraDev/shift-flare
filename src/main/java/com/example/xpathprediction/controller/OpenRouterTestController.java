package com.example.xpathprediction.controller;

import com.example.xpathprediction.domain.OpenRouterTestResult;
import com.example.xpathprediction.service.OpenRouterTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controlador para executar os testes do OpenRouter.
 */
@RestController
@RequestMapping("/api/test")
public class OpenRouterTestController {

    private static final Logger logger = LoggerFactory.getLogger(OpenRouterTestController.class);
    private final OpenRouterTestService openRouterTestService;

    public OpenRouterTestController(OpenRouterTestService openRouterTestService) {
        this.openRouterTestService = openRouterTestService;
    }

    /**
     * Endpoint para executar todos os cenários de teste.
     * @return Resultados dos testes executados
     */
    @GetMapping("/run-all")
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
} 