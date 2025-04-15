package com.shiftflare.xpathprediction.exception;

/**
 * Exceção customizada para erros na predição do XPath.
 */
public class XpathPredictionException extends RuntimeException {

    public XpathPredictionException(String message) {
        super(message);
    }

    public XpathPredictionException(String message, Throwable cause) {
        super(message, cause);
    }
} 