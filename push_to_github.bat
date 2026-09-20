@echo off
title SIMR - Pushing to GitHub
color 0b
echo ======================================================================
echo    SIMR // CYBER - Pushing to GitHub Repository
echo    Target: https://github.com/MOTTIMIVIKASCHOWDARY/PROGRAMMING-IN-JAVA-
echo ======================================================================
echo.
cd /d "%~dp0"
echo [INFO] Pushing files to branch 'main'...
echo [INFO] If a browser window opens, please click 'Sign in' / 'Authorize'.
echo.
git push -u origin main
echo.
if %errorlevel% equ 0 (
    color 0a
    echo ======================================================================
    echo    SUCCESS: All src files, website, and README pushed to GitHub!
    echo ======================================================================
) else (
    color 0c
    echo ======================================================================
    echo    PUSH FAILED. Please make sure you are signed into GitHub.
    echo ======================================================================
)
echo.
pause
