#!/bin/bash

# Cores para saída
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[0;33m'
NC='\033[0m' # Sem cor

# Obter versão atual
CURRENT_VERSION=$(grep -o 'buildNumber=[0-9]*' config/buildNumber.properties | cut -d'=' -f2)
echo -e "${BLUE}Versão atual:${NC} 1.0.0-${CURRENT_VERSION}"

# Executar o Maven build com incremento de versão
echo -e "${YELLOW}Executando build com incremento de versão...${NC}"
mvn clean package

# Obter nova versão
NEW_VERSION=$(grep -o 'buildNumber=[0-9]*' config/buildNumber.properties | cut -d'=' -f2)
echo -e "${GREEN}Build concluído!${NC}"
echo -e "${BLUE}Nova versão:${NC} 1.0.0-${NEW_VERSION}"

# Mostrar o arquivo gerado
JAR_FILE=$(ls target/xpathprediction-*.jar | grep -v '.original')
echo -e "${BLUE}Arquivo gerado:${NC} ${JAR_FILE}"

# Instruções de execução
echo ""
echo -e "${GREEN}Para executar a aplicação, use:${NC}"
echo "java -jar ${JAR_FILE}" 