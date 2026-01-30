# Documentação da API - Sistema de Pagamentos (DAC)

Este documento descreve os endpoints disponíveis no sistema para gerenciamento de clientes, cobranças e notificações de pagamento.

**Base URL (Manager):** `http://127.0.0.1:8080`
**Base URL (Proxy):** `http://127.0.0.1:8081`

---

## 1. Clientes (Manager)

### Criar Cliente
Cria um novo cliente no banco de dados local e registra no Asaas (via Proxy).
*   **URL:** `/api/clients`
*   **Método:** `POST`
*   **Body (JSON):**
    ```json
    {
      "name": "Nome do Cliente",
      "email": "cliente@email.com",
      "cpf": "11122233344"
    }
    ```

### Listar Clientes
Retorna todos os clientes cadastrados.
*   **URL:** `/api/clients`
*   **Método:** `GET`

---

## 2. Cobranças (Manager)

### Criar Cobrança (Charge)
Gera uma nova cobrança para um cliente existente.
*   **URL:** `/api/charges`
*   **Método:** `POST`
*   **Body (JSON):**
    ```json
    {
      "clientId": 1,
      "billingType": "BOLETO",
      "value": 100.00,
      "dueDate": "2026-12-31"
    }
    ```
    *   *Nota: O `billingType` pode ser `BOLETO`, `CREDIT_CARD` ou `PIX`.*

### Listar Cobranças
Retorna o histórico de todas as cobranças e seus status.
*   **URL:** `/api/charges`
*   **Método:** `GET`

---

## 3. Webhook / Confirmação de Pagamento (Proxy)

### Simular Pagamento (Webhook Asaas)
Endpoint que recebe notificações do Asaas. Usado para confirmar pagamentos.
*   **URL:** `http://127.0.0.1:8081/webhook/asaas`
*   **Método:** `POST`
*   **Headers:**
    *   `Authorization`: `Bearer e8f9a1c4-asaas-webhook-secret`
*   **Body (JSON):**
    ```json
    {
      "event": "PAYMENT_CONFIRMED",
      "payment": {
        "id": "ID_DO_ASAAS_AQUI",
        "status": "CONFIRMED",
        "value": 100.00
      }
    }
    ```

---

## Log de Eventos (Observabilidade)
O sistema utiliza **Spring Events** para monitorar ações importantes. Verifique os logs do container do Manager para visualizar eventos como:
*   `MANAGER-EVENT -> Cliente Criado`
*   `MANAGER-EVENT -> Cobrança Criada`
*   `EMAIL-MOCK -> Pagamento confirmado` (Reação ao evento de confirmação)
