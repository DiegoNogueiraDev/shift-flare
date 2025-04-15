#!/bin/bash

# Cores para saída
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
YELLOW='\033[0;33m'
NC='\033[0m' # Sem cor

echo -e "${BLUE}===============================================${NC}"
echo -e "${BLUE}   Executando Cliente de Teste XPath Java       ${NC}"
echo -e "${BLUE}===============================================${NC}"
echo

# Modificar o diretório para o diretório de scripts
cd "$(dirname "$0")"
echo -e "${YELLOW}Diretório atual: $(pwd)${NC}"

# Verificar se o servidor está rodando
echo -e "${YELLOW}Verificando se o servidor está rodando...${NC}"
SERVER_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "http://localhost:8080/actuator/health" 2>/dev/null)

if [ "$SERVER_STATUS" != "200" ]; then
  echo -e "${RED}O servidor não está rodando em http://localhost:8080${NC}"
  echo -e "${YELLOW}Gostaria de iniciar o servidor agora? (s/n)${NC}"
  read -r START_SERVER
  
  if [ "$START_SERVER" = "s" ] || [ "$START_SERVER" = "S" ]; then
    echo -e "${BLUE}Iniciando o servidor...${NC}"
    cd .. && ./scripts/run.sh &
    # Esperar o servidor iniciar
    echo -e "${YELLOW}Aguardando o servidor iniciar...${NC}"
    for i in {1..10}; do
      echo -n "."
      sleep 1
      SERVER_STATUS=$(curl -s -o /dev/null -w "%{http_code}" "http://localhost:8080/actuator/health" 2>/dev/null)
      if [ "$SERVER_STATUS" = "200" ]; then
        echo ""
        echo -e "${GREEN}Servidor iniciado com sucesso!${NC}"
        cd - > /dev/null # Voltar para o diretório de scripts
        break
      fi
    done
    
    if [ "$SERVER_STATUS" != "200" ]; then
      echo ""
      echo -e "${RED}Não foi possível iniciar o servidor. Verifique os logs.${NC}"
      exit 1
    fi
  else
    echo -e "${RED}Por favor, inicie o servidor manualmente antes de executar o cliente de teste.${NC}"
    echo -e "${YELLOW}Execute: ./scripts/run.sh${NC}"
    exit 1
  fi
else
  echo -e "${GREEN}Servidor está rodando!${NC}"
fi

echo

# Compilar o cliente de teste
echo -e "${YELLOW}Compilando o cliente de teste...${NC}"

# Verificar se já existem arquivos compilados e limpar
if [ -f "XPathTestClient.class" ]; then
  rm *.class
fi

# Compilar
javac XPathTestClient.java

if [ $? -ne 0 ]; then
  echo -e "${RED}Erro na compilação. Verifique os erros acima.${NC}"
  exit 1
fi

echo -e "${GREEN}Compilação bem-sucedida!${NC}"
echo

# Executar o cliente de teste
echo -e "${YELLOW}Executando o cliente de teste...${NC}"
echo -e "${BLUE}===============================================${NC}"
java XPathTestClient

echo -e "${BLUE}===============================================${NC}"
echo -e "${GREEN}Teste concluído!${NC}"

# Limpar arquivos compilados
echo -e "${YELLOW}Limpando arquivos temporários...${NC}"
rm *.class

echo -e "${BLUE}===============================================${NC}" 