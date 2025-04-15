#!/bin/bash

# Cores para saída
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
YELLOW='\033[0;33m'
NC='\033[0m' # Sem cor

# Verificar se o servidor está rodando
echo -e "${BLUE}Verificando se o servidor está rodando...${NC}"
if ! curl -s "http://localhost:8080/actuator/health" > /dev/null; then
  echo -e "${RED}Servidor não está rodando em http://localhost:8080${NC}"
  echo -e "${YELLOW}Inicie o servidor com: java -jar target/xpathprediction-1.0.0.jar${NC}"
  exit 1
fi

echo -e "${GREEN}Servidor está rodando!${NC}\n"

# Definir o DOM e XPath para teste
ERROR_XPATH="//div[@id=\"produto-antigo\"]"
PAGE_DOM="<html><body><div class=\"container\"><div id=\"produto-novo\">iPhone 13</div></div></body></html>"

# Construir o payload
PAYLOAD="{\"errorXpath\": \"$ERROR_XPATH\", \"pageDOM\": \"$PAGE_DOM\"}"

echo -e "${BLUE}Enviando requisição para a API de predição de XPath...${NC}"
echo -e "${YELLOW}XPath com erro: ${NC}$ERROR_XPATH"
echo -e "${YELLOW}DOM da página: ${NC}$PAGE_DOM\n"

# Enviar a requisição
RESPONSE=$(curl -s -X POST "http://localhost:8080/api/v1/xpath/predict" \
  -H "Content-Type: application/json" \
  -d "$PAYLOAD")

# Exibir resultado
echo -e "${BLUE}Resposta da API:${NC}"
echo $RESPONSE | jq '.' 2>/dev/null || echo $RESPONSE

# Extrair o novo XPath
NEW_XPATH=$(echo $RESPONSE | grep -o '"newXpath":"[^"]*"' | cut -d'"' -f4)

if [ ! -z "$NEW_XPATH" ]; then
  echo -e "\n${GREEN}Predição bem-sucedida!${NC}"
  echo -e "${YELLOW}Novo XPath sugerido: ${NC}$NEW_XPATH"
else
  echo -e "\n${RED}Falha na predição. Verifique os logs do servidor para mais detalhes.${NC}"
fi 