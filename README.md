# Microserviço de Predição de XPath

Este microserviço oferece uma API REST para sugerir novos XPaths quando os XPaths existentes param de funcionar devido a mudanças no DOM das páginas web. O serviço utiliza a API da OpenRouter para gerar predições baseadas em modelos de linguagem avançados.

## Índice

- [Requisitos](#requisitos)
- [Configuração](#configuração)
- [Compilação e Execução](#compilação-e-execução)
- [Uso da API](#uso-da-api)
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
git clone https://github.com/seu-usuario/xpathprediction.git
cd xpathprediction
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
- `openrouter.model`: Modelo usado para predições (padrão: openai/gpt-4o)

## Compilação e Execução

### Compilação

O projeto usa o Maven para gerenciamento de dependências e construção. O número da versão é incrementado automaticamente a cada build.

**Compilação standard:**

```bash
mvn clean package
```

**Compilação com visualização do incremento de versão:**

```bash
chmod +x build-with-version.sh
./build-with-version.sh
```

A versão do pacote segue o formato `1.0.0-X`, onde X é o número de build que é incrementado automaticamente.

### Execução

```bash
java -jar target/xpathprediction-1.0.0-[número-build].jar
```

Substitua `[número-build]` pelo número da versão atual, ou simplesmente use:

```bash
java -jar $(ls target/xpathprediction-*.jar | grep -v '.original')
```

Após a execução, o serviço estará disponível em `http://localhost:8080`.

## Uso da API

O serviço expõe um endpoint REST para predição de XPath.

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

### Documentação da API com Swagger

A documentação completa da API está disponível através do Swagger UI em:

```
http://localhost:8080/swagger-ui.html
```

## Estrutura do Projeto

O projeto segue uma estrutura padrão de microsserviços Spring Boot:

```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── xpathprediction/
│   │   │               ├── controller/      # Controladores REST
│   │   │               ├── domain/          # Objetos de domínio
│   │   │               ├── exception/       # Exceções e handlers
│   │   │               ├── integration/     # Integração com serviços externos
│   │   │               ├── service/         # Lógica de negócio
│   │   │               └── config/          # Configurações Spring
│   │   └── resources/
│   │       └── application.properties       # Configurações do aplicativo
│   └── test/                                # Testes
├── pom.xml                                  # Configuração Maven
├── buildNumber.properties                   # Controle de versão
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

## Desenvolvimento e Contribuição

### Executando os testes

Para executar os testes automatizados:

```bash
mvn test
```

### Adicionando novos recursos

Se desejar adicionar novos recursos:

1. Crie um fork do repositório
2. Crie uma branch para sua feature (`git checkout -b feature/nova-feature`)
3. Faça commit das suas alterações (`git commit -m 'Adiciona nova feature'`)
4. Envie para a branch (`git push origin feature/nova-feature`)
5. Abra um Pull Request

### Depuração

Para executar em modo de depuração:

```bash
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8000 -jar target/xpathprediction-1.0.0-[número-build].jar
``` 