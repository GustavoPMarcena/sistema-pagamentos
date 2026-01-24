<<<<<<< HEAD
<<<<<<< HEAD
=======
>>>>>>> 5de66a5 (Corrigindo implementação do SOAP)
# Sistema de Pagamentos (Manager + Proxy)

Este repositório tem **dois microserviços**:

- **payments-manager** (`charge-manager`) — API principal com banco (PostgreSQL) + regras de negócio
- **payments-proxy** (`charge-proxy`) — integra com ASAAS (ou modo `MOCK`) e recebe webhook

> 🧩 Objetivo: manter a mesma *lógica de fluxo* do projeto consolidado (Manager→Proxy→Webhook→Manager), mas com **nomes/rotas e estrutura próprias**.

## 1) Pré-requisitos

- Docker Desktop / Docker Engine
- (Opcional) Java 17 + Maven, se você quiser rodar sem Docker

<<<<<<< HEAD
## 2) Subir com Docker Swarm (stack.yml) — **sem docker-compose**

> ✅ Requisito da disciplina: **não adotar docker-compose**.

1. Abra o terminal na pasta raiz `sistema-pagamentos`.
=======
## 2) Subir com Docker Compose (recomendado)

1. Abra o terminal na pasta raiz `sistema-pagamentos-development`.
>>>>>>> 5de66a5 (Corrigindo implementação do SOAP)
2. Crie um `.env` baseado no exemplo:

```bash
cp .env.example .env
```

<<<<<<< HEAD
3. Inicialize o Swarm (uma vez por máquina):

```bash
docker swarm init
```

4. Faça o build das imagens (o Swarm não faz build automaticamente):

```bash
docker build -t payments/manager:1.0 ./charge-manager
docker build -t payments/proxy:1.0 ./charge-proxy
```

5. Suba a stack:

```bash
docker stack deploy -c stack.yml payments
=======
3. Suba tudo:

```bash
docker compose up --build
>>>>>>> 5de66a5 (Corrigindo implementação do SOAP)
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

<<<<<<< HEAD
## 4) Derrubar a stack

```bash
docker stack rm payments
=======
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
>>>>>>> 5de66a5 (Corrigindo implementação do SOAP)
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
<<<<<<< HEAD
=======
# Sistema de Pagamentos (DAC)

Este projeto foi desenvolvido para a disciplina **Desenvolvimento de Aplicações Corporativas (DAC)** e implementa um **Sistema de Cobranças** baseado em **arquitetura distribuída**, com separação clara de responsabilidades entre serviços, integração externa simulada (ASAAS) e infraestrutura conteinerizada com Docker.

## Visão Geral da Arquitetura

O sistema é composto por três componentes principais:

- **Charge Manager**  
  Serviço responsável pelas regras de negócio, persistência de dados, controle do ciclo de vida das cobranças e comunicação com o Proxy.

- **Charge Proxy**  
  Serviço intermediário responsável por simular a integração com o ASAAS e receber webhooks de pagamento.

- **PostgreSQL**  
  Banco de dados relacional utilizado para persistência das informações.

A comunicação entre os serviços ocorre através de uma rede Docker dedicada (`dac-net`), utilizando o nome dos serviços como hostname.


## Tecnologias Utilizadas

- Java 17
- Spring Boot
- JDBC (NamedParameterJdbcTemplate)
- Flyway
- PostgreSQL
- Docker
- Docker Swarm
- Maven
  

## Pré-requisitos

Antes de rodar o projeto, certifique-se de ter instalado:

- Docker Desktop
- Java 17 ou superior
- Maven 3.8 ou superior
- Git

Verifique com:

```bash
docker --version
java -version
mvn -version
git --version

Rodar no charge-manager e charge-proxy:
mvn clean package -DskipTests


subir as imagens docker:
docker build -t charge-manager:latest ./charge-manager
docker build -t charge-proxy:latest ./charge-proxy


Inicializar o Docker Swarm:
docker swarm init

Subir a stack da aplicação:
docker stack deploy -c stack.yml pagamentos

Verificar se os serviços estão rodando:
docker service ls





>>>>>>> 6d5fc466378213b2a0ef2098878416f793dc96a4
=======
>>>>>>> 5de66a5 (Corrigindo implementação do SOAP)
