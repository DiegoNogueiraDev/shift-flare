@echo off
setlocal EnableDelayedExpansion

echo [34m===============================================[0m
echo [34m   Configuracao do XPathPrediction Service     [0m
echo [34m===============================================[0m
echo.

:: Verificar se o Java está instalado
echo [33mVerificando se o Java esta instalado...[0m
java -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [31mJava nao encontrado. Por favor, instale o Java 11 ou superior.[0m
    goto :eof
)
for /f "tokens=3" %%a in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVA_VERSION=%%a
)
set JAVA_VERSION=!JAVA_VERSION:"=!
echo [32mJava encontrado: versao !JAVA_VERSION![0m

:: Verificar se o Maven está instalado
echo [33mVerificando se o Maven esta instalado...[0m
mvn -version >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [31mMaven nao encontrado. Por favor, instale o Maven 3.6 ou superior.[0m
    goto :eof
)
for /f "tokens=3" %%a in ('mvn --version 2^>^&1 ^| findstr /i "Apache Maven"') do (
    set MVN_VERSION=%%a
)
echo [32mMaven encontrado: versao !MVN_VERSION![0m

:: Configurar a API key da OpenRouter
echo.
echo [33mConfiguracao da API da OpenRouter[0m
set /p HAS_KEY=[34mVoce ja possui uma API key da OpenRouter? (s/n) [0m

if /i "!HAS_KEY!"=="s" (
    set /p API_KEY=[34mPor favor, digite sua API key da OpenRouter: [0m
    set /p SITE_URL=[34mDigite a URL do seu site (ou pressione Enter para usar localhost): [0m
    if "!SITE_URL!"=="" set SITE_URL=http://localhost:8080
    set /p SITE_NAME=[34mDigite o nome do seu site (ou pressione Enter para usar XpathPrediction): [0m
    if "!SITE_NAME!"=="" set SITE_NAME=XpathPrediction
    
    :: Atualizar application.properties
    echo [33mAtualizando configuracoes...[0m
    if exist "src\main\resources\application.properties" (
        powershell -Command "(Get-Content src\main\resources\application.properties) -replace 'openrouter.api-key=.*', 'openrouter.api-key=!API_KEY!' | Set-Content src\main\resources\application.properties"
        powershell -Command "(Get-Content src\main\resources\application.properties) -replace 'openrouter.site-url=.*', 'openrouter.site-url=!SITE_URL!' | Set-Content src\main\resources\application.properties"
        powershell -Command "(Get-Content src\main\resources\application.properties) -replace 'openrouter.site-name=.*', 'openrouter.site-name=!SITE_NAME!' | Set-Content src\main\resources\application.properties"
        echo [32mConfiguracoes atualizadas com sucesso![0m
    ) else (
        echo [31mArquivo application.properties nao encontrado![0m
        goto :eof
    )
) else (
    echo [33mVoce precisara obter uma API key da OpenRouter e configurar manualmente.[0m
    echo [34mVisite: https://openrouter.ai para obter sua API key.[0m
)

:: Compilação
echo.
set /p BUILD_NOW=[33mDeseja compilar o projeto agora? (s/n) [0m

if /i "!BUILD_NOW!"=="s" (
    echo [33mCompilando o projeto...[0m
    call scripts\build-with-version.bat
    
    if %ERRORLEVEL% EQU 0 (
        echo [32mCompilacao concluida com sucesso![0m
        
        :: Perguntar se deseja executar
        echo.
        set /p RUN_NOW=[33mDeseja executar o servico agora? (s/n) [0m
        
        if /i "!RUN_NOW!"=="s" (
            set /p PORT=[33mEm qual porta deseja executar? (Pressione Enter para usar a porta padrao 8080) [0m
            
            if "!PORT!"=="" (
                call scripts\run.bat
            ) else (
                call scripts\run.bat -p !PORT!
            )
        ) else (
            echo [34mPara executar o servico mais tarde, use:[0m
            echo [32mscripts\run.bat[0m
        )
    ) else (
        echo [31mErro durante a compilacao. Verifique os erros acima.[0m
    )
) else (
    echo [34mPara compilar o projeto mais tarde, use:[0m
    echo [32mscripts\build-with-version.bat[0m
)

echo.
echo [34m===============================================[0m
echo [32mConfiguracao concluida![0m
echo [34m===============================================[0m

pause 