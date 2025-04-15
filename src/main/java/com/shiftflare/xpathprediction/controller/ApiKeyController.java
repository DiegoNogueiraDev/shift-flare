package com.shiftflare.xpathprediction.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Controlador para atualização da chave de API do OpenRouter em tempo de execução.
 */
@RestController
@RequestMapping("/api/config")
@Tag(name = "Configuração", description = "Endpoints para gerenciar configurações em tempo de execução")
public class ApiKeyController {

    private static final Logger logger = LoggerFactory.getLogger(ApiKeyController.class);
    
    @Autowired
    private Environment environment;
    
    private String currentApiKey;
    
    @Autowired
    public ApiKeyController(@Value("${openrouter.api-key:}") String apiKey) {
        this.currentApiKey = apiKey;
    }
    
    /**
     * Endpoint para atualizar a chave da API do OpenRouter.
     * 
     * @param requestBody Mapa contendo a nova chave da API no formato {"apiKey": "valor-da-chave"}
     * @return Mensagem confirmando a atualização
     */
    @PostMapping("/update-api-key")
    @Operation(summary = "Atualizar chave de API", 
               description = "Atualiza a chave da API do OpenRouter em tempo de execução")
    public ResponseEntity<Map<String, Object>> updateApiKey(@RequestBody Map<String, String> requestBody) {
        if (!requestBody.containsKey("apiKey")) {
            return ResponseEntity.badRequest().body(
                Map.of(
                    "success", false,
                    "message", "A chave 'apiKey' é obrigatória no corpo da requisição"
                )
            );
        }
        
        String newApiKey = requestBody.get("apiKey");
        if (newApiKey == null || newApiKey.isBlank()) {
            return ResponseEntity.badRequest().body(
                Map.of(
                    "success", false,
                    "message", "A chave da API não pode ser vazia"
                )
            );
        }
        
        // Atualiza a chave da API em tempo de execução
        // Observação: isso não altera o arquivo application.properties, apenas a variável em memória
        System.setProperty("openrouter.api-key", newApiKey);
        currentApiKey = newApiKey;
        
        logger.info("Chave da API OpenRouter atualizada com sucesso");
        
        // Retornar apenas confirmação, nunca a chave
        return ResponseEntity.ok(
            Map.of(
                "success", true,
                "message", "Chave da API atualizada com sucesso"
            )
        );
    }
    
    /**
     * Verifica se a chave da API está configurada (sem revelar o valor).
     * 
     * @return Status da configuração da chave
     */
    @PostMapping("/check-api-key")
    @Operation(summary = "Verificar chave de API", 
               description = "Verifica se a chave da API do OpenRouter está configurada")
    public ResponseEntity<Map<String, Object>> checkApiKey() {
        boolean hasKey = currentApiKey != null && !currentApiKey.isEmpty();
        String maskedKey = hasKey ? 
                "sk-..." + currentApiKey.substring(Math.max(0, currentApiKey.length() - 4)) : 
                "Não configurada";
        
        return ResponseEntity.ok(
            Map.of(
                "configured", hasKey,
                "maskedKey", maskedKey
            )
        );
    }
} 