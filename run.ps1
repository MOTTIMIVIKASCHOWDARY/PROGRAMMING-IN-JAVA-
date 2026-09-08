# PowerShell Runner for SIMR Capstone Project
Write-Host "======================================================================" -ForegroundColor Cyan
Write-Host "   Starting SIMR — Smart Inventory & Predictive Reorder Platform" -ForegroundColor Cyan
Write-Host "======================================================================" -ForegroundColor Cyan

$javaExec = "java"
if (!(Get-Command java -ErrorAction SilentlyContinue)) {
    if (Test-Path "$env:USERPROFILE\.jdks\ms-21.0.12\bin\java.exe") {
        $javaExec = "$env:USERPROFILE\.jdks\ms-21.0.12\bin\java.exe"
    } elseif (Test-Path "$env:USERPROFILE\.jdks\jbr-17.0.14\bin\java.exe") {
        $javaExec = "$env:USERPROFILE\.jdks\jbr-17.0.14\bin\java.exe"
    }
}

if (!(Test-Path "bin")) {
    New-Item -ItemType Directory -Path "bin" -Force | Out-Null
    $javacExec = $javaExec.Replace("java.exe", "javac.exe")
    Write-Host "[INFO] Compiling Java classes..." -ForegroundColor Yellow
    $sources = Get-ChildItem -Path src -Filter *.java -Recurse | Select-Object -ExpandProperty FullName
    & $javacExec -cp "lib/*;bin" -d bin $sources
}

Write-Host "[INFO] Launching server on http://localhost:8080 ..." -ForegroundColor Green
Start-Process "http://localhost:8080/"
& $javaExec -cp "bin;lib/*" Main
