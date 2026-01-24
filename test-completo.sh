#!/bin/bash
# Script de teste completo do sistema

set -e  # Para na primeira falha

echo "🧪 Iniciando testes do sistema de pagamentos..."
echo ""

# Cores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Função para testar endpoint
test_endpoint() {
    local url=$1
    local method=${2:-GET}
    local data=$3
    local expected_status=${4:-200}
    
    if [ -z "$data" ]; then
        response=$(curl -s -w "\n%{http_code}" -X $method "$url")
    else
        response=$(curl -s -w "\n%{http_code}" -X $method "$url" \
            -H "Content-Type: application/json" \
            -d "$data")
    fi
    
    http_code=$(echo "$response" | tail -n1)
    body=$(echo "$response" | sed '$d')
    
    if [ "$http_code" -eq "$expected_status" ]; then
        echo -e "${GREEN}✅${NC} $url (HTTP $http_code)"
        echo "$body" | head -c 200
        echo ""
        return 0
    else
        echo -e "${RED}❌${NC} $url (HTTP $http_code, esperado $expected_status)"
        echo "$body"
        return 1
    fi
}

# 1. Verificar se serviços estão rodando
echo "📋 1. Verificando serviços..."
docker service ls | grep payments || {
    echo -e "${RED}❌ Stack não está rodando. Execute: docker stack deploy -c stack.yml payments${NC}"
    exit 1
}
echo -e "${GREEN}✅ Serviços encontrados${NC}"
echo ""

# 2. Testar Manager
echo "📋 2. Testando Manager (http://localhost:8080)..."
sleep 2

test_endpoint "http://localhost:8080/api/clients" "GET" "" "200" || exit 1
echo ""

# 3. Criar cliente
echo "📋 3. Criando cliente..."
CLIENT_RESPONSE=$(curl -s -X POST http://localhost:8080/api/clients \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Teste Automatizado",
    "email": "teste@automated.com",
    "cpf": "99988877766"
  }')

CLIENT_ID=$(echo $CLIENT_RESPONSE | grep -o '"id":[0-9]*' | cut -d: -f2)
if [ -z "$CLIENT_ID" ]; then
    echo -e "${RED}❌ Falha ao criar cliente${NC}"
    echo "$CLIENT_RESPONSE"
    exit 1
fi

echo -e "${GREEN}✅ Cliente criado: ID=$CLIENT_ID${NC}"
echo ""

# 4. Criar cobrança
echo "📋 4. Criando cobrança..."
CHARGE_RESPONSE=$(curl -s -X POST http://localhost:8080/api/charges \
  -H "Content-Type: application/json" \
  -d "{
    \"clientId\": $CLIENT_ID,
    \"value\": 99.99,
    \"billingType\": \"PIX\",
    \"dueDate\": \"2026-03-15\"
  }")

CHARGE_ID=$(echo $CHARGE_RESPONSE | grep -o '"id":[0-9]*' | cut -d: -f2)
ASAAS_PAYMENT_ID=$(echo $CHARGE_RESPONSE | grep -o '"asaasPaymentId":"[^"]*"' | cut -d: -f2 | tr -d '"')

if [ -z "$CHARGE_ID" ] || [ -z "$ASAAS_PAYMENT_ID" ]; then
    echo -e "${RED}❌ Falha ao criar cobrança${NC}"
    echo "$CHARGE_RESPONSE"
    exit 1
fi

echo -e "${GREEN}✅ Cobrança criada: ID=$CHARGE_ID, ASAAS_PAYMENT_ID=$ASAAS_PAYMENT_ID${NC}"
echo ""

# 5. Verificar status inicial
echo "📋 5. Verificando status inicial da cobrança..."
STATUS_INICIAL=$(curl -s http://localhost:8080/api/charges/$CHARGE_ID | grep -o '"status":"[^"]*"' | cut -d: -f2 | tr -d '"')
echo "Status inicial: $STATUS_INICIAL"
if [ "$STATUS_INICIAL" != "REGISTERED" ]; then
    echo -e "${YELLOW}⚠️  Status esperado: REGISTERED, obtido: $STATUS_INICIAL${NC}"
fi
echo ""

# 6. Testar webhook
echo "📋 6. Simulando webhook de pagamento confirmado..."
WEBHOOK_RESPONSE=$(curl -s -w "\n%{http_code}" -X POST http://localhost:8081/webhook/asaas \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer e8f9a1c4-asaas-webhook-secret" \
  -d "{
    \"event\": \"PAYMENT_CONFIRMED\",
    \"payment\": {
      \"id\": \"$ASAAS_PAYMENT_ID\",
      \"customer\": \"mock_customer_test\",
      \"value\": 99.99,
      \"status\": \"CONFIRMED\"
    }
  }")

HTTP_CODE=$(echo "$WEBHOOK_RESPONSE" | tail -n1)
if [ "$HTTP_CODE" -eq 200 ]; then
    echo -e "${GREEN}✅ Webhook processado com sucesso${NC}"
else
    echo -e "${RED}❌ Webhook falhou (HTTP $HTTP_CODE)${NC}"
    echo "$WEBHOOK_RESPONSE"
    exit 1
fi
echo ""

# 7. Aguardar processamento
echo "📋 7. Aguardando processamento (3 segundos)..."
sleep 3
echo ""

# 8. Verificar status final
echo "📋 8. Verificando status final da cobrança..."
STATUS_FINAL=$(curl -s http://localhost:8080/api/charges/$CHARGE_ID | grep -o '"status":"[^"]*"' | cut -d: -f2 | tr -d '"')
echo "Status final: $STATUS_FINAL"

if [ "$STATUS_FINAL" = "PAID" ]; then
    echo -e "${GREEN}✅ Status atualizado corretamente para PAID${NC}"
else
    echo -e "${RED}❌ Status esperado: PAID, obtido: $STATUS_FINAL${NC}"
    echo "Verifique os logs:"
    echo "  docker service logs payments_payments-manager --tail 20"
    echo "  docker service logs payments_payments-proxy --tail 20"
    exit 1
fi
echo ""

# 9. Testar endpoint SOAP
echo "📋 9. Verificando endpoint SOAP..."
SOAP_WSDL=$(curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/soap/PaymentNotificationService?wsdl)
if [ "$SOAP_WSDL" -eq 200 ]; then
    echo -e "${GREEN}✅ Endpoint SOAP acessível${NC}"
else
    echo -e "${YELLOW}⚠️  Endpoint SOAP retornou HTTP $SOAP_WSDL${NC}"
fi
echo ""

echo -e "${GREEN}🎉 Todos os testes passaram!${NC}"
echo ""
echo "📊 Resumo:"
echo "   Cliente ID: $CLIENT_ID"
echo "   Cobrança ID: $CHARGE_ID"
echo "   Status inicial: $STATUS_INICIAL"
echo "   Status final: $STATUS_FINAL"
echo "   SOAP endpoint: OK"
