CREATE TABLE IF NOT EXISTS stocks (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  code VARCHAR(20) NOT NULL UNIQUE,
  name VARCHAR(100) NOT NULL,
  description TEXT,
  current_price DECIMAL(15, 2) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO stocks (code, name, description, current_price) VALUES
  ('ALP', 'Alpha Tech', '안정적인 기술주', 50000.00),
  ('BET', 'Beta Bio', '변동성이 큰 바이오주', 12000.00),
  ('CRN', 'Crown Energy', '에너지 관련 종목', 30000.00),
  ('DLT', 'Delta Mobility', '모빌리티 관련 종목', 24000.00),
  ('ECH', 'Echo Games', '게임/엔터 관련 종목', 8000.00),
  ('FRN', 'Front Finance', '금융 관련 종목', 70000.00),
  ('GLD', 'Gold Retail', '리테일 관련 종목', 18000.00),
  ('HYP', 'Hyper AI', 'AI 성장주', 95000.00),
  ('IVY', 'Ivy Foods', '식품 관련 종목', 15000.00)
ON DUPLICATE KEY UPDATE
  name = VALUES(name),
  description = VALUES(description),
  current_price = VALUES(current_price);
