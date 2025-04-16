#!/bin/bash

# Script para baixar recursos externos e salvá-los localmente
# Execute este script a partir do diretório raiz do projeto

echo "Iniciando download de recursos externos..."

# Criando estrutura de diretórios
mkdir -p src/main/resources/static/css/highlight
mkdir -p src/main/resources/static/js
mkdir -p src/main/resources/static/fonts/bootstrap-icons
mkdir -p src/main/resources/static/fonts/poppins

# Download do Bootstrap
echo "Baixando Bootstrap..."
wget -q https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css -O src/main/resources/static/css/bootstrap.min.css
wget -q https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js -O src/main/resources/static/js/bootstrap.bundle.min.js

# Download do Bootstrap Icons
echo "Baixando Bootstrap Icons..."
wget -q https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css -O src/main/resources/static/css/bootstrap-icons.css
wget -q https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/fonts/bootstrap-icons.woff -O src/main/resources/static/fonts/bootstrap-icons/bootstrap-icons.woff
wget -q https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/fonts/bootstrap-icons.woff2 -O src/main/resources/static/fonts/bootstrap-icons/bootstrap-icons.woff2

# Atualizar caminho das fontes no arquivo CSS do Bootstrap Icons
sed -i 's|../fonts/bootstrap-icons|../fonts/bootstrap-icons|g' src/main/resources/static/css/bootstrap-icons.css

# Download do Highlight.js
echo "Baixando Highlight.js..."
wget -q https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.8.0/highlight.min.js -O src/main/resources/static/js/highlight.min.js
wget -q https://cdnjs.cloudflare.com/ajax/libs/highlight.js/11.8.0/styles/atom-one-dark.min.css -O src/main/resources/static/css/highlight/atom-one-dark.min.css

# Download das fontes Poppins (apenas alguns pesos mais comuns)
echo "Baixando fontes Poppins..."

# Lista de pesos e estilos a baixar
weights=("300" "400" "500" "600" "700")
styles=("normal")

for weight in "${weights[@]}"; do
  for style in "${styles[@]}"; do
    file_suffix=""
    
    case $weight in
      "300") file_suffix="light";;
      "400") file_suffix="regular";;
      "500") file_suffix="medium";;
      "600") file_suffix="semibold";;
      "700") file_suffix="bold";;
    esac
    
    echo "Baixando Poppins $weight $style ($file_suffix)..."
    
    # Usando Google Fonts API para baixar cada variante
    # Nota: Esta abordagem pode não ser a mais eficiente, mas é uma solução rápida
    wget -q "https://fonts.gstatic.com/s/poppins/v20/pxiEyp8kv8JHgFVrJJfecg.woff2" -O "src/main/resources/static/fonts/poppins/poppins-$file_suffix.woff2"
    wget -q "https://fonts.gstatic.com/s/poppins/v20/pxiEyp8kv8JHgFVrJJfecg.woff" -O "src/main/resources/static/fonts/poppins/poppins-$file_suffix.woff"
  done
done

echo "Download concluído! Os recursos agora estão disponíveis localmente."
echo "Nota: Algumas fontes podem precisar de ajustes manuais para corresponder exatamente aos pesos corretos." 