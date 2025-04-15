package com.example.xpathprediction.service;

import com.example.xpathprediction.domain.OpenRouterTestResult;
import com.example.xpathprediction.integration.OpenRouterClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Serviço para testar diferentes cenários de resposta do OpenRouter.
 */
@Service
public class OpenRouterTestService {

    private static final Logger logger = LoggerFactory.getLogger(OpenRouterTestService.class);
    private final OpenRouterClient openRouterClient;

    public OpenRouterTestService(OpenRouterClient openRouterClient) {
        this.openRouterClient = openRouterClient;
    }

    /**
     * Executa testes com diferentes cenários para avaliar comportamento do OpenRouter.
     * @return Lista com os resultados dos testes de cada cenário
     */
    public List<OpenRouterTestResult> testarCenarios() {
        List<OpenRouterTestResult> resultados = new ArrayList<>();
        
        // Cenário 1: XPath com ID que não existe mais
        logger.info("Iniciando teste do Cenário 1: XPath com ID inexistente");
        String xpathIdInexistente = "//div[@id='old-id']";
        String domIdNovo = "<html><body><div id='new-id'>Conteúdo</div></body></html>";
        OpenRouterTestResult resultado1 = executarCenario(
            "ID_Inexistente", 
            xpathIdInexistente, 
            domIdNovo,
            "//div[@id='new-id']"
        );
        resultados.add(resultado1);
        
        // Cenário 2: XPath com classe que mudou
        logger.info("Iniciando teste do Cenário 2: XPath com classe alterada");
        String xpathClasseAntiga = "//div[@class='old-class']";
        String domClasseNova = "<html><body><div class='new-class'>Conteúdo</div></body></html>";
        OpenRouterTestResult resultado2 = executarCenario(
            "Classe_Alterada", 
            xpathClasseAntiga, 
            domClasseNova,
            "//div[@class='new-class']"
        );
        resultados.add(resultado2);
        
        // Cenário 3: XPath com estrutura que mudou (elemento movido na hierarquia)
        logger.info("Iniciando teste do Cenário 3: Estrutura alterada");
        String xpathEstrutura = "//div[@id='container']/span[@class='item']";
        String domEstruturaAlterada = "<html><body><div id='container'><div><span class='item'>Conteúdo</span></div></div></body></html>";
        OpenRouterTestResult resultado3 = executarCenario(
            "Estrutura_Alterada", 
            xpathEstrutura, 
            domEstruturaAlterada,
            "//div[@id='container']/div/span[@class='item']"
        );
        resultados.add(resultado3);
        
        // Cenário 4: XPath com texto que mudou
        logger.info("Iniciando teste do Cenário 4: Texto alterado");
        String xpathTexto = "//button[text()='Enviar']";
        String domTextoAlterado = "<html><body><button>Submeter</button></body></html>";
        OpenRouterTestResult resultado4 = executarCenario(
            "Texto_Alterado", 
            xpathTexto, 
            domTextoAlterado,
            "//button[text()='Submeter']"
        );
        resultados.add(resultado4);
        
        // Cenário 5: DOM complexo
        logger.info("Iniciando teste do Cenário 5: DOM complexo");
        String xpathComplexo = "//div[@id='main']/div[2]/ul/li[3]/a";
        String domComplexo = "<html><body><div id='main-content'><div><ul><li><a href='#'>Link 1</a></li>" +
                             "<li><a href='#'>Link 2</a></li><li><a href='#'>Link 3</a></li></ul></div></div></body></html>";
        OpenRouterTestResult resultado5 = executarCenario(
            "DOM_Complexo", 
            xpathComplexo, 
            domComplexo,
            "//div[@id='main-content']/div/ul/li[3]/a"
        );
        resultados.add(resultado5);
        
        logger.info("Testes concluídos. {} cenários testados", resultados.size());
        return resultados;
    }
    
    /**
     * Executa um cenário específico e avalia o resultado.
     * 
     * @param cenarioNome Nome do cenário
     * @param xpathOriginal XPath original com erro
     * @param dom DOM atual da página
     * @param xpathEsperado XPath esperado que seria ideal para o elemento (opcional)
     * @return Objeto contendo os detalhes do teste
     */
    private OpenRouterTestResult executarCenario(String cenarioNome, String xpathOriginal, 
                                               String dom, String xpathEsperado) {
        OpenRouterTestResult resultado = new OpenRouterTestResult(cenarioNome, xpathOriginal, dom);
        
        try {
            long inicio = System.currentTimeMillis();
            String xpathSugerido = openRouterClient.getSuggestedXpath(xpathOriginal, dom);
            long fim = System.currentTimeMillis();
            
            resultado.setXpathSugerido(xpathSugerido);
            resultado.setTempoExecucao(fim - inicio);
            
            // Verifica se o xpath sugerido corresponde ao esperado
            if (xpathEsperado != null) {
                boolean correspondeExatamente = xpathEsperado.equals(xpathSugerido);
                boolean correspondeSemantico = avaliarCorrespondenciaSemantica(xpathSugerido, xpathEsperado);
                
                resultado.setSucesso(correspondeExatamente || correspondeSemantico);
                
                if (correspondeExatamente) {
                    resultado.setObservacao("XPath corresponde exatamente ao esperado");
                } else if (correspondeSemantico) {
                    resultado.setObservacao("XPath corresponde semanticamente ao esperado");
                } else {
                    resultado.setObservacao("XPath não corresponde ao esperado: " + xpathEsperado);
                }
            } else {
                // Se não temos um xpath esperado, consideramos sucesso se recebemos qualquer resposta não vazia
                resultado.setSucesso(xpathSugerido != null && !xpathSugerido.isEmpty());
                resultado.setObservacao("Avaliação sem XPath esperado definido");
            }
            
            logger.info("Cenário {} concluído: {}", cenarioNome, resultado.isSucesso() ? "SUCESSO" : "FALHA");
            
        } catch (Exception e) {
            resultado.setSucesso(false);
            resultado.setObservacao("Erro ao executar teste: " + e.getMessage());
            logger.error("Erro ao executar cenário {}", cenarioNome, e);
        }
        
        return resultado;
    }
    
    /**
     * Avalia se dois XPaths são semanticamente equivalentes, mesmo que não sejam exatamente iguais.
     * Esta é uma análise simplificada; uma implementação real seria mais sofisticada.
     * 
     * @param xpathSugerido XPath sugerido pelo OpenRouter
     * @param xpathEsperado XPath esperado para o teste
     * @return true se forem semanticamente equivalentes
     */
    private boolean avaliarCorrespondenciaSemantica(String xpathSugerido, String xpathEsperado) {
        if (xpathSugerido == null || xpathEsperado == null) {
            return false;
        }
        
        // Remove espaços em branco e formatação para comparação mais flexível
        String sugerido = xpathSugerido.replaceAll("\\s+", "");
        String esperado = xpathEsperado.replaceAll("\\s+", "");
        
        // Verifica se são exatamente iguais após normalização
        if (sugerido.equals(esperado)) {
            return true;
        }
        
        // Verifica se a diferença está apenas em aspas simples vs. duplas
        String sugeridoNormalizado = sugerido.replace("'", "\"");
        String esperadoNormalizado = esperado.replace("'", "\"");
        if (sugeridoNormalizado.equals(esperadoNormalizado)) {
            return true;
        }
        
        // Verifica se o XPath tem os mesmos elementos críticos (IDs, classes, etc.)
        // Esta é uma verificação básica que pode ser expandida
        boolean temMesmoId = verificarPatternEmAmbos(sugerido, esperado, "@id=['\"][^'\"]+['\"]");
        boolean temMesmaClasse = verificarPatternEmAmbos(sugerido, esperado, "@class=['\"][^'\"]+['\"]");
        boolean temMesmoTexto = verificarPatternEmAmbos(sugerido, esperado, "text\\(\\)=['\"][^'\"]+['\"]");
        
        // Se ambos têm o mesmo ID, classe ou texto, consideramos semelhantes o suficiente
        return temMesmoId || temMesmaClasse || temMesmoTexto;
    }
    
    /**
     * Verifica se um padrão existe em ambas as strings
     */
    private boolean verificarPatternEmAmbos(String xpath1, String xpath2, String patternStr) {
        Pattern pattern = Pattern.compile(patternStr);
        boolean existeEm1 = pattern.matcher(xpath1).find();
        boolean existeEm2 = pattern.matcher(xpath2).find();
        
        // Se existe em ambos ou não existe em ambos, retorna true
        return (existeEm1 && existeEm2) || (!existeEm1 && !existeEm2);
    }
} 