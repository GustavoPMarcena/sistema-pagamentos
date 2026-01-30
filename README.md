# Sistema de Pagamentos Distribuído (DAC Project)

Este projeto implementa um sistema de gestão de pagamentos distribuído, composto por dois microserviços principais (`Manager` e `Proxy`) e um banco de dados PostgreSQL. O sistema integra-se à API do **Asaas** para processamento real de boletos e PIX, utilizando uma arquitetura híbrida com comunicação REST e SOAP (JAX-WS).

## 🚀 Tecnologias Utilizadas

- **Java 17** & **Spring Boot 3**
- **Docker** & **Docker Swarm** (Orquestração de Containers)
- **PostgreSQL** (Banco de Dados Relacional)
- **JDBC Puro** (Persistência de dados sem JPA/Hibernate - Requisito [m1])
- **JAX-WS (SOAP)** (Comunicação RPC entre serviços - Requisito Arquitetural)
- **TransactionTemplate** (Controle de Transações Explícito - Requisito [m2])
- **Asaas API** (Integração com Gateway de Pagamentos Real)
- **Flyway** (Migração de Banco de Dados)
- **Ngrok** (Exposição de Webhook Local)

---

## 🏗️ Arquitetura do Sistema

O sistema é dividido em três camadas principais:

1.  **Charge Manager**: O núcleo do sistema. Gerencia clientes e cobranças, mantendo o estado no banco de dados. Comunica-se com o `Proxy` via **SOAP** para registrar operações no gateway externo.
2.  **Charge Proxy**: Atua como um intermediário (Adapter) entre o sistema interno e a API do Asaas. Recebe requisições SOAP do Manager e as traduz para chamadas REST na API do Asaas. Também recebe **Webhooks** do Asaas e notifica o Manager.
3.  **Database**: PostgreSQL rodando em container, com persistência via volumes Docker.

### Destaques da Implementação
- **Persistência Otimizada**: Uso de `NamedParameterJdbcTemplate` para consultas SQL diretas e performáticas.
- **Transações Seguras**: Controle programático de transações para garantir consistência entre banco local e gateway remoto.
- **Design Patterns**: Uso de *Observer Pattern* para desacoplar o processamento de eventos (ex: envio de e-mail após confirmação de pagamento).

---

## 🛠️ Como Executar o Projeto

### Pré-requisitos
- Docker e Docker Compose instalados.
- (Opcional) Ngrok para testar Webhooks reais.

### Passo a Passo

1.  **Configuração de Ambiente**:
    Copie o arquivo de exemplo `.env.example` para `.env` (as configurações padrão já funcionam para o modo Sandbox do Asaas com credenciais fornecidas ou Mock).

    ```bash
    cp .env.example .env
    ```

2.  **Inicializar o Swarm**:
    Se ainda não iniciou o Swarm na sua máquina:
    ```bash
    docker swarm init
    ```

3.  **Build e Deploy**:
    Utilize o script do Maven e Docker para construir e subir a stack.

    ```bash
    # Compilar os projetos (opcional se já tiver as imagens)
    cd charge-manager && ./mvnw clean package -DskipTests && cd ..
    cd charge-proxy && ./mvnw clean package -DskipTests && cd ..

    # Construir imagens Docker
    docker build -t payments/manager:latest ./charge-manager
    docker build -t payments/proxy:latest ./charge-proxy

    # Subir a Stack no Swarm
    docker stack deploy -c stack.yml payments
    ```

4.  **Verificar Status**:
    Aguarde alguns instantes e verifique se os serviços estão rodando:
    ```bash
    docker service ls
    ```

---

## 🔌 Endpoints e Testes

### 1. Criar Cliente
Cria um cliente no banco local e, via SOAP, registra-o no Asaas.

```bash
curl -X POST http://localhost:8080/api/clients \
  -H "Content-Type: application/json" \
  -d '{"name":"Cliente Teste","email":"cliente.teste@email.com","cpf":"11144477735"}'
```

### 2. Criar Cobrança (PIX ou BOLETO)
Gera uma cobrança vinculada ao cliente. O sistema notifica via e-mail automaticamente.

```bash
curl -X POST http://localhost:8080/api/charges \
  -H "Content-Type: application/json" \
  -d '{"clientId": 1, "value": 150.00, "billingType": "PIX", "dueDate": "2026-05-20"}'
```

### 3. Cancelar Cobrança
Remove a cobrança no Asaas e atualiza o status local para `CANCELED`.

```bash
curl -X POST http://localhost:8080/api/charges/{ID}/cancel
```

### 4. Consultar Cobranças
Lista todas as cobranças e seus status atuais.

```bash
curl http://localhost:8080/api/charges
```

---

## 🔔 Webhooks (Simulação Local)

Para simular o recebimento de um pagamento (caso não esteja usando Ngrok com Asaas Real):

```bash
curl -X POST "http://localhost:8081/webhook/asaas" \
  -H "asaas-access-token: e8f9a1c4-asaas-webhook-secret" \
  -H "Content-Type: application/json" \
  -d '{
    "event": "PAYMENT_CONFIRMED",
    "payment": {
      "id": "pay_SEU_ID_DO_ASAAS", 
      "status": "CONFIRMED",
      "value": 150.00
    }
  }'
```
*Substitua `pay_SEU_ID_DO_ASAAS` pelo ID retornado na criação da cobrança.*

---

## 📝 Notas de Desenvolvimento

Este projeto foi desenvolvido com foco estrito nos requisitos de **Sistemas Distribuídos**, garantindo que a comunicação entre microserviços, a persistência de dados e a consistência transacional fossem implementadas "by the book", sem abstrações excessivas de frameworks ORM, para demonstrar domínio sobre as tecnologias base (JDBC, SQL, Transações).
