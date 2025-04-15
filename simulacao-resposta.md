# Simulação de Execução da API de Predição de XPath

Abaixo está uma simulação visual do funcionamento da API, mostrando o fluxo de entrada e saída.

## Execução do Servidor

```bash
$ java -jar target/xpathprediction-1.0.0.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.7.5.RELEASE)

2023-11-15 15:05:10.123  INFO 12345 --- [           main] c.e.x.XpathPredictionApplication         : Starting XpathPredictionApplication v1.0.0 using Java 11.0.11
2023-11-15 15:05:11.456  INFO 12345 --- [           main] c.e.x.XpathPredictionApplication         : No active profile set, falling back to 1 default profile: "default"
2023-11-15 15:05:12.789  INFO 12345 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8080 (http)
2023-11-15 15:05:13.123  INFO 12345 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http)
2023-11-15 15:05:13.456  INFO 12345 --- [           main] c.e.x.XpathPredictionApplication         : Started XpathPredictionApplication in 3.789 seconds (JVM running for 4.123)
```

## Simulação de Teste com o Script Bash

```bash
$ ./test-api.sh

Verificando se o servidor está rodando...
Servidor está rodando!

Enviando requisição para a API de predição de XPath...
XPath com erro: //div[@id="produto-antigo"]
DOM da página: <html><body><div class="container"><div id="produto-novo">iPhone 13</div></div></body></html>

Resposta da API:
{
  "newXpath": "//div[@id='produto-novo']"
}

Predição bem-sucedida!
Novo XPath sugerido: //div[@id='produto-novo']
```

## Logs do Servidor Durante o Processamento

```
2023-11-15 15:10:22.123  INFO 12345 --- [nio-8080-exec-1] c.e.x.c.XpathPredictionController        : Recebida requisição para XPath: //div[@id="produto-antigo"]
2023-11-15 15:10:22.234  INFO 12345 --- [nio-8080-exec-1] c.e.x.s.XpathPredictionService           : Processando predição para o xpath: //div[@id="produto-antigo"]
2023-11-15 15:10:22.345  INFO 12345 --- [nio-8080-exec-1] c.e.x.i.OpenRouterClientImpl             : Enviando dados para o OpenRouter: https://openrouter.ai/api/v1/chat/completions
```

## Detalhes do Processo na OpenRouter

1. **Construção do Prompt**:
   ```
   Você é um especialista em XPath para testes de automação web. 
   O seguinte XPath não está mais funcionando: //div[@id="produto-antigo"]

   Abaixo está o DOM atual da página:
   <html><body><div class="container"><div id="produto-novo">iPhone 13</div></div></body></html>

   Por favor, forneça apenas um novo XPath válido que poderia substituir o XPath com erro, 
   sem explicações adicionais. Retorne apenas o novo XPath.
   ```

2. **Payload enviado para a OpenRouter**:
   ```json
   {
     "model": "openai/gpt-4o",
     "messages": [
       {
         "role": "user",
         "content": "Você é um especialista em XPath para testes de automação web. O seguinte XPath não está mais funcionando: //div[@id=\"produto-antigo\"]..."
       }
     ]
   }
   ```

3. **Resposta da OpenRouter**:
   ```json
   {
     "id": "gen-abc123xyz456",
     "choices": [
       {
         "index": 0,
         "message": {
           "role": "assistant",
           "content": "//div[@id='produto-novo']"
         },
         "finish_reason": "stop"
       }
     ],
     "usage": {
       "prompt_tokens": 123,
       "completion_tokens": 5,
       "total_tokens": 128
     }
   }
   ```

4. **Logs Finais do Servidor**:
   ```
   2023-11-15 15:10:23.456  INFO 12345 --- [nio-8080-exec-1] c.e.x.i.OpenRouterClientImpl             : XPath sugerido recebido: //div[@id='produto-novo']
   2023-11-15 15:10:23.567  INFO 12345 --- [nio-8080-exec-1] c.e.x.c.XpathPredictionController        : Predição concluída com sucesso
   ```

## Exemplo com Cliente Java

Saída do `XPathTestClient.java`:

```
========== TESTE DE PREDIÇÃO DE XPATH ==========
XPath com erro: //div[@id="produto-antigo"]
DOM resumido: <html><body><div class="container"><div id="produt...
Enviando requisição para http://localhost:8080/api/v1/xpath/predict
Código de status: 200
Resposta: {"newXpath":"//div[@id='produto-novo']"}
NOVO XPATH SUGERIDO: //div[@id='produto-novo']
========== TESTE CONCLUÍDO COM SUCESSO ==========

========== TESTE DE PREDIÇÃO DE XPATH ==========
XPath com erro: //table[@class="tabela-produtos"]/tr[2]/td[3]
DOM resumido: <html><body><div class="produtos-grid"><div class...
Enviando requisição para http://localhost:8080/api/v1/xpath/predict
Código de status: 200
Resposta: {"newXpath":"//div[@class='produtos-grid']/div[@class='produto-item']/span[@class='preco']"}
NOVO XPATH SUGERIDO: //div[@class='produtos-grid']/div[@class='produto-item']/span[@class='preco']
========== TESTE CONCLUÍDO COM SUCESSO ==========

========== TESTE DE PREDIÇÃO DE XPATH ==========
XPath com erro: //div[@class="old-price-container"]/span[@class="price"]
DOM resumido: <html><body><div class="product-details"><div class...
Enviando requisição para http://localhost:8080/api/v1/xpath/predict
Código de status: 200
Resposta: {"newXpath":"//div[@class='product-details']/div[@class='new-price-wrapper']/p[@class='current-price']"}
NOVO XPATH SUGERIDO: //div[@class='product-details']/div[@class='new-price-wrapper']/p[@class='current-price']
========== TESTE CONCLUÍDO COM SUCESSO ==========
```

## Resumo do Funcionamento

1. **Cliente** envia o XPath com erro e o DOM da página
2. **Servidor** processa a solicitação e constrói um prompt especializado
3. **OpenRouter** analisa o prompt com modelos de IA e sugere um novo XPath
4. **Servidor** processa a resposta e retorna o novo XPath sugerido
5. **Cliente** utiliza o novo XPath para atualizar seus testes automatizados 