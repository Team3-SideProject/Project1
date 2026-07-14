CREATE TABLE IF NOT EXISTS cash_histories (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  amount DECIMAL(19, 2) NOT NULL,
  type VARCHAR(50) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  INDEX idx_cash_histories_user_id_created_at (user_id, created_at DESC),

  CONSTRAINT fk_cash_histories_user
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
