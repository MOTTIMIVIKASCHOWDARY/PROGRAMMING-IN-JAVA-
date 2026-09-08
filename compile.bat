@echo off
echo ======================================================================
echo    Compiling SIMR Capstone Project Sources (Java)
echo ======================================================================

if not exist bin mkdir bin

powershell -ExecutionPolicy Bypass -NoProfile -Command ^
  "$jc = 'javac';" ^
  "if (!(Get-Command javac -ErrorAction SilentlyContinue)) {" ^
  "  if (Test-Path \"$env:USERPROFILE\.jdks\ms-21.0.12\bin\javac.exe\") { $jc = \"$env:USERPROFILE\.jdks\ms-21.0.12\bin\javac.exe\" }" ^
  "  elseif (Test-Path \"$env:USERPROFILE\.jdks\jbr-17.0.14\bin\javac.exe\") { $jc = \"$env:USERPROFILE\.jdks\jbr-17.0.14\bin\javac.exe\" }" ^
  "};" ^
  "Write-Host \"[Compiler] Using: $jc\";" ^
  "$sources = Get-ChildItem -Path src -Filter *.java -Recurse | Select-Object -ExpandProperty FullName;" ^
  "& $jc -cp 'lib/*;bin' -d bin $sources;" ^
  "exit $LASTEXITCODE"

if %errorlevel% equ 0 (
    echo [SUCCESS] Compilation completed without errors.
) else (
    echo [ERROR] Compilation failed with code %errorlevel%.
)
if "%1"=="/nopause" goto end
pause
:end
