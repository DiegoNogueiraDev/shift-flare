@echo off
setlocal

:: Encontrar o arquivo JAR mais recente
for /f "delims=" %%a in ('dir /b /o-d target\xpathprediction-*.jar ^| findstr /v ".original"') do (
    set "JAR_FILE=target\%%a"
    goto :found
)

:not_found
echo [31mErro: Nenhum arquivo JAR encontrado na pasta target\.[0m
echo [34mExecute primeiro o build: [0mscripts\build-with-version.bat
exit /b 1

:found
:: Extrair a versão do nome do arquivo
for /f "tokens=1 delims=" %%a in ("%JAR_FILE%") do (
    set "FILE_NAME=%%~nxa"
)
for /f "tokens=2 delims=-" %%a in ("%FILE_NAME%") do (
    set "VERSION=1.0.0-%%a"
)

echo [34mExecutando XPathPrediction versao %VERSION%[0m
echo [34mArquivo JAR: [0m%JAR_FILE%
echo.

:: Verificar se foi fornecido argumento de porta
if "%1"=="-p" (
    if not "%2"=="" (
        echo [34mUsando porta personalizada: [0m%2
        java -Dserver.port=%2 -jar %JAR_FILE%
    ) else (
        goto :default_port
    )
) else (
    :default_port
    echo [34mUsando porta padrao: [0m8080
    java -jar %JAR_FILE%
) 