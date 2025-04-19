#!/bin/bash

# Script para copiar os arquivos do frontend compilado para o diretório static-dist
# Este script deve ser executado após o build do frontend Next.js

# Cores para output
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color

# Diretório do frontend
FRONTEND_DIR=$(realpath "$(dirname "$0")/../../../../")

# Diretório de saída do Next.js
NEXT_OUT_DIR="$FRONTEND_DIR/out"

# Diretório destino no Spring Boot
STATIC_DIST_DIR="$(dirname "$0")/static-dist"

echo -e "${YELLOW}Iniciando cópia do frontend para o Spring Boot...${NC}"

# Verificar se o diretório de saída do Next.js existe
if [ ! -d "$NEXT_OUT_DIR" ]; then
    echo -e "${RED}Erro: Diretório de saída do Next.js não encontrado: $NEXT_OUT_DIR${NC}"
    echo -e "${YELLOW}Execute 'npm run build' e 'next export' no diretório do frontend primeiro.${NC}"
    exit 1
fi

# Limpar o diretório de destino
echo -e "${YELLOW}Limpando diretório de destino...${NC}"
find "$STATIC_DIST_DIR" -mindepth 1 -not -name "README.md" -not -name ".gitkeep" -exec rm -rf {} \;

# Copiar os arquivos
echo -e "${YELLOW}Copiando arquivos compilados do frontend...${NC}"
cp -r "$NEXT_OUT_DIR"/* "$STATIC_DIST_DIR"

# Verificar se a cópia foi bem-sucedida
if [ $? -eq 0 ]; then
    echo -e "${GREEN}Frontend copiado com sucesso para: $STATIC_DIST_DIR${NC}"
    echo -e "${GREEN}A aplicação Spring Boot agora servirá o frontend Next.js!${NC}"
else
    echo -e "${RED}Erro ao copiar os arquivos do frontend.${NC}"
    exit 1
fi

# Ajustar permissões
chmod -R 755 "$STATIC_DIST_DIR"

echo -e "${YELLOW}Processo concluído!${NC}" 