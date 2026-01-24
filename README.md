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

Rodar no charge-manager/charge-proxy:
mvn clean package -DskipTests

Inicializar o Docker Swarm:
docker swarm init

Subir a stack da aplicação:
docker stack deploy -c stack.yml pagamentos

Verificar se os serviços estão rodando:
docker service ls




