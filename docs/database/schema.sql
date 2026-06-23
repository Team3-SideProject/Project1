CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  nickname VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  cash NUMERIC(15, 2) NOT NULL DEFAULT 10000000,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stocks (
  id BIGSERIAL PRIMARY KEY,
  code VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(100) NOT NULL,
  description TEXT,
  current_price NUMERIC(15, 2) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stock_price_histories (
  id BIGSERIAL PRIMARY KEY,
  stock_id BIGINT NOT NULL REFERENCES stocks(id),
  price NUMERIC(15, 2) NOT NULL CHECK (price > 0),
  recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE portfolios (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id),
  stock_id BIGINT NOT NULL REFERENCES stocks(id),
  quantity INTEGER NOT NULL CHECK (quantity >= 0),
  average_buy_price NUMERIC(15, 2) NOT NULL CHECK (average_buy_price >= 0),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE (user_id, stock_id)
);

CREATE TABLE trades (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id),
  stock_id BIGINT NOT NULL REFERENCES stocks(id),
  trade_type VARCHAR(10) NOT NULL CHECK (trade_type IN ('BUY', 'SELL')),
  quantity INTEGER NOT NULL CHECK (quantity > 0),
  price NUMERIC(15, 2) NOT NULL CHECK (price > 0),
  total_amount NUMERIC(15, 2) NOT NULL CHECK (total_amount > 0),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_stock_price_histories_stock_id_recorded_at
  ON stock_price_histories (stock_id, recorded_at);

CREATE INDEX idx_portfolios_user_id
  ON portfolios (user_id);

CREATE INDEX idx_trades_user_id_created_at
  ON trades (user_id, created_at DESC);

INSERT INTO stocks (code, name, description, current_price) VALUES
  ('ALP', 'Alpha Tech', '안정적인 기술주', 50000),
  ('BET', 'Beta Bio', '변동성이 큰 바이오주', 12000),
  ('CRN', 'Crown Energy', '에너지 관련 종목', 30000),
  ('DLT', 'Delta Mobility', '모빌리티 관련 종목', 24000),
  ('ECH', 'Echo Games', '게임/엔터 관련 종목', 8000),
  ('FRN', 'Front Finance', '금융 관련 종목', 70000),
  ('GLD', 'Gold Retail', '리테일 관련 종목', 18000),
  ('HYP', 'Hyper AI', 'AI 성장주', 95000),
  ('IVY', 'Ivy Foods', '식품 관련 종목', 15000);
