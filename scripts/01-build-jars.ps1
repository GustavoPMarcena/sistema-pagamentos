# Build JARs for manager and proxy (Windows PowerShell)
$ErrorActionPreference = "Stop"

Write-Host "== Building charge-manager ==" -ForegroundColor Cyan
Push-Location (Join-Path $PSScriptRoot "..\charge-manager")
mvn -DskipTests clean package
Pop-Location

Write-Host "== Building charge-proxy ==" -ForegroundColor Cyan
Push-Location (Join-Path $PSScriptRoot "..\charge-proxy")
mvn -DskipTests clean package
Pop-Location

Write-Host "OK: JARs built." -ForegroundColor Green
