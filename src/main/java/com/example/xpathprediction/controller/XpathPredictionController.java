package com.example.xpathprediction.controller;

import com.example.xpathprediction.domain.XpathPredictionRequest;
import com.example.xpathprediction.domain.XpathPredictionResponse;
import com.example.xpathprediction.service.XpathPredictionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * Controller para o endpoint de predição de XPath.
 */
@RestController
@RequestMapping("/api/v1/xpath")
@Validated
@Tag(name = "Xpath Prediction", description = "Endpoints para predição de novos XPaths")
public class XpathPredictionController {

    private final XpathPredictionService predictionService;

    public XpathPredictionController(XpathPredictionService predictionService) {
        this.predictionService = predictionService;
    }

    /**
     * Endpoint para obter a predição do XPath.
     * 
     * @param request Objeto com o XPath que falhou e o DOM da página.
     * @return Novo XPath sugerido encapsulado em JSON.
     */
    @PostMapping("/predict")
    @Operation(summary = "Prediz novo XPath", description = "Recebe informações do erro e retorna um novo XPath sugerido")
    public ResponseEntity<XpathPredictionResponse> predictXpath(@Valid @RequestBody XpathPredictionRequest request) {
        XpathPredictionResponse response = predictionService.predictNewXpath(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
} 