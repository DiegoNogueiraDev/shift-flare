package com.example.xpathprediction.domain;

import javax.validation.constraints.NotBlank;

/**
 * Representa a requisição para predição de XPath.
 */
public class XpathPredictionRequest {

    @NotBlank(message = "O xpath com erro deve ser informado.")
    private String errorXpath;

    @NotBlank(message = "O body/DOM da página deve ser informado.")
    private String pageDOM;

    // Construtores, getters e setters

    public XpathPredictionRequest() {
    }

    public XpathPredictionRequest(String errorXpath, String pageDOM) {
        this.errorXpath = errorXpath;
        this.pageDOM = pageDOM;
    }

    public String getErrorXpath() {
        return errorXpath;
    }

    public void setErrorXpath(String errorXpath) {
        this.errorXpath = errorXpath;
    }

    public String getPageDOM() {
        return pageDOM;
    }

    public void setPageDOM(String pageDOM) {
        this.pageDOM = pageDOM;
    }
} 