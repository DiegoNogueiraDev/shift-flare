#!/bin/bash

# Cores para saída
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # Sem cor

# Encontrar o arquivo JAR mais recente
JAR_FILE=$(ls -t target/xpathprediction-*.jar 2>/dev/null | grep -v '.original' | head -1)

if [ -z "$JAR_FILE" ]; then
  echo -e "${RED}Erro: Nenhum arquivo JAR encontrado na pasta target/.${NC}"
  echo -e "${BLUE}Execute primeiro o build: ${NC}./scripts/build-with-version.sh"
  exit 1
fi

# Obter a versão do JAR
VERSION=$(basename "$JAR_FILE" | grep -o -E '[0-9]+\.[0-9]+\.[0-9]+-[0-9]+')

echo -e "${BLUE}Executando XPathPrediction versão ${VERSION}${NC}"
echo -e "${BLUE}Arquivo JAR: ${NC}${JAR_FILE}"
echo ""

# Verificar se foi fornecido argumento de porta
if [ "$1" == "-p" ] && [ -n "$2" ]; then
  PORT=$2
  echo -e "${BLUE}Usando porta personalizada: ${NC}${PORT}"
  java -Dserver.port=${PORT} -jar ${JAR_FILE}
else
  echo -e "${BLUE}Usando porta padrão: ${NC}8080"
  java -jar ${JAR_FILE}
fi 