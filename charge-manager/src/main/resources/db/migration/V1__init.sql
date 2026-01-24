CREATE TABLE IF NOT EXISTS clients (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(180) NOT NULL UNIQUE,
  cpf VARCHAR(14) NOT NULL,
  asaas_customer_id VARCHAR(64),
  created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS charges (
  id BIGSERIAL PRIMARY KEY,
  client_id BIGINT NOT NULL REFERENCES clients(id),
  value NUMERIC(12,2) NOT NULL,
  billing_type VARCHAR(20) NOT NULL,
  status VARCHAR(20) NOT NULL,
  due_date DATE,
  asaas_payment_id VARCHAR(64),
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_charges_client_id ON charges(client_id);
CREATE INDEX IF NOT EXISTS idx_charges_asaas_payment_id ON charges(asaas_payment_id);
