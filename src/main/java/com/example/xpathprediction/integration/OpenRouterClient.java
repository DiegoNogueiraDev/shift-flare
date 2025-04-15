package com.example.xpathprediction.integration;

import java.util.Map;

/**
 * Interface para integração com o serviço OpenRouter.
 */
public interface OpenRouterClient {

    /**
     * Envia dados para o OpenRouter e retorna o novo XPath sugerido.
     *
     * @param errorXpath XPath que apresentou erro.
     * @param pageDOM    Body ou DOM da página.
     * @return Novo XPath sugerido.
     */
    String getSuggestedXpath(String errorXpath, String pageDOM);
    
    /**
     * Envia dados para o OpenRouter e retorna o novo XPath sugerido, juntamente com os payloads.
     *
     * @param errorXpath XPath que apresentou erro.
     * @param pageDOM    Body ou DOM da página.
     * @return Mapa contendo o XPath sugerido e os payloads de requisição e resposta.
     */
    Map<String, Object> getSuggestedXpathWithPayloads(String errorXpath, String pageDOM);
} 