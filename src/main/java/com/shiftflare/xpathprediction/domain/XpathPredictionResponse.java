package com.shiftflare.xpathprediction.domain;

/**
 * Representa a resposta com o novo XPath sugerido.
 */
public class XpathPredictionResponse {

    private String newXpath;

    // Construtores, getters e setters

    public XpathPredictionResponse() {
    }

    public XpathPredictionResponse(String newXpath) {
        this.newXpath = newXpath;
    }

    public String getNewXpath() {
        return newXpath;
    }

    public void setNewXpath(String newXpath) {
        this.newXpath = newXpath;
    }
} 