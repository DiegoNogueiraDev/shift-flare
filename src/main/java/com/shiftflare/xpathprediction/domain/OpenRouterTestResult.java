package com.shiftflare.xpathprediction.domain;

import java.time.LocalDateTime;

/**
 * Classe para armazenar detalhes de cada teste realizado.
 */
public class OpenRouterTestResult {

    private String cenarioNome;
    private String xpathOriginal;
    private String dom;
    private String xpathSugerido;
    private boolean sucesso;
    private long tempoExecucao; // em milissegundos
    private LocalDateTime dataExecucao;
    private String observacao;
    private String payloadEnviado;
    private String payloadRecebido;

    public OpenRouterTestResult() {
        this.dataExecucao = LocalDateTime.now();
    }

    public OpenRouterTestResult(String cenarioNome, String xpathOriginal, String dom) {
        this.cenarioNome = cenarioNome;
        this.xpathOriginal = xpathOriginal;
        this.dom = dom;
        this.dataExecucao = LocalDateTime.now();
    }

    public String getCenarioNome() {
        return cenarioNome;
    }

    public void setCenarioNome(String cenarioNome) {
        this.cenarioNome = cenarioNome;
    }

    public String getXpathOriginal() {
        return xpathOriginal;
    }

    public void setXpathOriginal(String xpathOriginal) {
        this.xpathOriginal = xpathOriginal;
    }

    public String getDom() {
        return dom;
    }

    public void setDom(String dom) {
        this.dom = dom;
    }

    public String getXpathSugerido() {
        return xpathSugerido;
    }

    public void setXpathSugerido(String xpathSugerido) {
        this.xpathSugerido = xpathSugerido;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }

    public long getTempoExecucao() {
        return tempoExecucao;
    }

    public void setTempoExecucao(long tempoExecucao) {
        this.tempoExecucao = tempoExecucao;
    }

    public LocalDateTime getDataExecucao() {
        return dataExecucao;
    }

    public void setDataExecucao(LocalDateTime dataExecucao) {
        this.dataExecucao = dataExecucao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getPayloadEnviado() {
        return payloadEnviado;
    }

    public void setPayloadEnviado(String payloadEnviado) {
        this.payloadEnviado = payloadEnviado;
    }

    public String getPayloadRecebido() {
        return payloadRecebido;
    }

    public void setPayloadRecebido(String payloadRecebido) {
        this.payloadRecebido = payloadRecebido;
    }

    @Override
    public String toString() {
        return "OpenRouterTestResult{" +
                "cenarioNome='" + cenarioNome + '\'' +
                ", xpathOriginal='" + xpathOriginal + '\'' +
                ", xpathSugerido='" + xpathSugerido + '\'' +
                ", sucesso=" + sucesso +
                ", tempoExecucao=" + tempoExecucao + "ms" +
                ", dataExecucao=" + dataExecucao +
                '}';
    }
} 