package com.shiftflare.xpathprediction.controller;

import com.shiftflare.xpathprediction.integration.AiPromptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador para processamento de requisições de IA para as diferentes funcionalidades.
 */
@RestController
@RequestMapping("/api/ai")
@Tag(name = "Inteligência Artificial", description = "Endpoints para processamento de IA")
public class AiController {

    private static final Logger logger = LoggerFactory.getLogger(AiController.class);
    private final AiPromptService aiPromptService;

    public AiController(AiPromptService aiPromptService) {
        this.aiPromptService = aiPromptService;
    }

    /**
     * Endpoint para análise de código (Code Review).
     *
     * @param requestBody Mapa contendo o código e a linguagem para análise
     * @return Resultado da análise de código
     */
    @PostMapping("/code-review")
    @Operation(summary = "Analisar código", 
               description = "Analisa o código enviado e retorna revisão detalhada")
    public ResponseEntity<Map<String, Object>> codeReview(@RequestBody Map<String, String> requestBody) {
        String code = requestBody.get("code");
        String language = requestBody.getOrDefault("language", "auto");
        
        logger.info("Recebida solicitação de análise de código em {}", language);
        logger.debug("Tamanho do código recebido: {} caracteres", code != null ? code.length() : 0);
        
        if (code == null || code.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Map<String, Object> result = aiPromptService.codeReview(code, language);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint para geração de script de automação.
     *
     * @param requestBody Mapa contendo a descrição e o framework para automação
     * @return Resultado da geração de automação
     */
    @PostMapping("/automation")
    @Operation(summary = "Gerar automação", 
               description = "Gera script de automação baseado na descrição enviada")
    public ResponseEntity<Map<String, Object>> generateAutomation(@RequestBody Map<String, String> requestBody) {
        String description = requestBody.get("description");
        String framework = requestBody.getOrDefault("framework", "selenium");
        
        logger.info("Recebida solicitação de geração de automação usando {}", framework);
        logger.debug("Tamanho da descrição recebida: {} caracteres", description != null ? description.length() : 0);
        
        if (description == null || description.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Map<String, Object> result = aiPromptService.generateAutomation(description, framework);
        return ResponseEntity.ok(result);
    }

    /**
     * Endpoint para migração de código Q2 para Q3.
     *
     * @param requestBody Mapa contendo o código e o tipo de componente para migração
     * @return Resultado da migração de código
     */
    @PostMapping("/migration")
    @Operation(summary = "Migrar código Q2→Q3", 
               description = "Migra código Q2 para a estrutura Q3")
    public ResponseEntity<Map<String, Object>> migrateCode(@RequestBody Map<String, String> requestBody) {
        String code = requestBody.get("code");
        String componentType = requestBody.getOrDefault("componentType", "autodetect");
        
        logger.info("Recebida solicitação de migração de código para tipo {}", componentType);
        logger.debug("Tamanho do código recebido: {} caracteres", code != null ? code.length() : 0);
        
        if (code == null || code.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Map<String, Object> result = aiPromptService.migrateCode(code, componentType);
        return ResponseEntity.ok(result);
    }
} 