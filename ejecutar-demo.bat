@echo off
call compilar.bat
if errorlevel 1 exit /b %ERRORLEVEL%
java -cp out sigecad.principal.Main
