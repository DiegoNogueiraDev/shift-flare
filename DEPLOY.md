# Guia de Implantação do Shift Flare

## Executando via JAR

Para executar a aplicação normalmente via arquivo JAR:

```bash
java -jar target/shift-flare-1.0.X.jar
```

Onde X é o número da versão gerado pelo plugin buildNumber.

## Implantação em Ambiente Sem Internet

Para implantar o Shift Flare em um ambiente sem internet, você tem duas opções:

### Opção 1: Instalação com JRE Existente

Se a máquina já possui Java 11 instalado:

1. Compile o projeto em uma máquina com internet:
   ```bash
   mvn clean package
   ```

2. Transfira o arquivo JAR gerado em `target/shift-flare-1.0.X.jar` para a máquina de destino

3. Execute na máquina de destino:
   ```bash
   java -jar shift-flare-1.0.X.jar
   ```

### Opção 2: Empacotamento com JRE Embutida (Recomendado)

Para evitar a necessidade de instalar Java na máquina de destino:

1. Baixe uma JRE 11 para Linux (na sua máquina com internet): 
   - Acesse: https://adoptium.net/temurin/releases/?version=11

2. Crie a seguinte estrutura de diretórios:
   ```
   shift-flare-package/
     ├── jre/                   # A JRE descompactada vai aqui
     ├── app/                   # O JAR vai aqui
     ├── start.sh               # Script de inicialização
     └── README.txt             # Instruções
   ```

3. Compacte o JAR e coloque na pasta `app/`:
   ```bash
   mvn clean package
   cp target/shift-flare-1.0.X.jar shift-flare-package/app/
   ```

4. Crie o script `start.sh` com o seguinte conteúdo:
   ```bash
   #!/bin/bash

   # Diretório onde o script está
   SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

   # Configurando variáveis
   export JAVA_HOME="$SCRIPT_DIR/jre"
   export PATH="$JAVA_HOME/bin:$PATH"

   # Executando o aplicativo
   "$JAVA_HOME/bin/java" -jar "$SCRIPT_DIR/app/shift-flare-1.0.X.jar"
   ```

5. Torne o script executável:
   ```bash
   chmod +x shift-flare-package/start.sh
   ```

6. Compacte tudo para transferir para a máquina de destino:
   ```bash
   tar -czvf shift-flare-package.tar.gz shift-flare-package/
   ```

7. Na máquina de destino, descompacte e execute:
   ```bash
   tar -xzvf shift-flare-package.tar.gz
   cd shift-flare-package
   ./start.sh
   ```

## Acesso à Aplicação

Após executar a aplicação, acesse via navegador:

- http://localhost:8080 - Página inicial
- http://localhost:8080/automation.html - Automação
- http://localhost:8080/migration.html - Migração
- http://localhost:8080/code-review.html - Revisão de Código
- http://localhost:8080/hercules.html - Hercules
- http://localhost:8080/xpath-prediction.html - Predição XPath

## Observações Importantes

1. **Limitações Offline:** A aplicação utiliza serviços de API externos (OpenRouter), então algumas funcionalidades podem não estar disponíveis sem acesso à internet.

2. **Requisitos de Sistema:**
   - Linux x86_64 (para a versão empacotada)
   - No mínimo 2GB de RAM recomendados

3. **Portas:** A aplicação usa a porta 8080 por padrão. Certifique-se de que esta porta esteja disponível ou altere a configuração em `application.properties`. 