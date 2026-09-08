@echo off
title Pushing SIMR to GitHub
color 0b
echo ======================================================================
echo    SIMR // CYBER - Pushing to GitHub Repository
echo    Target: https://github.com/MOTTIMIVIKASCHOWDARY/PROGRAMMING-IN-JAVA-
echo ======================================================================
echo.
cd /d "%~dp0"
git push -u origin main
echo.
if %errorlevel% equ 0 (
    color 0a
    echo ======================================================================
    echo    SUCCESS: Repository and Website successfully pushed to GitHub!
    echo ======================================================================
) else (
    color 0c
    echo ======================================================================
    echo    NOTE: If GitHub prompted for login, please complete it above.
    echo ======================================================================
)
echo.
echo Press any key to close this window...
pause >nul
