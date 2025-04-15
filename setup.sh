#!/bin/bash

# Cores para saída
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[0;33m'
RED='\033[0;31m'
NC='\033[0m' # Sem cor

echo -e "${BLUE}===============================================${NC}"
echo -e "${BLUE}   Configuração do XPathPrediction Service     ${NC}"
echo -e "${BLUE}===============================================${NC}"
echo

# Verificar se o Java está instalado
echo -e "${YELLOW}Verificando se o Java está instalado...${NC}"
if ! command -v java &> /dev/null; then
    echo -e "${RED}Java não encontrado. Por favor, instale o Java 11 ou superior.${NC}"
    exit 1
fi
JAVA_VERSION=$(java -version 2>&1 | head -1 | cut -d'"' -f2 | sed 's/^1\.//' | cut -d'.' -f1)
echo -e "${GREEN}Java encontrado: versão ${JAVA_VERSION}${NC}"

# Verificar se o Maven está instalado
echo -e "${YELLOW}Verificando se o Maven está instalado...${NC}"
if ! command -v mvn &> /dev/null; then
    echo -e "${RED}Maven não encontrado. Por favor, instale o Maven 3.6 ou superior.${NC}"
    exit 1
fi
MVN_VERSION=$(mvn --version | head -1 | cut -d' ' -f3)
echo -e "${GREEN}Maven encontrado: versão ${MVN_VERSION}${NC}"

# Configurar a API key da OpenRouter
echo
echo -e "${YELLOW}Configuração da API da OpenRouter${NC}"
echo -e "${BLUE}Você já possui uma API key da OpenRouter? (s/n)${NC}"
read -r HAS_KEY

if [ "$HAS_KEY" = "s" ] || [ "$HAS_KEY" = "S" ]; then
    echo -e "${BLUE}Por favor, digite sua API key da OpenRouter:${NC}"
    read -r API_KEY
    echo -e "${BLUE}Digite a URL do seu site (ou pressione Enter para usar localhost):${NC}"
    read -r SITE_URL
    SITE_URL=${SITE_URL:-http://localhost:8080}
    echo -e "${BLUE}Digite o nome do seu site (ou pressione Enter para usar XpathPrediction):${NC}"
    read -r SITE_NAME
    SITE_NAME=${SITE_NAME:-XpathPrediction}
    
    # Atualizar application.properties
    echo -e "${YELLOW}Atualizando configurações...${NC}"
    if [ -f "src/main/resources/application.properties" ]; then
        sed -i "s|openrouter.api-key=.*|openrouter.api-key=${API_KEY}|g" src/main/resources/application.properties
        sed -i "s|openrouter.site-url=.*|openrouter.site-url=${SITE_URL}|g" src/main/resources/application.properties
        sed -i "s|openrouter.site-name=.*|openrouter.site-name=${SITE_NAME}|g" src/main/resources/application.properties
        echo -e "${GREEN}Configurações atualizadas com sucesso!${NC}"
    else
        echo -e "${RED}Arquivo application.properties não encontrado!${NC}"
        exit 1
    fi
else
    echo -e "${YELLOW}Você precisará obter uma API key da OpenRouter e configurar manualmente.${NC}"
    echo -e "${BLUE}Visite: https://openrouter.ai para obter sua API key.${NC}"
fi

# Compilação
echo
echo -e "${YELLOW}Deseja compilar o projeto agora? (s/n)${NC}"
read -r BUILD_NOW

if [ "$BUILD_NOW" = "s" ] || [ "$BUILD_NOW" = "S" ]; then
    echo -e "${YELLOW}Compilando o projeto...${NC}"
    chmod +x scripts/build-with-version.sh
    ./scripts/build-with-version.sh
    
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}Compilação concluída com sucesso!${NC}"
        
        # Perguntar se deseja executar
        echo
        echo -e "${YELLOW}Deseja executar o serviço agora? (s/n)${NC}"
        read -r RUN_NOW
        
        if [ "$RUN_NOW" = "s" ] || [ "$RUN_NOW" = "S" ]; then
            echo -e "${YELLOW}Em qual porta deseja executar? (Pressione Enter para usar a porta padrão 8080)${NC}"
            read -r PORT
            
            chmod +x scripts/run.sh
            if [ -z "$PORT" ]; then
                ./scripts/run.sh
            else
                ./scripts/run.sh -p "$PORT"
            fi
        else
            echo -e "${BLUE}Para executar o serviço mais tarde, use:${NC}"
            echo -e "${GREEN}./scripts/run.sh${NC}"
        fi
    else
        echo -e "${RED}Erro durante a compilação. Verifique os erros acima.${NC}"
    fi
else
    echo -e "${BLUE}Para compilar o projeto mais tarde, use:${NC}"
    echo -e "${GREEN}./scripts/build-with-version.sh${NC}"
fi

echo
echo -e "${BLUE}===============================================${NC}"
echo -e "${GREEN}Configuração concluída!${NC}"
echo -e "${BLUE}===============================================${NC}" 