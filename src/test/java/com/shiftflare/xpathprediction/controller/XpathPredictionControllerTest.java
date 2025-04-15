package com.shiftflare.xpathprediction.controller;

import com.shiftflare.xpathprediction.domain.XpathPredictionRequest;
import com.shiftflare.xpathprediction.domain.XpathPredictionResponse;
import com.shiftflare.xpathprediction.exception.XpathPredictionException;
import com.shiftflare.xpathprediction.service.XpathPredictionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is; // Para jsonPath

// Testa apenas a camada web (Controller), mockando o Service
@WebMvcTest(XpathPredictionController.class) 
class XpathPredictionControllerTest {

    @Autowired
    private MockMvc mockMvc; // Para simular requisições HTTP

    @MockBean // Cria um mock do serviço e o adiciona ao contexto do Spring
    private XpathPredictionService predictionService;

    @Autowired
    private ObjectMapper objectMapper; // Para converter objetos Java em JSON

    private XpathPredictionRequest validRequest;
    private XpathPredictionRequest invalidRequest_MissingXpath;
    private XpathPredictionRequest invalidRequest_MissingDOM;
    private final String newXpath = "//div[@id='novo']";

    @BeforeEach
    void setUp() {
        validRequest = new XpathPredictionRequest("//div[@id='erro']", "<html>...</html>");
        invalidRequest_MissingXpath = new XpathPredictionRequest(null, "<html>...</html>");
        invalidRequest_MissingDOM = new XpathPredictionRequest("//div[@id='erro']", ""); // DOM vazio
    }

    @Test
    @DisplayName("Deve retornar status 200 OK e novo XPath quando a requisição for válida")
    void testPredictXpath_Success() throws Exception {
        // Arrange: Configura o mock do serviço para retornar uma resposta de sucesso
        XpathPredictionResponse serviceResponse = new XpathPredictionResponse(newXpath);
        when(predictionService.predictNewXpath(any(XpathPredictionRequest.class))).thenReturn(serviceResponse);

        // Act: Executa a requisição POST para /api/v1/xpath/predict
        ResultActions result = mockMvc.perform(post("/api/v1/xpath/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)));

        // Assert: Verifica se o status é 200 OK e o corpo JSON contém o novo XPath
        result.andExpect(status().isOk())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.newXpath", is(newXpath)));

        // Verifica se o método do serviço foi chamado uma vez com qualquer objeto XpathPredictionRequest
        verify(predictionService, times(1)).predictNewXpath(any(XpathPredictionRequest.class));
    }

    @Test
    @DisplayName("Deve retornar status 500 Internal Server Error quando o serviço lançar XpathPredictionException")
    void testPredictXpath_ServiceThrowsException() throws Exception {
        // Arrange: Configura o mock do serviço para lançar uma exceção
        String errorMessage = "Erro simulado no serviço";
        when(predictionService.predictNewXpath(any(XpathPredictionRequest.class)))
              .thenThrow(new XpathPredictionException(errorMessage));

        // Act: Executa a requisição POST
        ResultActions result = mockMvc.perform(post("/api/v1/xpath/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)));

        // Assert: Verifica se o status é 500 e o corpo JSON contém a mensagem de erro (conforme GlobalExceptionHandler)
        result.andExpect(status().isInternalServerError())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Espera JSON por causa do ErrorResponse
              .andExpect(jsonPath("$.errorType", is("Erro de Predição")))
              .andExpect(jsonPath("$.message", is(errorMessage)));

        // Verifica se o método do serviço foi chamado
        verify(predictionService, times(1)).predictNewXpath(any(XpathPredictionRequest.class));
    }

    @Test
    @DisplayName("Deve retornar status 400 Bad Request quando o errorXpath estiver faltando")
    void testPredictXpath_BadRequest_MissingXpath() throws Exception {
        // Act: Executa a requisição POST com dados inválidos
        ResultActions result = mockMvc.perform(post("/api/v1/xpath/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest_MissingXpath)));

        // Assert: Verifica se o status é 400 e a mensagem de erro de validação está presente
        result.andExpect(status().isBadRequest())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON)) 
              .andExpect(jsonPath("$.errorType", is("Erro de Validação")))
              .andExpect(jsonPath("$.message", is("errorXpath: O xpath com erro deve ser informado.")));

        // Verifica que o serviço NÃO foi chamado
        verify(predictionService, never()).predictNewXpath(any());
    }

     @Test
    @DisplayName("Deve retornar status 400 Bad Request quando o pageDOM estiver faltando")
    void testPredictXpath_BadRequest_MissingDOM() throws Exception {
        // Act: Executa a requisição POST com dados inválidos
        ResultActions result = mockMvc.perform(post("/api/v1/xpath/predict")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest_MissingDOM)));

        // Assert: Verifica se o status é 400 e a mensagem de erro de validação está presente
         result.andExpect(status().isBadRequest())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON)) 
              .andExpect(jsonPath("$.errorType", is("Erro de Validação")))
              .andExpect(jsonPath("$.message", is("pageDOM: O body/DOM da página deve ser informado.")));

        // Verifica que o serviço NÃO foi chamado
        verify(predictionService, never()).predictNewXpath(any());
    }
} 