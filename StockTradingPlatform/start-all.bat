@echo off
cd /d "%~dp0"
echo Starting Stock Trading Platform...
echo.
echo Backend will run at:  http://localhost:8080
echo Frontend will run at: http://localhost:5173
echo.

start "Stock Trading Backend" "%~dp0run-backend.bat"

timeout /t 8 /nobreak > nul

start "Stock Trading Frontend" "%~dp0run-frontend.bat"

timeout /t 5 /nobreak > nul
start http://localhost:5173
