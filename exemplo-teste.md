# Exemplo de Teste da API de Predição de XPath

Abaixo está um exemplo completo de como testar a API de predição de XPath usando curl e o retorno esperado.

## Preparação

Primeiro, certifique-se de que:
1. O servidor está em execução (`java -jar target/xpathprediction-1.0.0.jar`)
2. A API da OpenRouter está configurada corretamente (API key, etc.)

## Exemplo 1: XPath simples por ID

### Requisição

```bash
curl -X POST http://localhost:8080/api/v1/xpath/predict \
  -H "Content-Type: application/json" \
  -d '{
    "errorXpath": "//div[@id=\"produto-antigo\"]",
    "pageDOM": "<html><body><div class=\"container\"><div id=\"produto-novo\">iPhone 13</div></div></body></html>"
  }'
```

### Resposta esperada

```json
{
  "newXpath": "//div[@id='produto-novo']"
}
```

## Exemplo 2: XPath mais complexo

### Requisição

```bash
curl -X POST http://localhost:8080/api/v1/xpath/predict \
  -H "Content-Type: application/json" \
  -d '{
    "errorXpath": "//table[@class=\"tabela-produtos\"]/tr[2]/td[3]",
    "pageDOM": "<html><body><div class=\"produtos-grid\"><div class=\"produto-item\"><span class=\"nome\">Mouse</span><span class=\"preco\">R$ 50,00</span></div></div></body></html>"
  }'
```

### Resposta esperada

```json
{
  "newXpath": "//div[@class='produtos-grid']/div[@class='produto-item']/span[@class='preco']"
}
```

## Exemplo 3: Input inválido (Erro)

### Requisição (DOM vazio)

```bash
curl -X POST http://localhost:8080/api/v1/xpath/predict \
  -H "Content-Type: application/json" \
  -d '{
    "errorXpath": "//button[@id=\"comprar\"]",
    "pageDOM": ""
  }'
```

### Resposta esperada (erro)

```json
{
  "errorType": "Erro de Validação",
  "message": "pageDOM: O body/DOM da página deve ser informado."
}
```

## Simulação de Integração Completa

Abaixo está um exemplo de como seria o fluxo completo quando um teste automatizado quebra devido a mudanças na página:

1. **Problema detectado**: Durante a execução de testes automatizados, o teste falha ao tentar localizar um elemento com XPath `//div[@id="preco-produto"]`

2. **Captura do DOM**: O framework de teste captura o DOM atual da página

3. **Chamada à API de Predição**:
   ```bash
   curl -X POST http://localhost:8080/api/v1/xpath/predict \
     -H "Content-Type: application/json" \
     -d '{
       "errorXpath": "//div[@id=\"preco-produto\"]",
       "pageDOM": "<html><body><section class=\"detalhes-produto\"><div class=\"info-produto\"><p class=\"valor\">R$ 199,90</p></div></section></body></html>"
     }'
   ```

4. **Resposta da API**:
   ```json
   {
     "newXpath": "//p[@class='valor']"
   }
   ```

5. **Atualização do teste**: O teste é atualizado automaticamente para usar o novo XPath e pode continuar funcionando

## Logs do Servidor (Exemplo)

```
2023-11-15 14:32:45.123  INFO  --- [http-nio-8080-exec-1] c.e.x.i.OpenRouterClientImpl             : Enviando dados para o OpenRouter: https://openrouter.ai/api/v1/chat/completions
2023-11-15 14:32:47.456  INFO  --- [http-nio-8080-exec-1] c.e.x.i.OpenRouterClientImpl             : XPath sugerido recebido: //p[@class='valor']
2023-11-15 14:32:47.458  INFO  --- [http-nio-8080-exec-1] c.e.x.c.XpathPredictionController        : Predição concluída com sucesso para XPath: //div[@id="preco-produto"]
``` 