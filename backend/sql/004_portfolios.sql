CREATE TABLE IF NOT EXISTS portfolios (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  stock_id BIGINT NOT NULL,
  quantity INT NOT NULL CHECK (quantity >= 0),
  average_buy_price DECIMAL(19, 2) NOT NULL CHECK (average_buy_price >= 0),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

  UNIQUE KEY uk_portfolios_user_stock (user_id, stock_id),
  INDEX idx_portfolios_user_id (user_id),
  INDEX idx_portfolios_stock_id (stock_id),

  CONSTRAINT fk_portfolios_user
    FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_portfolios_stock
    FOREIGN KEY (stock_id) REFERENCES stocks(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
