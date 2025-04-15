@echo off
setlocal enabledelayedexpansion

:: Cores para saída
set "GREEN=92m"
set "RED=91m"
set "BLUE=94m"
set "YELLOW=93m"
set "NC=0m"

echo [%BLUE%===============================================[%NC%
echo [%BLUE%   Executando Cliente de Teste XPath Java      [%NC%
echo [%BLUE%===============================================[%NC%
echo.

:: Modificar o diretório para o diretório de scripts
cd /d "%~dp0"
echo [%YELLOW%Diretório atual: %CD%[%NC%

:: Verificar se o servidor está rodando
echo [%YELLOW%Verificando se o servidor está rodando...[%NC%
curl -s -o nul -w "%%{http_code}" "http://localhost:8080/actuator/health" > temp.txt 2>nul
set /p SERVER_STATUS=<temp.txt
del temp.txt

if not "%SERVER_STATUS%"=="200" (
  echo [%RED%O servidor não está rodando em http://localhost:8080[%NC%
  echo [%YELLOW%Gostaria de iniciar o servidor agora? (s/n)[%NC%
  set /p START_SERVER=
  
  if /i "%START_SERVER%"=="s" (
    echo [%BLUE%Iniciando o servidor...[%NC%
    pushd ..
    start /b scripts\run.bat
    popd
    
    :: Esperar o servidor iniciar
    echo [%YELLOW%Aguardando o servidor iniciar...[%NC%
    for /l %%i in (1,1,10) do (
      <nul set /p =.
      timeout /t 1 /nobreak >nul
      curl -s -o nul -w "%%{http_code}" "http://localhost:8080/actuator/health" > temp.txt 2>nul
      set /p SERVER_STATUS=<temp.txt
      del temp.txt
      if "!SERVER_STATUS!"=="200" (
        echo.
        echo [%GREEN%Servidor iniciado com sucesso![%NC%
        goto :server_running
      )
    )
    
    echo.
    echo [%RED%Não foi possível iniciar o servidor. Verifique os logs.[%NC%
    exit /b 1
  ) else (
    echo [%RED%Por favor, inicie o servidor manualmente antes de executar o cliente de teste.[%NC%
    echo [%YELLOW%Execute: scripts\run.bat[%NC%
    exit /b 1
  )
) else (
  echo [%GREEN%Servidor está rodando![%NC%
)

:server_running
echo.

:: Compilar o cliente de teste
echo [%YELLOW%Compilando o cliente de teste...[%NC%

:: Verificar se já existem arquivos compilados e limpar
if exist "XPathTestClient.class" (
  del *.class
)

:: Compilar
javac XPathTestClient.java

if errorlevel 1 (
  echo [%RED%Erro na compilação. Verifique os erros acima.[%NC%
  exit /b 1
)

echo [%GREEN%Compilação bem-sucedida![%NC%
echo.

:: Executar o cliente de teste
echo [%YELLOW%Executando o cliente de teste...[%NC%
echo [%BLUE%===============================================[%NC%
java XPathTestClient

echo [%BLUE%===============================================[%NC%
echo [%GREEN%Teste concluído![%NC%

:: Limpar arquivos compilados
echo [%YELLOW%Limpando arquivos temporários...[%NC%
del *.class

echo [%BLUE%===============================================[%NC% 