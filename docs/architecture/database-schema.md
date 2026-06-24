# DB 테이블 명세 초안

## users

| 컬럼 | 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT AUTO_INCREMENT | PK | 유저 ID |
| email | VARCHAR(255) | UNIQUE, NOT NULL | 이메일 |
| nickname | VARCHAR(50) | UNIQUE, NOT NULL | 닉네임 |
| password | VARCHAR(255) | NOT NULL | 암호화된 비밀번호 |
| cash | DECIMAL(15, 2) | NOT NULL | 보유 현금 |
| created_at | TIMESTAMP | NOT NULL | 생성일 |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 수정일 |

## stocks

| 컬럼 | 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT AUTO_INCREMENT | PK | 주식 ID |
| code | VARCHAR(20) | UNIQUE, NOT NULL | 종목 코드 |
| name | VARCHAR(100) | NOT NULL | 종목명 |
| description | TEXT |  | 종목 설명 |
| current_price | DECIMAL(15, 2) | NOT NULL | 현재가 |
| created_at | TIMESTAMP | NOT NULL | 생성일 |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 수정일 |

## stock_price_histories

| 컬럼 | 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT AUTO_INCREMENT | PK | 가격 히스토리 ID |
| stock_id | BIGINT | FK(fk_stock_price_histories_stock), NOT NULL | 주식 ID |
| price | DECIMAL(15, 2) | NOT NULL | 기록 가격 |
| recorded_at | TIMESTAMP | NOT NULL | 기록 시간 |

## portfolios

| 컬럼 | 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT AUTO_INCREMENT | PK | 포트폴리오 ID |
| user_id | BIGINT | FK(fk_portfolios_user), NOT NULL | 유저 ID |
| stock_id | BIGINT | FK(fk_portfolios_stock), NOT NULL | 주식 ID |
| quantity | INTEGER | NOT NULL | 보유 수량 |
| average_buy_price | DECIMAL(15, 2) | NOT NULL | 평균 매수가 |
| created_at | TIMESTAMP | NOT NULL | 생성일 |
| updated_at | TIMESTAMP | NOT NULL, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP | 수정일 |

## trades

| 컬럼 | 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGINT AUTO_INCREMENT | PK | 거래 ID |
| user_id | BIGINT | FK(fk_trades_user), NOT NULL | 유저 ID |
| stock_id | BIGINT | FK(fk_trades_stock), NOT NULL | 주식 ID |
| trade_type | VARCHAR(10) | NOT NULL | BUY 또는 SELL |
| quantity | INTEGER | NOT NULL | 거래 수량 |
| price | DECIMAL(15, 2) | NOT NULL | 거래 당시 가격 |
| total_amount | DECIMAL(15, 2) | NOT NULL | 총 거래 금액 |
| created_at | TIMESTAMP | NOT NULL | 거래 시간 |
