#!/bin/bash
# Script para configurar variáveis de ambiente para Docker Swarm

echo "🔧 Configurando variáveis de ambiente..."

# Variáveis obrigatórias
export ASAAS_API_KEY="aact_hmlg_000MzkwODA2MWY2OGM3MWRlMDU2NWM3MzJlNzZmNGZhZGY6OjcxMTg3YjNjLTUwMjQtNDJiYy1hOTIyLWM0MTY5N2E2OGMyNTo6JGFhY2hfYTRhZGM1NGUtNWQxZS00NjVmLTg2MWItMTE4M2IyZTc2ZDQ0"
export ASAAS_WEBHOOK_TOKEN="e8f9a1c4-asaas-webhook-secret"
export WEBHOOK_TOKEN="e8f9a1c4-asaas-webhook-secret"

# Variáveis opcionais (deixe vazio para modo MOCK)
export MAIL_HOST=""
export MAIL_PORT=""
export MAIL_USER=""
export MAIL_PASS=""

echo "✅ Variáveis configuradas:"
echo "   ASAAS_API_KEY: ${ASAAS_API_KEY:0:30}..."
echo "   ASAAS_WEBHOOK_TOKEN: $ASAAS_WEBHOOK_TOKEN"
echo "   WEBHOOK_TOKEN: $WEBHOOK_TOKEN"
echo ""
echo "💡 Para usar estas variáveis, execute: source setup-env.sh"
echo "💡 Ou copie e cole os exports no seu terminal antes do deploy"
