# Deploy stack to Docker Swarm
$ErrorActionPreference = "Stop"
Push-Location (Join-Path $PSScriptRoot "..")

Write-Host "== Checking Swarm ==" -ForegroundColor Cyan
# If already part of a swarm, this prints an error; that's OK.
docker swarm init 2>$null | Out-Null

Write-Host "== Deploying stack: payments ==" -ForegroundColor Cyan
docker stack deploy -c stack.yml payments

Write-Host "== Services ==" -ForegroundColor Cyan
docker service ls

Pop-Location
Write-Host "OK: Stack deployed." -ForegroundColor Green
