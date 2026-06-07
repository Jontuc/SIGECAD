@echo off
if not exist lib\mysql-connector-j.jar (
  echo Falta lib\mysql-connector-j.jar
  exit /b 1
)
call compilar.bat
if errorlevel 1 exit /b %ERRORLEVEL%
java -cp "out;lib\mysql-connector-j.jar" sigecad.principal.Main --mysql
