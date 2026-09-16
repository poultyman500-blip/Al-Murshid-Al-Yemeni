@echo off
setlocal
cd /d %~dp0
where gradle >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo Gradle is not installed or not on PATH.
    exit /b 1
)
call gradle %*
