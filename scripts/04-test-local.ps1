# Quick local tests (no Asaas)
$ErrorActionPreference = "Stop"

Write-Host "== Checking Manager route existence (should be 405 Allow: POST) ==" -ForegroundColor Cyan
curl.exe -i http://localhost:8080/api/clients

Write-Host "`n== Create a client ==" -ForegroundColor Cyan
$body = @{ name="Henrique"; email="henrique@email.com"; cpf="12345678900" } | ConvertTo-Json
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/clients" -ContentType "application/json" -Body $body
