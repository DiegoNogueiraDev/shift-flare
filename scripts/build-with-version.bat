@echo off
setlocal enabledelayedexpansion

echo [34mObtendo versao atual...[0m

:: Obter versão atual
for /f "tokens=2 delims==" %%a in ('findstr /B "buildNumber" config\buildNumber.properties') do (
    set CURRENT_VERSION=%%a
)
echo [34mVersao atual:[0m 1.0.0-%CURRENT_VERSION%

:: Executar o Maven build com incremento de versão
echo [33mExecutando build com incremento de versao...[0m
call mvn clean package

:: Obter nova versão
for /f "tokens=2 delims==" %%a in ('findstr /B "buildNumber" config\buildNumber.properties') do (
    set NEW_VERSION=%%a
)
echo [32mBuild concluido![0m
echo [34mNova versao:[0m 1.0.0-%NEW_VERSION%

:: Mostrar o arquivo gerado
for /f "delims=" %%a in ('dir /b target\xpathprediction-*.jar ^| findstr /v ".original"') do (
    set JAR_FILE=target\%%a
)
echo [34mArquivo gerado:[0m %JAR_FILE%

:: Instruções de execução
echo.
echo [32mPara executar a aplicacao, use:[0m
echo java -jar %JAR_FILE% 