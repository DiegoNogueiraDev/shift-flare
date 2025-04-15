import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Cliente Java de exemplo para testar a API de Predição de XPath.
 * 
 * Execute este programa após iniciar o servidor para testar a integração.
 */
public class XPathTestClient {

    private static final String API_URL = "http://localhost:8080/api/v1/xpath/predict";
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static void main(String[] args) {
        try {
            // Exemplo 1: XPath simples
            testXPathPrediction(
                "//div[@id=\"produto-antigo\"]",
                "<html><body><div class=\"container\"><div id=\"produto-novo\">iPhone 13</div></div></body></html>"
            );
            
            // Exemplo 2: XPath complexo
            testXPathPrediction(
                "//table[@class=\"tabela-produtos\"]/tr[2]/td[3]",
                "<html><body><div class=\"produtos-grid\"><div class=\"produto-item\"><span class=\"nome\">Mouse</span>" +
                "<span class=\"preco\">R$ 50,00</span></div></div></body></html>"
            );
            
            // Exemplo 3: Caso real de uma página de e-commerce
            testXPathPrediction(
                "//div[@class=\"old-price-container\"]/span[@class=\"price\"]",
                "<html><body><div class=\"product-details\">" +
                "<div class=\"new-price-wrapper\"><p class=\"current-price\">R$ 1.299,00</p>" +
                "<p class=\"discount\">10% OFF</p></div></div></body></html>"
            );
            
        } catch (Exception e) {
            System.err.println("Erro ao testar a API: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Testa a predição de XPath enviando uma requisição para a API.
     * 
     * @param errorXpath XPath que não está funcionando
     * @param pageDOM DOM atual da página
     */
    private static void testXPathPrediction(String errorXpath, String pageDOM) throws Exception {
        System.out.println("\n========== TESTE DE PREDIÇÃO DE XPATH ==========");
        System.out.println("XPath com erro: " + errorXpath);
        System.out.println("DOM resumido: " + pageDOM.substring(0, Math.min(pageDOM.length(), 50)) + "...");
        
        // Construir o payload JSON
        String jsonPayload = String.format(
            "{\"errorXpath\": \"%s\", \"pageDOM\": \"%s\"}",
            errorXpath.replace("\"", "\\\""),
            pageDOM.replace("\"", "\\\"")
        );
        
        // Construir a requisição HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();
        
        // Enviar a requisição e obter a resposta
        System.out.println("Enviando requisição para " + API_URL);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        // Processar e exibir a resposta
        System.out.println("Código de status: " + response.statusCode());
        System.out.println("Resposta: " + response.body());
        
        // Extrair o XPath sugerido (implementação simples)
        if (response.statusCode() == 200 && response.body().contains("newXpath")) {
            String newXpath = response.body().split("\"newXpath\":")[1]
                .split("\"")[1];
            System.out.println("NOVO XPATH SUGERIDO: " + newXpath);
            System.out.println("========== TESTE CONCLUÍDO COM SUCESSO ==========");
        } else {
            System.out.println("ERRO NA PREDIÇÃO. Verifique a resposta acima.");
            System.out.println("========== TESTE CONCLUÍDO COM FALHA ==========");
        }
    }
} 