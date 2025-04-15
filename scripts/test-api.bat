@echo off
setlocal enabledelayedexpansion

echo [34mVerificando se o servidor esta rodando...[0m

:: Verifica se o servidor está rodando
for /f "tokens=*" %%a in ('curl -s -o nul -w "%%{http_code}" "http://localhost:8080/actuator/health" 2^>nul') do (
    set SERVER_STATUS=%%a
)

if not "!SERVER_STATUS!"=="200" (
    echo [31mServidor nao esta rodando em http://localhost:8080[0m
    echo [33mInicie o servidor com: scripts\run.bat[0m
    exit /b 1
)

echo [32mServidor esta rodando![0m
echo.

:: Verificar configuração da OpenRouter
echo [34mVerificando configuracao da OpenRouter...[0m
curl -s "http://localhost:8080/actuator/env" > temp_config.txt
findstr "openrouter.api-key" temp_config.txt > nul

if %ERRORLEVEL% NEQ 0 (
    echo [33mAtencao: API key da OpenRouter nao encontrada.[0m
    echo [33mExecute setup.bat para configurar ou atualize manualmente o application.properties.[0m
    echo [33mProsseguindo com o teste, mas pode falhar se o servico exigir acesso a OpenRouter.[0m
    echo.
) else (
    findstr "sua-chave-api-aqui\|OPENROUTER_API_KEY" temp_config.txt > nul
    if %ERRORLEVEL% EQU 0 (
        echo [33mAtencao: API key da OpenRouter nao configurada corretamente.[0m
        echo [33mExecute setup.bat para configurar ou atualize manualmente o application.properties.[0m
        echo [33mProsseguindo com o teste, mas pode falhar se o servico exigir acesso a OpenRouter.[0m
        echo.
    ) else (
        echo [32mConfiguracao da OpenRouter parece estar configurada.[0m
        echo.
    )
)
del temp_config.txt

:: Definir o DOM e XPath para teste
set ERROR_XPATH=//div[@id=\"produto-antigo\"]
set PAGE_DOM=^<html^>^<body^>^<div class=\"container\"^>^<div id=\"produto-novo\"^>iPhone 13^</div^>^</div^>^</body^>^</html^>

:: Construir o payload
set PAYLOAD={\"errorXpath\": \"%ERROR_XPATH%\", \"pageDOM\": \"%PAGE_DOM%\"}

echo [34mEnviando requisicao para a API de predicao de XPath...[0m
echo [33mXPath com erro: [0m%ERROR_XPATH%
echo [33mDOM da pagina: [0m%PAGE_DOM%
echo.

:: Enviar a requisição e salvar resposta em arquivo temporário
curl -s -X POST "http://localhost:8080/api/v1/xpath/predict" -H "Content-Type: application/json" -d "%PAYLOAD%" > response.json

:: Exibir resultado
echo [34mResposta da API:[0m
type response.json

:: Verificar se houve erro
findstr "errorType" response.json > nul
if %ERRORLEVEL% EQU 0 (
    echo.
    echo [31mFalha na predicao. Verificando logs do servidor...[0m
    
    :: Tentar obter logs do servidor (se disponível)
    echo [33mUltimas linhas do log do servidor:[0m
    curl -s "http://localhost:8080/actuator/logfile" > server_log.txt 2>nul
    if exist server_log.txt (
        for /f "skip=1000 delims=" %%i in (server_log.txt) do echo %%i
        del server_log.txt
    ) else (
        echo Logs nao disponiveis via actuator.
    )
    
    echo.
    echo [33mPossiveis causas do erro:[0m
    echo 1. API key da OpenRouter nao configurada ou invalida
    echo 2. Problema de conexao com a OpenRouter
    echo 3. Formato de dados invalido
    echo 4. Configuracao incorreta do servico
    
    echo.
    echo [34mSolucao sugerida:[0m
    echo Execute [32msetup.bat[0m e configure adequadamente a API key da OpenRouter
) else (
    :: Extrair o novo XPath usando PowerShell
    for /f "delims=" %%a in ('powershell -Command "(Get-Content response.json | ConvertFrom-Json).newXpath"') do (
        set NEW_XPATH=%%a
    )
    
    if defined NEW_XPATH (
        echo.
        echo [32mPredicao bem-sucedida![0m
        echo [33mNovo XPath sugerido: [0m!NEW_XPATH!
    ) else (
        echo.
        echo [31mResposta em formato inesperado. Verifique a saida acima.[0m
    )
)

:: Limpar arquivo temporário
del response.json 