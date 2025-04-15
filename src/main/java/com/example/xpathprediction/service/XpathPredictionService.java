package com.example.xpathprediction.service;

import com.example.xpathprediction.domain.XpathPredictionRequest;
import com.example.xpathprediction.domain.XpathPredictionResponse;
import com.example.xpathprediction.exception.XpathPredictionException;
import com.example.xpathprediction.integration.OpenRouterClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por processar a lógica de predição de XPath.
 */
@Service
public class XpathPredictionService {

    private static final Logger logger = LoggerFactory.getLogger(XpathPredictionService.class);

    private final OpenRouterClient openRouterClient;

    public XpathPredictionService(OpenRouterClient openRouterClient) {
        this.openRouterClient = openRouterClient;
    }

    /**
     * Processa a predição de um novo XPath com base no erro informado e no DOM.
     *
     * @param request Objeto contendo o XPath com erro e o DOM da página.
     * @return Resposta contendo o novo XPath sugerido.
     */
    public XpathPredictionResponse predictNewXpath(XpathPredictionRequest request) {
        try {
            logger.info("Processando predição para o xpath: {}", request.getErrorXpath());
            String suggestedXpath = openRouterClient.getSuggestedXpath(request.getErrorXpath(), request.getPageDOM());
            return new XpathPredictionResponse(suggestedXpath);
        } catch (Exception ex) {
            logger.error("Erro ao processar a predição de XPath", ex);
            throw new XpathPredictionException("Erro ao prever novo XPath", ex);
        }
    }
} 