@echo off
cd /d "%~dp0"
echo Starting Stock Trading Platform backend...
echo Backend URL: http://localhost:8080
java -jar backend\target\stock-trading-platform-1.0.0.jar
pause
