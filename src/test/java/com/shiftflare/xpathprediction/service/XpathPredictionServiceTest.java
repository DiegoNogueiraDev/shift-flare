package com.shiftflare.xpathprediction.service;

import com.shiftflare.xpathprediction.domain.XpathPredictionRequest;
import com.shiftflare.xpathprediction.domain.XpathPredictionResponse;
import com.shiftflare.xpathprediction.exception.XpathPredictionException;
import com.shiftflare.xpathprediction.integration.OpenRouterClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Habilita injeção de mocks do Mockito com JUnit 5
class XpathPredictionServiceTest {

    @Mock // Cria um mock para OpenRouterClient
    private OpenRouterClient openRouterClient;

    @InjectMocks // Cria uma instância de XpathPredictionService e injeta os mocks (@Mock)
    private XpathPredictionService predictionService;

    private XpathPredictionRequest validRequest;
    private final String errorXpath = "//div[@id='erro']";
    private final String pageDOM = "<html><body><div id='novo'></div></body></html>";
    private final String newXpath = "//div[@id='novo']";

    @BeforeEach
    void setUp() {
        validRequest = new XpathPredictionRequest(errorXpath, pageDOM);
        // Não precisa mais instanciar predictionService manualmente, @InjectMocks faz isso
    }

    @Test
    @DisplayName("Deve prever novo XPath com sucesso quando o cliente retornar uma sugestão válida")
    void testPredictNewXpath_Success() {
        // Arrange: Configura o mock para retornar o novo XPath esperado
        when(openRouterClient.getSuggestedXpath(errorXpath, pageDOM)).thenReturn(newXpath);

        // Act: Chama o método do serviço
        XpathPredictionResponse response = predictionService.predictNewXpath(validRequest);

        // Assert: Verifica se a resposta não é nula e contém o XPath esperado
        assertNotNull(response);
        assertEquals(newXpath, response.getNewXpath());
        // Verifica se o método do cliente foi chamado exatamente uma vez com os parâmetros corretos
        verify(openRouterClient, times(1)).getSuggestedXpath(errorXpath, pageDOM);
    }

    @Test
    @DisplayName("Deve lançar XpathPredictionException quando o cliente lançar uma exceção")
    void testPredictNewXpath_Failure_ClientThrowsException() {
        // Arrange: Configura o mock para lançar uma RuntimeException simulando um erro na API
        when(openRouterClient.getSuggestedXpath(errorXpath, pageDOM)).thenThrow(new RuntimeException("Erro na API externa"));

        // Act & Assert: Verifica se XpathPredictionException é lançada ao chamar o serviço
        XpathPredictionException exception = assertThrows(XpathPredictionException.class, () -> {
            predictionService.predictNewXpath(validRequest);
        });

        // Assert: Verifica a mensagem da exceção e a causa raiz
        assertEquals("Erro ao prever novo XPath", exception.getMessage());
        assertTrue(exception.getCause() instanceof RuntimeException);
        assertEquals("Erro na API externa", exception.getCause().getMessage());
        // Verifica se o método do cliente foi chamado exatamente uma vez
        verify(openRouterClient, times(1)).getSuggestedXpath(errorXpath, pageDOM);
    }

    @Test
    @DisplayName("Deve criar resposta correta quando o cliente retornar null")
    void testPredictNewXpath_Failure_ClientReturnsNull() {
        // Arrange: Configura o mock para retornar null
        when(openRouterClient.getSuggestedXpath(errorXpath, pageDOM)).thenReturn(null);

        // Act: Chama o método do serviço - não vai lançar exceção, pois o service não verifica null
        XpathPredictionResponse response = predictionService.predictNewXpath(validRequest);

        // Assert: Verifica que a resposta foi criada, porém com newXpath nulo
        assertNotNull(response);
        assertNull(response.getNewXpath());
        
        // Verifica se o método do cliente foi chamado
        verify(openRouterClient, times(1)).getSuggestedXpath(errorXpath, pageDOM);
    }
} 