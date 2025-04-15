import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

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
            // Verificar se o servidor está rodando
            try {
                HttpResponse<String> healthResponse = client.send(
                    HttpRequest.newBuilder()
                        .uri(URI.create("http://localhost:8080/actuator/health"))
                        .GET()
                        .build(),
                    HttpResponse.BodyHandlers.ofString()
                );
                
                if (healthResponse.statusCode() != 200) {
                    System.err.println("ERRO: O servidor não está rodando corretamente em http://localhost:8080");
                    System.err.println("Inicie o servidor antes de executar este cliente.");
                    System.exit(1);
                }
                
                System.out.println("Servidor está rodando! Status: " + healthResponse.statusCode());
            } catch (Exception e) {
                System.err.println("ERRO: Não foi possível conectar ao servidor em http://localhost:8080");
                System.err.println("Mensagem: " + e.getMessage());
                System.err.println("Inicie o servidor antes de executar este cliente.");
                System.exit(1);
            }
            
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
        
        // Construir o payload JSON usando HashMap para evitar problemas de escape
        Map<String, String> payload = new HashMap<>();
        payload.put("errorXpath", errorXpath);
        payload.put("pageDOM", pageDOM);
        
        // Converter HashMap para JSON usando método simples
        String jsonPayload = toJson(payload);
        
        System.out.println("\nPayload JSON: " + jsonPayload);
        
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
    
    /**
     * Método simples para converter um Map para JSON
     */
    private static String toJson(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!first) {
                sb.append(",");
            }
            first = false;
            sb.append("\"").append(entry.getKey()).append("\":");
            sb.append("\"").append(escapeJson(entry.getValue())).append("\"");
        }
        sb.append("}");
        return sb.toString();
    }
    
    /**
     * Método para escapar caracteres especiais em uma string JSON
     */
    private static String escapeJson(String input) {
        if (input == null) {
            return "";
        }
        
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            switch (ch) {
                case '\"':
                    result.append("\\\"");
                    break;
                case '\\':
                    result.append("\\\\");
                    break;
                case '\b':
                    result.append("\\b");
                    break;
                case '\f':
                    result.append("\\f");
                    break;
                case '\n':
                    result.append("\\n");
                    break;
                case '\r':
                    result.append("\\r");
                    break;
                case '\t':
                    result.append("\\t");
                    break;
                default:
                    // Tratar caracteres Unicode que precisam ser escapados
                    if (ch < ' ' || (ch >= '\u0080' && ch < '\u00a0') || (ch >= '\u2000' && ch < '\u2100')) {
                        String hex = Integer.toHexString(ch);
                        result.append("\\u");
                        for (int j = 0; j < 4 - hex.length(); j++) {
                            result.append('0');
                        }
                        result.append(hex);
                    } else {
                        result.append(ch);
                    }
            }
        }
        return result.toString();
    }
} 