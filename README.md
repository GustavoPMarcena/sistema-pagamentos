# Sistema de Pagamentos (Manager + Proxy)

Este repositório tem **dois microserviços**:

- **payments-manager** (`charge-manager`) — API principal com banco (PostgreSQL) + regras de negócio
- **payments-proxy** (`charge-proxy`) — integra com ASAAS (ou modo `MOCK`) e recebe webhook

> 🧩 Objetivo: manter a mesma *lógica de fluxo* do projeto consolidado (Manager→Proxy→Webhook→Manager), mas com **nomes/rotas e estrutura próprias**.

## 1) Pré-requisitos

- Docker Desktop / Docker Engine
- (Opcional) Java 17 + Maven, se você quiser rodar sem Docker

## 2) Subir com Docker Compose (recomendado)

1. Abra o terminal na pasta raiz `sistema-pagamentos-development`.
2. Crie um `.env` baseado no exemplo:

```bash
cp .env.example .env
```

3. Suba tudo:

```bash
docker compose up --build
```

Serviços:
- Manager: `http://localhost:8080`
- Proxy: `http://localhost:8081`

## 3) Testar o fluxo (sem ASAAS, modo MOCK)

### 3.1 Criar um cliente

```bash
curl -X POST http://localhost:8080/api/clients \
  -H "Content-Type: application/json" \
  -d '{"name":"Henrique","email":"henrique@email.com","cpf":"12345678900"}'
```

Você vai receber algo como:
- `id` do cliente no banco
- `asaasCustomerId` no formato `mock_customer_...`

### 3.2 Criar uma cobrança

```bash
curl -X POST http://localhost:8080/api/charges \
  -H "Content-Type: application/json" \
  -d '{"clientId":1,"value":49.90,"billingType":"PIX","dueDate":"2026-01-30"}'
```

Você deve receber `asaasPaymentId` no formato `mock_payment_...` e status `REGISTERED`.

### 3.3 Simular um webhook de pagamento confirmado

O Proxy expõe o endpoint:
- `POST http://localhost:8081/webhook/asaas`

Ele exige o header:
- `Authorization: Bearer <ASAAS_WEBHOOK_TOKEN>`

Exemplo:

```bash
curl -X POST http://localhost:8081/webhook/asaas \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer webhook-secret" \
  -d '{
    "event":"PAYMENT_CONFIRMED",
    "payment": {"id":"mock_payment_SEU_ID","customer":"mock_customer_X","value":49.90,"status":"CONFIRMED"}
  }'
```

Depois disso, o Manager recebe a notificação internamente e marca a cobrança como **PAID** (Observer/Event).

> Dica: pegue o `asaasPaymentId` retornado na criação da cobrança e coloque no payload do webhook.

### 3.4 Consultar a cobrança

```bash
curl http://localhost:8080/api/charges/1
```

## 4) Rodar com Docker Swarm (stack.yml)

> Só use se você já estiver usando Swarm.

```bash
docker swarm init
cp .env.example .env
# edite o .env se quiser

# build das imagens (Swarm não faz build automaticamente)
docker build -t payments/manager:1.0 ./charge-manager
docker build -t payments/proxy:1.0 ./charge-proxy

docker stack deploy -c stack.yml payments
```

## 5) Por que o seu projeto “não consolidava” como o outro?

Principais pontos corrigidos aqui:

- **Config/portas e nomes coerentes**: Manager 8080, Proxy 8081, URLs dentro da rede Docker
- **Proxy com endpoints internos** (`/internal/*`) para o Manager consumir (sem gambiarras de SOAP/localhost)
- **Integração ASAAS correta** via header `Authorization: Bearer <token>`
- **Modo MOCK** para testar sem depender de ASAAS
- **Webhook protegido por token** e notificação segura do Proxy → Manager
- **Banco + migração Flyway** para deixar o Manager realmente persistente

---

Se quiser, eu também posso te passar uma coleção do Postman (JSON) com as 3 requisições prontas.


## Rodar no Windows com Docker Swarm (recomendado)

> ⚠️ Este projeto **NÃO usa docker compose**. O deploy é via **Docker Swarm** (`stack.yml`), conforme o requisito do professor.

### 1) Pré-requisitos
- Docker Desktop (com WSL2)
- Java 17 + Maven (para gerar os JARs)
- (Opcional) ngrok (para webhook real do Asaas)

### 2) Build + deploy (comandos)
Abra o PowerShell **na raiz do projeto** (onde está o `stack.yml`) e execute:

```powershell
# 1) gerar os jars
./scripts/01-build-jars.ps1

# 2) build das imagens
./scripts/02-build-images.ps1

# 3) subir no swarm
./scripts/03-deploy-swarm.ps1
```

### 3) Checagens rápidas
```powershell
docker service ls
curl.exe -i http://localhost:8080/api/clients
```
- Se retornar **405 Allow: POST**, o Manager está no ar e a rota existe.

### 4) Rotas principais
**Manager (porta 8080)**
- `POST http://localhost:8080/api/clients`
- `POST http://localhost:8080/api/charges`

**Proxy (porta 8081)**
- `POST http://localhost:8081/webhook/asaas`  (endpoint que o Asaas chama)

**SOAP (porta 9090)**
- `http://localhost:9090/soap/ProxyCustomerService?wsdl`
- `http://localhost:9090/soap/ProxyPaymentService?wsdl`

## Asaas + Webhook usando ngrok (ambiente local)

> O Asaas não consegue chamar `localhost`. Para webhook real em ambiente local, use ngrok.

### 1) Subir ngrok apontando para o Proxy (8081)
Em um terminal separado:
```powershell
ngrok http 8081
```

Copie a URL HTTPS mostrada (ex: `https://xxxx.ngrok-free.app`) e configure no Asaas:
- **URL do Webhook**: `https://xxxx.ngrok-free.app/webhook/asaas`
- **Token**: use o mesmo valor de `ASAAS_WEBHOOK_TOKEN` (o Asaas envia no header `asaas-access-token`)

Dica: painel do ngrok:
- `http://127.0.0.1:4040`

### 2) Teste do fluxo
1. Crie cliente no Manager:
```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/clients" -ContentType "application/json" -Body (@{name="Henrique";email="henrique@email.com";cpf="12345678900"} | ConvertTo-Json)
```

2. Crie cobrança no Manager (exemplo):
```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/charges" -ContentType "application/json" -Body (@{clientId=1;value=50.0;billingType="PIX";dueDate="2026-01-30"} | ConvertTo-Json)
```

3. Confirme o pagamento no Asaas Sandbox (painel) para disparar o webhook.

4. Veja logs:
```powershell
docker service logs -f payments_payments-proxy
docker service logs -f payments_payments-manager
```
