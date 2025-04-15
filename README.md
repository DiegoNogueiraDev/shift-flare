# Microserviço de Predição de XPath

Este microserviço oferece uma API REST para sugerir novos XPaths quando os XPaths existentes param de funcionar devido a mudanças no DOM das páginas web. O serviço utiliza a API da OpenRouter para gerar predições baseadas em modelos de linguagem avançados.

## Índice

- [Requisitos](#requisitos)
- [Configuração](#configuração)
- [Compilação e Execução](#compilação-e-execução)
- [Uso da API](#uso-da-api)
  - [Endpoint para predição de XPath](#endpoint-para-predição-de-xpath)
  - [Endpoints de Teste](#endpoints-de-teste)
  - [Endpoints de Configuração](#endpoints-de-configuração)
  - [Documentação da API com Swagger](#documentação-da-api-com-swagger)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Como Funciona](#como-funciona)
- [Desenvolvimento e Contribuição](#desenvolvimento-e-contribuição)

## Requisitos

- Java 11 ou superior
- Maven 3.6 ou superior
- Conta na [OpenRouter](https://openrouter.ai/) com chave de API
- Conexão com a internet para acessar a API da OpenRouter

## Configuração

### 1. Clone o repositório

```bash
git clone https://github.com/seu-usuario/shift-flare.git
cd shift-flare
```

### 2. Configure as variáveis de ambiente

Você pode configurar as variáveis de ambiente necessárias para a API da OpenRouter de duas maneiras:

**A. Usando variáveis de ambiente:**

```bash
export OPENROUTER_API_KEY=sua-chave-api-aqui
export OPENROUTER_SITE_URL=https://seu-site.com
export OPENROUTER_SITE_NAME="Nome do Seu Site"
```

**B. Ou diretamente no arquivo application.properties:**

Edite o arquivo `src/main/resources/application.properties` e ajuste as configurações:

```properties
openrouter.api-key=sua-chave-api-aqui
openrouter.site-url=https://seu-site.com
openrouter.site-name=Nome do Seu Site
```

### 3. Configurações adicionais (opcional)

No arquivo `application.properties`, você pode ajustar:

- `server.port`: Porta do servidor (padrão: 8080)
- `openrouter.model`: Modelo usado para predições (padrão: google/gemini-2.5-pro-exp-03-25:free)
- `openrouter.use-fallback`: Habilitar/desabilitar o mecanismo de fallback quando a API falha (true/false)

## Compilação e Execução

### Compilação

O projeto usa o Maven para gerenciamento de dependências e construção.

```bash
mvn clean package
```

### Execução

Você pode executar a aplicação diretamente com o comando java:

```bash
java -jar target/shift-flare-[VERSÃO].jar
```

Ou usando o Maven:

```bash
mvn spring-boot:run
```

Após a execução, o serviço estará disponível em `http://localhost:8080` (ou na porta especificada).

## Uso da API

O serviço expõe endpoints REST para predição de XPath, testes e configuração.

### Endpoint para predição de XPath

**URL**: `/api/v1/xpath/predict`  
**Método**: POST  
**Content-Type**: application/json

**Corpo da requisição**:

```json
{
  "errorXpath": "//div[@id='old-id']",
  "pageDOM": "<html><body><div id='new-id'>Conteúdo</div></body></html>"
}
```

**Campos**:
- `errorXpath`: O XPath que não está mais funcionando.
- `pageDOM`: O DOM atual da página onde o XPath falhou.

**Exemplo de requisição usando curl**:

```bash
curl -X POST http://localhost:8080/api/v1/xpath/predict \
  -H "Content-Type: application/json" \
  -d '{
    "errorXpath": "//div[@id=\"old-id\"]",
    "pageDOM": "<html><body><div id=\"new-id\">Conteúdo</div></body></html>"
  }'
```

**Resposta de sucesso**:

```json
{
  "newXpath": "//div[@id='new-id']"
}
```

**Códigos de resposta**:
- `200 OK`: Predição bem-sucedida.
- `400 Bad Request`: Dados de entrada inválidos.
- `500 Internal Server Error`: Erro ao processar a predição.

### Endpoints de Teste

O serviço inclui endpoints para testar os cenários de predição de XPath e a conexão com o OpenRouter.

#### 1. Executar todos os cenários de teste

**URL**: `/api/test/run-all`  
**Método**: GET

Este endpoint executa todos os 5 cenários de teste predefinidos e retorna os resultados básicos, incluindo o XPath sugerido para cada caso.

**Exemplo de requisição**:

```bash
curl -X GET http://localhost:8080/api/test/run-all
```

#### 2. Executar todos os cenários com detalhes

**URL**: `/api/test/run-all/detailed`  
**Método**: GET

Executa todos os cenários e retorna resultados detalhados, incluindo payloads de requisição e resposta, estatísticas de sucesso e informações completas sobre cada cenário.

**Exemplo de requisição**:

```bash
curl -X GET http://localhost:8080/api/test/run-all/detailed
```

#### 3. Executar cenário específico

**URL**: `/api/test/run/{id}`  
**Método**: GET  
**Parâmetros de URL**: `id` - ID do cenário (1-5)

Executa um cenário específico e retorna os resultados detalhados apenas desse cenário.

**Exemplo de requisição**:

```bash
curl -X GET http://localhost:8080/api/test/run/1
```

**Cenários disponíveis**:
1. XPath com ID inexistente
2. XPath com classe alterada
3. XPath com estrutura hierárquica modificada
4. XPath com texto alterado
5. XPath em DOM complexo

#### 4. Testar conexão com OpenRouter

**URL**: `/api/test/test-openrouter-connection`  
**Método**: GET

Testa diretamente a conexão com a API do OpenRouter, enviando uma requisição simples e retornando detalhes completos da comunicação, incluindo status, erros e sugestões de solução.

**Exemplo de requisição**:

```bash
curl -X GET http://localhost:8080/api/test/test-openrouter-connection
```

### Endpoints de Configuração

O serviço permite atualizar a chave da API do OpenRouter em tempo de execução.

#### 1. Atualizar chave da API

**URL**: `/api/config/update-api-key`  
**Método**: POST  
**Content-Type**: application/json

**Corpo da requisição**:

```json
{
  "apiKey": "sk-or-v1-sua-nova-chave-api"
}
```

**Exemplo de requisição**:

```bash
curl -X POST http://localhost:8080/api/config/update-api-key \
  -H "Content-Type: application/json" \
  -d '{"apiKey": "sk-or-v1-sua-nova-chave-api"}'
```

#### 2. Verificar status da chave da API

**URL**: `/api/config/check-api-key`  
**Método**: POST

Verifica se a chave da API está configurada, retornando seu status e uma versão mascarada da chave.

**Exemplo de requisição**:

```bash
curl -X POST http://localhost:8080/api/config/check-api-key
```

### Documentação da API com Swagger

A documentação completa da API está disponível através do Swagger UI em:

```
http://localhost:8080/swagger-ui.html
```

A interface do Swagger permite explorar todos os endpoints, parâmetros e modelos de dados, além de testar as chamadas diretamente pela interface.

## Estrutura do Projeto

O projeto segue uma estrutura organizada:

```
├── src/                                     # Código-fonte
│   ├── main/
│   │   ├── java/com/shiftflare/xpathprediction/
│   │   │   ├── controller/                  # Controladores REST
│   │   │   │   ├── ApiKeyController.java    # Gerenciamento da chave API
│   │   │   │   ├── OpenRouterTestController.java # Testes do OpenRouter
│   │   │   │   └── XpathPredictionController.java # Predição de XPath
│   │   │   ├── domain/                      # Objetos de domínio
│   │   │   ├── exception/                   # Exceções e handlers
│   │   │   ├── integration/                 # Integração com serviços externos
│   │   │   │   ├── OpenRouterClient.java    # Interface do cliente OpenRouter
│   │   │   │   └── OpenRouterClientImpl.java # Implementação do cliente
│   │   │   ├── service/                     # Lógica de negócio
│   │   │   │   ├── OpenRouterTestService.java # Serviço de testes
│   │   │   │   └── XpathPredictionService.java # Serviço de predição
│   │   │   └── config/                      # Configurações Spring
│   │   │       ├── OpenRouterConfig.java    # Validação de configurações
│   │   │       ├── OpenRouterClientTestConfig.java # Teste de conexão
│   │   │       ├── RestTemplateConfig.java  # Configuração HTTP
│   │   │       └── SwaggerConfig.java       # Configuração da documentação
│   │   └── resources/
│   │       └── application.properties       # Configurações do aplicativo
│   └── test/                                # Testes
├── config/                                  # Arquivos de configuração
├── pom.xml                                  # Configuração Maven
└── README.md                                # Esta documentação
```

## Como Funciona

O fluxo de predição de XPath funciona da seguinte forma:

1. O cliente envia uma requisição POST com o XPath que falhou e o DOM atual da página.
2. O controlador `XpathPredictionController` valida a requisição e a encaminha para o serviço.
3. O serviço `XpathPredictionService` processa a requisição e chama o cliente de integração.
4. O cliente `OpenRouterClient` cria um prompt especializado e o envia para a API da OpenRouter.
5. A resposta da OpenRouter é processada para extrair o novo XPath sugerido.
6. O novo XPath é retornado ao cliente como resposta.

### Comunicação com OpenRouter

O serviço utiliza a API de completions da OpenRouter para gerar o novo XPath. O prompt é construído para instruir o modelo de linguagem a analisar o DOM atual e o XPath com erro, retornando um novo XPath válido que possa ser usado como substituto.

Quando a API do OpenRouter falha, o serviço pode utilizar um mecanismo de fallback inteligente (se configurado) para gerar um XPath alternativo baseado na análise do DOM.

## Mecanismo de Fallback

O sistema inclui um mecanismo de fallback robusto que é ativado quando:

1. A comunicação com a API do OpenRouter falha
2. A API retorna um erro
3. A resposta não contém um XPath válido

A lógica de fallback analisa o tipo de XPath original (baseado em ID, classe, texto, etc.) e o DOM atual para gerar uma alternativa adequada. O comportamento do fallback pode ser habilitado ou desabilitado através da configuração `openrouter.use-fallback` no arquivo `application.properties`.

## Desenvolvimento e Contribuição

### Executando os testes

Para executar os testes automatizados:

```bash
mvn test
```

### Modo de teste offline (sem OpenRouter)

Se você quiser testar a aplicação sem uma chave API da OpenRouter, pode habilitar o modo de fallback e desativar as chamadas externas:

1. No arquivo `application.properties`, configure:
```properties
openrouter.use-fallback=true
```

2. Execute a aplicação normalmente. Quando as chamadas à API falharem, o mecanismo de fallback gerará XPaths alternativos baseados no DOM. 