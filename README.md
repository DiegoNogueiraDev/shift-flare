# Microserviço Shift-Flare com OpenRouter

Este microserviço oferece uma API REST para sugerir novos XPaths quando os XPaths existentes param de funcionar devido a mudanças no DOM das páginas web. O serviço utiliza a API da OpenRouter para gerar predições baseadas em modelos de linguagem avançados.

## Índice

- [Requisitos](#requisitos)
- [Configuração](#configuração)
- [Compilação e Execução](#compilação-e-execução)
- [Funcionalidades Implementadas](#funcionalidades-implementadas)
  - [1. Predição de XPath](#1-predição-de-xpath)
  - [2. Análise de Código (Code Review)](#2-análise-de-código-code-review)
  - [3. Geração de Scripts de Automação](#3-geração-de-scripts-de-automação)
  - [4. Migração de Código Q2 para Q3](#4-migração-de-código-q2-para-q3)
- [Uso da API](#uso-da-api)
  - [Endpoint para predição de XPath](#endpoint-para-predição-de-xpath)
  - [Endpoints de IA](#endpoints-de-ia)
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

## Funcionalidades Implementadas

O projeto possui 4 funcionalidades principais que utilizam a API do OpenRouter para processamento de linguagem natural:

### 1. Predição de XPath

Esta é a funcionalidade principal que analisa um XPath quebrado e o DOM atual da página para sugerir um novo XPath válido.

**Endpoint:** `/api/v1/xpath/predict`

**Como funciona:**
1. Recebe o XPath quebrado e o DOM atual da página
2. Envia para o modelo LLM via OpenRouter com um prompt especializado
3. Processa a resposta e extrai o novo XPath sugerido
4. Retorna o XPath que deve funcionar com o DOM atual

### 2. Análise de Código (Code Review)

Permite analisar código-fonte e receber feedback detalhado sobre qualidade, bugs e possíveis melhorias.

**Endpoint:** `/api/ai/code-review`

**Como funciona:**
1. Recebe o código a ser analisado e a linguagem (opcional)
2. Envia para o modelo LLM com um prompt estruturado para análise de código
3. Retorna uma revisão detalhada com problemas identificados e sugestões

### 3. Geração de Scripts de Automação

Gera scripts de automação de testes baseados em descrições em linguagem natural.

**Endpoint:** `/api/ai/automation`

**Como funciona:**
1. Recebe uma descrição do que deve ser automatizado e o framework desejado
2. Envia para o modelo LLM com um prompt especializado para automação
3. Retorna um script completo de automação pronto para uso

### 4. Migração de Código Q2 para Q3

Converte código legado da estrutura Q2 para o padrão mais recente Q3.

**Endpoint:** `/api/ai/migration`

**Como funciona:**
1. Recebe o código Q2 a ser migrado e o tipo de componente
2. Envia para o modelo LLM com instruções específicas de migração
3. Retorna o código convertido para a estrutura Q3

## Uso da API

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

### Endpoints de IA

#### 1. Análise de Código (Code Review)

**URL**: `/api/ai/code-review`  
**Método**: POST  
**Content-Type**: application/json

**Corpo da requisição**:

```json
{
  "code": "function soma(a, b) { return a + b; }",
  "language": "javascript"
}
```

**Exemplo de requisição**:

```bash
curl -X POST http://localhost:8080/api/ai/code-review \
  -H "Content-Type: application/json" \
  -d '{
    "code": "function soma(a, b) { return a + b; }",
    "language": "javascript"
  }'
```

#### 2. Geração de Scripts de Automação

**URL**: `/api/ai/automation`  
**Método**: POST  
**Content-Type**: application/json

**Corpo da requisição**:

```json
{
  "description": "Automatizar login em uma página web, inserir username e senha e clicar no botão submit",
  "framework": "selenium"
}
```

**Exemplo de requisição**:

```bash
curl -X POST http://localhost:8080/api/ai/automation \
  -H "Content-Type: application/json" \
  -d '{
    "description": "Automatizar login em uma página web, inserir username e senha e clicar no botão submit",
    "framework": "selenium"
  }'
```

#### 3. Migração de Código Q2 para Q3

**URL**: `/api/ai/migration`  
**Método**: POST  
**Content-Type**: application/json

**Corpo da requisição**:

```json
{
  "code": "// código Q2 a ser migrado",
  "componentType": "widget"
}
```

**Exemplo de requisição**:

```bash
curl -X POST http://localhost:8080/api/ai/migration \
  -H "Content-Type: application/json" \
  -d '{
    "code": "// código Q2 a ser migrado",
    "componentType": "widget"
  }'
```

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

#### 3. Executar cenário específico

**URL**: `/api/test/run/{id}`  
**Método**: GET  
**Parâmetros de URL**: `id` - ID do cenário (1-5)

Executa um cenário específico e retorna os resultados detalhados apenas desse cenário.

**Cenários disponíveis**:
1. XPath com ID inexistente
2. XPath com classe alterada
3. XPath com estrutura hierárquica modificada
4. XPath com texto alterado
5. XPath em DOM complexo

#### 4. Testar conexão com OpenRouter

**URL**: `/api/test/test-openrouter-connection`  
**Método**: GET

Testa diretamente a conexão com a API do OpenRouter, enviando uma requisição simples e retornando detalhes completos da comunicação.

### Endpoints de Configuração

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

#### 2. Verificar status da chave da API

**URL**: `/api/config/check-api-key`  
**Método**: POST

Verifica se a chave da API está configurada, retornando seu status e uma versão mascarada da chave.

### Documentação da API com Swagger

A documentação completa da API está disponível através do Swagger UI em:

```
http://localhost:8080/swagger-ui.html
```

## Estrutura do Projeto

O projeto segue os princípios de Clean Architecture e SOLID:

```
src/main/java/com/shiftflare/xpathprediction/
├── config/             # Configurações do Spring Boot
├── controller/         # Controladores REST
│   ├── AiController.java              # Endpoints de IA
│   ├── ApiKeyController.java          # Gestão de chaves API
│   ├── OpenRouterTestController.java  # Testes da integração
│   └── XpathPredictionController.java # Predição de XPath
├── domain/             # Entidades e modelos de dados
├── exception/          # Tratamento de exceções
├── integration/        # Integração com APIs externas
│   ├── AiPromptService.java         # Serviço de prompts de IA
│   ├── OpenRouterClient.java        # Interface do cliente
│   └── OpenRouterClientImpl.java    # Implementação do cliente
└── service/            # Lógica de negócios
    ├── OpenRouterTestService.java   # Serviço de testes
    └── XpathPredictionService.java  # Serviço de predição
```

## Como Funciona

O microserviço funciona através da integração com a API OpenRouter, que fornece acesso a modelos de linguagem avançados. O processo é o seguinte:

1. O cliente envia uma requisição com dados específicos (XPath quebrado, código para revisão, etc.)
2. O controlador apropriado valida a entrada e aciona o serviço relevante
3. O serviço constrói um prompt específico para a tarefa
4. A integração com OpenRouter envia o prompt para o modelo de linguagem
5. A resposta é processada, extraindo as informações relevantes
6. O resultado é formatado e retornado ao cliente

Em caso de falha na API do OpenRouter, o sistema pode usar mecanismos de fallback configuráveis.

## Desenvolvimento e Contribuição

Para contribuir com o projeto:

1. Faça um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/nova-funcionalidade`)
3. Implemente as mudanças e adicione testes quando possível
4. Execute os testes (`mvn test`)
5. Faça commit das mudanças (`git commit -am 'Adiciona nova funcionalidade'`)
6. Envie para o GitHub (`git push origin feature/nova-funcionalidade`)
7. Crie um Pull Request

Para relatórios de bugs ou sugestões, por favor abra uma issue no repositório. 