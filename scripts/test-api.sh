#!/bin/bash

# Cores para saída
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
YELLOW='\033[0;33m'
NC='\033[0m' # Sem cor

# Verificar se o servidor está rodando
echo -e "${BLUE}Verificando se o servidor está rodando...${NC}"
SERVER_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "http://localhost:8080/actuator/health" 2>/dev/null)

if [ "$SERVER_STATUS" != "200" ]; then
  echo -e "${RED}Servidor não está rodando em http://localhost:8080${NC}"
  echo -e "${YELLOW}Inicie o servidor com: ./scripts/run.sh${NC}"
  exit 1
fi

echo -e "${GREEN}Servidor está rodando!${NC}\n"

# Verificar configuração da OpenRouter
echo -e "${BLUE}Verificando configuração da OpenRouter...${NC}"
CONFIG=$(curl -s "http://localhost:8080/actuator/env" | grep -o "openrouter.api-key.*" || echo "")

if [[ "$CONFIG" == *"sua-chave-api-aqui"* || "$CONFIG" == *"OPENROUTER_API_KEY"* || "$CONFIG" == "" ]]; then
  echo -e "${YELLOW}Atenção: API key da OpenRouter não configurada corretamente.${NC}"
  echo -e "${YELLOW}Execute ./setup.sh para configurar ou atualize manualmente o application.properties.${NC}"
  echo -e "${YELLOW}Prosseguindo com o teste, mas pode falhar se o serviço exigir acesso à OpenRouter.${NC}\n"
else
  echo -e "${GREEN}Configuração da OpenRouter parece estar configurada.${NC}\n"
fi

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

# Verificar se houve erro
if [[ "$RESPONSE" == *"errorType"* ]]; then
  echo -e "\n${RED}Falha na predição. Verificando logs do servidor...${NC}"
  
  # Tentar obter logs do servidor (se disponível)
  echo -e "${YELLOW}Últimas linhas do log do servidor:${NC}"
  curl -s "http://localhost:8080/actuator/logfile" 2>/dev/null | tail -n 20 || echo "Logs não disponíveis via actuator."
  
  echo -e "\n${YELLOW}Possíveis causas do erro:${NC}"
  echo -e "1. API key da OpenRouter não configurada ou inválida"
  echo -e "2. Problema de conexão com a OpenRouter"
  echo -e "3. Formato de dados inválido"
  echo -e "4. Configuração incorreta do serviço"
  
  echo -e "\n${BLUE}Solução sugerida:${NC}"
  echo -e "Execute ${GREEN}./setup.sh${NC} e configure adequadamente a API key da OpenRouter"
  exit 1
else
  # Extrair o novo XPath
  NEW_XPATH=$(echo $RESPONSE | grep -o '"newXpath":"[^"]*"' | cut -d'"' -f4)

  if [ ! -z "$NEW_XPATH" ]; then
    echo -e "\n${GREEN}Predição bem-sucedida!${NC}"
    echo -e "${YELLOW}Novo XPath sugerido: ${NC}$NEW_XPATH"
  else
    echo -e "\n${RED}Resposta em formato inesperado. Verifique a saída acima.${NC}"
  fi
fi 