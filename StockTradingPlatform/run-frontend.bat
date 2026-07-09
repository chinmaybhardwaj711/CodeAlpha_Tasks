@echo off
cd /d "%~dp0frontend"
echo Starting Stock Trading Platform frontend...
echo Frontend URL: http://localhost:5173
call npm.cmd run dev -- --host 127.0.0.1
pause
