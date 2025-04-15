package com.example.xpathprediction.controller;

import com.example.xpathprediction.domain.OpenRouterTestResult;
import com.example.xpathprediction.service.OpenRouterTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
} 