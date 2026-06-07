@echo off
setlocal
if not exist out mkdir out
dir /s /b src\main\java\*.java src\test\java\*.java > fuentes.txt
javac -encoding UTF-8 -d out @fuentes.txt
set RESULTADO=%ERRORLEVEL%
del fuentes.txt
if not %RESULTADO%==0 exit /b %RESULTADO%
echo Compilacion finalizada correctamente.
