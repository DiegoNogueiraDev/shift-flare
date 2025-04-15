package com.example.xpathprediction.exception;

import javax.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

/**
 * Manipulador global de exceções para padronizar respostas de erro.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(XpathPredictionException.class)
    public ResponseEntity<ErrorResponse> handleXpathPredictionException(XpathPredictionException ex) {
        logger.error("Erro de predição de XPath: {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse("Erro de Predição", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                         .map(error -> error.getField() + ": " + error.getDefaultMessage())
                         .collect(Collectors.joining(", "));
        logger.warn("Erro de validação: {}", errors);
        ErrorResponse errorResponse = new ErrorResponse("Erro de Validação", errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        String errors = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        logger.warn("Erro de validação de constraints: {}", errors);
        ErrorResponse errorResponse = new ErrorResponse("Erro de Validação", errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        logger.error("Erro inesperado na aplicação", ex);
        ErrorResponse errorResponse = new ErrorResponse("Erro Interno", "Ocorreu um erro inesperado no servidor.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Classe interna para padronizar a resposta de erro
    private static class ErrorResponse {
        private final String errorType;
        private final String message;

        public ErrorResponse(String errorType, String message) {
            this.errorType = errorType;
            this.message = message;
        }

        public String getErrorType() {
            return errorType;
        }

        public String getMessage() {
            return message;
        }
    }
} 