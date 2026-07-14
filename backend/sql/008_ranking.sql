CREATE TABLE IF NOT EXISTS rankings (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  ranking INT NOT NULL,
  user_id BIGINT NOT NULL,
  nickname VARCHAR(50) NOT NULL,
  total_asset DECIMAL(19, 2) NOT NULL,
  calculated_at TIMESTAMP NOT NULL,

  INDEX idx_rankings_ranking (ranking),
  INDEX idx_rankings_user_id (user_id),

  CONSTRAINT fk_rankings_user
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
