@echo off
setlocal
echo ======================================================================
echo    Starting SIMR — Smart Inventory & Predictive Reorder Platform
echo ======================================================================

if not exist bin\Main.class (
    echo [INFO] Compiling project classes first...
    call compile.bat /nopause
)

set JAVA_EXEC=java
where java >nul 2>nul
if %errorlevel% neq 0 (
    if exist "%USERPROFILE%\.jdks\ms-21.0.12\bin\java.exe" (
        set JAVA_EXEC="%USERPROFILE%\.jdks\ms-21.0.12\bin\java.exe"
    ) else if exist "%USERPROFILE%\.jdks\jbr-17.0.14\bin\java.exe" (
        set JAVA_EXEC="%USERPROFILE%\.jdks\jbr-17.0.14\bin\java.exe"
    )
)

echo [INFO] Using Java Runtime: %JAVA_EXEC%
echo [INFO] Starting embedded Java 21 HTTP Server on port 8080...

:: Launch browser in background once server is ready (avoids connection refused)
start "" powershell -NoProfile -WindowStyle Hidden -Command ^
  "for ($i=0; $i -lt 20; $i++) { Start-Sleep -Milliseconds 500; try { $r = [System.Net.WebRequest]::Create('http://localhost:8080/'); $r.Timeout=1000; $resp = $r.GetResponse(); $resp.Close(); Start-Process 'http://localhost:8080/'; break; } catch {} }"

%JAVA_EXEC% -cp "bin;lib/*" Main
pause
