package com.example.xpathprediction.integration;

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
} 