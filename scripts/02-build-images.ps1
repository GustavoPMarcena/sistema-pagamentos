# Build Docker images from the project root
$ErrorActionPreference = "Stop"

Push-Location (Join-Path $PSScriptRoot "..")

Write-Host "== Building Docker image: payments/manager:1.0 ==" -ForegroundColor Cyan
docker build -t payments/manager:1.0 .\charge-manager

Write-Host "== Building Docker image: payments/proxy:1.0 ==" -ForegroundColor Cyan
docker build -t payments/proxy:1.0 .\charge-proxy

Pop-Location
Write-Host "OK: Images built." -ForegroundColor Green
