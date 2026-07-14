-- Existing Aiven DB patch for fields added after the initial SQL draft.
-- This script is safe to run more than once on MySQL 8.

SET @add_refresh_token_sql = (
  SELECT IF(
    COUNT(*) = 0,
    'ALTER TABLE users ADD COLUMN refresh_token VARCHAR(500) NULL',
    'SELECT ''users.refresh_token already exists'' AS message'
  )
  FROM information_schema.columns
  WHERE table_schema = DATABASE()
    AND table_name = 'users'
    AND column_name = 'refresh_token'
);

PREPARE add_refresh_token_stmt FROM @add_refresh_token_sql;
EXECUTE add_refresh_token_stmt;
DEALLOCATE PREPARE add_refresh_token_stmt;

SET @rename_reason_sql = (
  SELECT IF(
    EXISTS (
      SELECT 1
      FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'cash_histories'
        AND column_name = 'reason'
    )
    AND NOT EXISTS (
      SELECT 1
      FROM information_schema.columns
      WHERE table_schema = DATABASE()
        AND table_name = 'cash_histories'
        AND column_name = 'type'
    ),
    'ALTER TABLE cash_histories CHANGE COLUMN reason type VARCHAR(50) NOT NULL',
    'SELECT ''cash_histories.reason rename skipped'' AS message'
  )
);

PREPARE rename_reason_stmt FROM @rename_reason_sql;
EXECUTE rename_reason_stmt;
DEALLOCATE PREPARE rename_reason_stmt;

SET @add_cash_history_type_sql = (
  SELECT IF(
    COUNT(*) = 0,
    'ALTER TABLE cash_histories ADD COLUMN type VARCHAR(50) NOT NULL DEFAULT ''CHARGE''',
    'SELECT ''cash_histories.type already exists'' AS message'
  )
  FROM information_schema.columns
  WHERE table_schema = DATABASE()
    AND table_name = 'cash_histories'
    AND column_name = 'type'
);

PREPARE add_cash_history_type_stmt FROM @add_cash_history_type_sql;
EXECUTE add_cash_history_type_stmt;
DEALLOCATE PREPARE add_cash_history_type_stmt;
