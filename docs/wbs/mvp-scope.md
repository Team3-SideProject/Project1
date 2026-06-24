# 주식 시뮬레이터 MVP 명세서

## 1. 전제 조건

### 팀/기간

- 팀원: 4명
- 기간: 1개월
- 작업 가능 시간: 평일 하루 1~2시간
- 전체 예상 작업량: 개인별 약 30~60시간, 팀 전체 약 120~240시간

### 기술 스택

- Frontend: React, TypeScript, Vite
- Backend: Java 21, Spring Boot
- Database: MySQL
- API 방식: REST API
- 인증 방식: JWT 기반 인증

### MVP 원칙

- 실제 주식 API는 사용하지 않습니다.
- 주식 데이터는 서버에 저장된 더미 데이터를 사용합니다.
- 유저는 실제 돈이 아닌 가상 자산으로만 거래합니다.
- 실시간 차트, 채팅 기능, 고급 주문 기능은 MVP 범위에서 제외합니다.
- 1개월 안에 완성 가능한 CRUD와 핵심 거래 플로우를 우선 구현합니다.

## 2. MVP 기능 범위

### 2.1 회원가입

#### 설명

사용자는 이메일, 닉네임, 비밀번호로 회원가입할 수 있습니다.

#### 기능 상세

- 이메일 입력
- 닉네임 입력
- 비밀번호 입력
- 이메일 중복 검사
- 닉네임 중복 검사
- 비밀번호 암호화 저장
- 가입 시 초기 가상 현금 지급

#### 제외 범위

- 이메일 인증
- 비밀번호 찾기
- 소셜 로그인
- 프로필 이미지

### 2.2 로그인

#### 설명

가입한 사용자는 이메일과 비밀번호로 로그인할 수 있습니다.

#### 기능 상세

- 이메일/비밀번호 로그인
- 로그인 성공 시 JWT 발급
- 인증이 필요한 API 요청 시 JWT 사용
- 로그아웃은 프론트엔드에서 토큰 삭제 방식으로 처리

#### 제외 범위

- Refresh Token
- 자동 로그인
- 로그인 이력 관리
- 계정 잠금

### 2.3 주식 목록 조회

#### 설명

사용자는 거래 가능한 더미 주식 목록을 조회할 수 있습니다.

#### 기능 상세

- 전체 주식 목록 조회
- 종목명, 종목 코드, 현재가 표시
- 전일 대비 등락률은 선택 구현 항목으로 둡니다.

#### 더미 종목 수

- MVP 기준 9개 종목

#### 제외 범위

- 실제 주식 API 연동
- 실시간 가격 변경
- 복잡한 차트 데이터

### 2.4 주식 상세 조회

#### 설명

사용자는 특정 주식의 상세 정보를 확인할 수 있습니다.

#### 기능 상세

- 종목명
- 종목 코드
- 현재가
- 간단한 종목 설명
- 최근 가격 히스토리

#### 구현 방식

- 가격 히스토리는 더미 데이터 또는 서버에서 생성한 간단한 데이터를 사용합니다.
- 차트는 선 차트 형태로 단순 표시합니다.

### 2.5 매수

#### 설명

사용자는 보유 현금 내에서 주식을 매수할 수 있습니다.

#### 기능 상세

- 종목 선택
- 매수 수량 입력
- 현재가 기준 총 매수 금액 계산
- 보유 현금 부족 여부 검증
- 매수 성공 시 보유 현금 차감
- 포트폴리오 보유 수량 증가
- 평균 매수가 갱신
- 거래 내역 저장

#### 제한 조건

- 수량은 1 이상 정수만 허용합니다.
- 보유 현금보다 큰 금액은 매수할 수 없습니다.
- MVP에서는 시장가 매수만 지원합니다.

### 2.6 매도

#### 설명

사용자는 보유 중인 주식을 매도할 수 있습니다.

#### 기능 상세

- 보유 종목 선택
- 매도 수량 입력
- 현재가 기준 총 매도 금액 계산
- 보유 수량 부족 여부 검증
- 매도 성공 시 보유 현금 증가
- 포트폴리오 보유 수량 감소
- 거래 내역 저장
- 보유 수량이 0이 되면 포트폴리오에서 제거하거나 0으로 유지합니다.

#### 제한 조건

- 수량은 1 이상 정수만 허용합니다.
- 보유 수량보다 큰 수량은 매도할 수 없습니다.
- MVP에서는 시장가 매도만 지원합니다.

### 2.7 포트폴리오 조회

#### 설명

사용자는 현재 자신의 보유 현금과 보유 주식을 확인할 수 있습니다.

#### 기능 상세

- 보유 현금
- 보유 주식 목록
- 종목별 보유 수량
- 종목별 평균 매수가
- 종목별 현재가
- 종목별 평가 금액
- 종목별 평가 손익
- 총 평가 금액
- 총 자산

#### 계산 기준

- 종목 평가 금액 = 현재가 * 보유 수량
- 총 주식 평가 금액 = 모든 종목 평가 금액 합계
- 총 자산 = 보유 현금 + 총 주식 평가 금액
- 평가 손익 = 평가 금액 - 매수 원금

### 2.8 거래 내역 조회

#### 설명

사용자는 자신의 매수/매도 내역을 조회할 수 있습니다.

#### 기능 상세

- 거래 유형
- 종목명
- 종목 코드
- 거래 수량
- 거래 가격
- 총 거래 금액
- 거래 일시

#### MVP 기준

- 내 거래 내역만 조회합니다.
- 최신순 정렬을 기본으로 합니다.
- 페이지네이션은 선택 구현 항목으로 둡니다.

### 2.9 랭킹

#### 설명

사용자는 전체 유저의 총 자산 순위를 확인할 수 있습니다.

#### 기능 상세

- 총 자산 기준 내림차순 정렬
- 순위
- 닉네임
- 보유 현금
- 주식 평가 금액
- 총 자산

#### MVP 기준

- 요청 시점에 총 자산을 계산합니다.
- 상위 20명만 표시합니다.
- 동점 처리, 시즌제, 기간별 랭킹은 제외합니다.

## 3. MVP 제외 범위

아래 기능은 1개월 MVP 범위에서는 제외합니다.

- 실제 주식 API 연동
- 실시간 주가 반영
- 채팅 기능
- 지정가 주문
- 예약 주문
- 거래 수수료
- 관리자 페이지
- 친구 기능
- 알림 기능
- 소셜 로그인
- 이메일 인증
- 비밀번호 찾기
- Refresh Token
- 모바일 앱
- 배포 자동화 고도화

## 4. 화면 목록

### 4.1 회원가입 화면

#### URL

`/signup`

#### 주요 요소

- 이메일 입력
- 닉네임 입력
- 비밀번호 입력
- 비밀번호 확인 입력
- 회원가입 버튼
- 로그인 화면 이동 링크

#### 주요 검증

- 이메일 형식
- 필수값 입력 여부
- 비밀번호와 비밀번호 확인 일치 여부
- 중복 이메일/닉네임 오류 표시

### 4.2 로그인 화면

#### URL

`/login`

#### 주요 요소

- 이메일 입력
- 비밀번호 입력
- 로그인 버튼
- 회원가입 화면 이동 링크

#### 주요 검증

- 필수값 입력 여부
- 로그인 실패 메시지 표시

### 4.3 주식 목록/메인 화면

#### URL

`/`

#### 주요 요소

- 상단 내비게이션
- 내 보유 현금 요약
- 주식 목록 테이블
- 종목명
- 종목 코드
- 현재가
- 상세 보기 버튼
- 매수 버튼

#### 주요 행동

- 주식 목록 조회
- 주식 상세 화면 이동
- 매수 화면 또는 매수 모달 열기

### 4.4 주식 상세 화면

#### URL

`/stocks/:stockId`

#### 주요 요소

- 종목명
- 종목 코드
- 현재가
- 종목 설명
- 간단한 가격 차트
- 매수 입력 영역
- 매도 입력 영역

#### 주요 행동

- 수량 입력
- 예상 거래 금액 확인
- 매수 요청
- 매도 요청

### 4.5 포트폴리오 화면

#### URL

`/portfolio`

#### 주요 요소

- 보유 현금
- 총 주식 평가 금액
- 총 자산
- 보유 종목 테이블
- 종목별 수량
- 평균 매수가
- 현재가
- 평가 금액
- 손익

#### 주요 행동

- 보유 종목 확인
- 종목 상세 화면 이동
- 매도 진행

### 4.6 거래 내역 화면

#### URL

`/trades`

#### 주요 요소

- 거래 내역 테이블
- 거래 유형
- 종목명
- 수량
- 가격
- 총 금액
- 거래 일시

#### 주요 행동

- 내 거래 내역 확인
- 최신순 조회

### 4.7 랭킹 화면

#### URL

`/ranking`

#### 주요 요소

- 랭킹 테이블
- 순위
- 닉네임
- 보유 현금
- 주식 평가 금액
- 총 자산

#### 주요 행동

- 상위 유저 랭킹 확인
- 내 순위 확인은 선택 구현 항목으로 둡니다.

## 5. API 목록

### 5.1 Auth API

| Method | Endpoint | 인증 | 설명 |
| --- | --- | --- | --- |
| POST | `/api/auth/signup` | 불필요 | 회원가입 |
| POST | `/api/auth/login` | 불필요 | 로그인 |
| GET | `/api/auth/me` | 필요 | 내 정보 조회 |

#### POST `/api/auth/signup`

Request

```json
{
  "email": "user@example.com",
  "nickname": "tester",
  "password": "password123"
}
```

Response

```json
{
  "id": 1,
  "email": "user@example.com",
  "nickname": "tester",
  "cash": 10000000
}
```

#### POST `/api/auth/login`

Request

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

Response

```json
{
  "accessToken": "jwt-token",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "nickname": "tester"
  }
}
```

### 5.2 Stock API

| Method | Endpoint | 인증 | 설명 |
| --- | --- | --- | --- |
| GET | `/api/stocks` | 필요 | 주식 목록 조회 |
| GET | `/api/stocks/{stockId}` | 필요 | 주식 상세 조회 |
| GET | `/api/stocks/{stockId}/prices` | 필요 | 주식 가격 히스토리 조회 |

#### GET `/api/stocks`

Response

```json
[
  {
    "id": 1,
    "code": "ALP",
    "name": "Alpha Tech",
    "currentPrice": 50000
  }
]
```

#### GET `/api/stocks/{stockId}`

Response

```json
{
  "id": 1,
  "code": "ALP",
  "name": "Alpha Tech",
  "description": "안정적인 기술주",
  "currentPrice": 50000
}
```

### 5.3 Trade API

| Method | Endpoint | 인증 | 설명 |
| --- | --- | --- | --- |
| POST | `/api/trades/buy` | 필요 | 매수 |
| POST | `/api/trades/sell` | 필요 | 매도 |
| GET | `/api/trades/me` | 필요 | 내 거래 내역 조회 |

#### POST `/api/trades/buy`

Request

```json
{
  "stockId": 1,
  "quantity": 3
}
```

Response

```json
{
  "tradeId": 1,
  "type": "BUY",
  "stockId": 1,
  "quantity": 3,
  "price": 50000,
  "totalAmount": 150000,
  "cashAfterTrade": 9850000
}
```

#### POST `/api/trades/sell`

Request

```json
{
  "stockId": 1,
  "quantity": 2
}
```

Response

```json
{
  "tradeId": 2,
  "type": "SELL",
  "stockId": 1,
  "quantity": 2,
  "price": 52000,
  "totalAmount": 104000,
  "cashAfterTrade": 9954000
}
```

### 5.4 Portfolio API

| Method | Endpoint | 인증 | 설명 |
| --- | --- | --- | --- |
| GET | `/api/portfolio/me` | 필요 | 내 포트폴리오 조회 |

#### GET `/api/portfolio/me`

Response

```json
{
  "cash": 9850000,
  "stockValue": 150000,
  "totalAsset": 10000000,
  "holdings": [
    {
      "stockId": 1,
      "code": "ALP",
      "name": "Alpha Tech",
      "quantity": 3,
      "averageBuyPrice": 50000,
      "currentPrice": 50000,
      "valuation": 150000,
      "profitLoss": 0
    }
  ]
}
```

### 5.5 Ranking API

| Method | Endpoint | 인증 | 설명 |
| --- | --- | --- | --- |
| GET | `/api/rankings/assets` | 필요 | 자산 랭킹 조회 |

#### GET `/api/rankings/assets`

Response

```json
[
  {
    "rank": 1,
    "nickname": "tester",
    "cash": 9850000,
    "stockValue": 150000,
    "totalAsset": 10000000
  }
]
```

## 6. DB 테이블 초안

### 6.1 users

| 컬럼 | 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGSERIAL | PK | 유저 ID |
| email | VARCHAR(255) | UNIQUE, NOT NULL | 이메일 |
| nickname | VARCHAR(50) | UNIQUE, NOT NULL | 닉네임 |
| password | VARCHAR(255) | NOT NULL | 암호화된 비밀번호 |
| cash | NUMERIC(15, 2) | NOT NULL | 보유 현금 |
| created_at | TIMESTAMP | NOT NULL | 생성일 |
| updated_at | TIMESTAMP | NOT NULL | 수정일 |

### 6.2 stocks

| 컬럼 | 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGSERIAL | PK | 주식 ID |
| code | VARCHAR(20) | UNIQUE, NOT NULL | 종목 코드 |
| name | VARCHAR(100) | NOT NULL | 종목명 |
| description | TEXT |  | 종목 설명 |
| current_price | NUMERIC(15, 2) | NOT NULL | 현재가 |
| created_at | TIMESTAMP | NOT NULL | 생성일 |
| updated_at | TIMESTAMP | NOT NULL | 수정일 |

### 6.3 stock_price_histories

| 컬럼 | 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGSERIAL | PK | 가격 히스토리 ID |
| stock_id | BIGINT | FK, NOT NULL | 주식 ID |
| price | NUMERIC(15, 2) | NOT NULL | 가격 |
| recorded_at | TIMESTAMP | NOT NULL | 기록 시간 |

### 6.4 portfolios

| 컬럼 | 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGSERIAL | PK | 포트폴리오 ID |
| user_id | BIGINT | FK, NOT NULL | 유저 ID |
| stock_id | BIGINT | FK, NOT NULL | 주식 ID |
| quantity | INTEGER | NOT NULL | 보유 수량 |
| average_buy_price | NUMERIC(15, 2) | NOT NULL | 평균 매수가 |
| created_at | TIMESTAMP | NOT NULL | 생성일 |
| updated_at | TIMESTAMP | NOT NULL | 수정일 |

#### 제약 조건

- `(user_id, stock_id)` 유니크 제약 조건을 둡니다.
- `quantity`는 0 이상이어야 합니다.

### 6.5 trades

| 컬럼 | 타입 | 제약 조건 | 설명 |
| --- | --- | --- | --- |
| id | BIGSERIAL | PK | 거래 ID |
| user_id | BIGINT | FK, NOT NULL | 유저 ID |
| stock_id | BIGINT | FK, NOT NULL | 주식 ID |
| trade_type | VARCHAR(10) | NOT NULL | BUY 또는 SELL |
| quantity | INTEGER | NOT NULL | 거래 수량 |
| price | NUMERIC(15, 2) | NOT NULL | 거래 가격 |
| total_amount | NUMERIC(15, 2) | NOT NULL | 총 거래 금액 |
| created_at | TIMESTAMP | NOT NULL | 거래 시간 |

#### 제약 조건

- `trade_type`은 `BUY`, `SELL`만 허용합니다.
- `quantity`는 1 이상이어야 합니다.

## 7. 초기 더미 데이터

### 초기 유저 자산

- 신규 가입 유저에게 `10,000,000`원의 가상 현금을 지급합니다.

### 초기 주식 데이터

| 코드 | 종목명 | 현재가 | 설명 |
| --- | --- | --- | --- |
| ALP | Alpha Tech | 50,000 | 안정적인 기술주 |
| BET | Beta Bio | 12,000 | 변동성이 큰 바이오주 |
| CRN | Crown Energy | 30,000 | 에너지 관련 종목 |
| DLT | Delta Mobility | 24,000 | 모빌리티 관련 종목 |
| ECH | Echo Games | 8,000 | 게임/엔터 관련 종목 |
| FRN | Front Finance | 70,000 | 금융 관련 종목 |
| GLD | Gold Retail | 18,000 | 리테일 관련 종목 |
| HYP | Hyper AI | 95,000 | AI 성장주 |
| IVY | Ivy Foods | 15,000 | 식품 관련 종목 |

## 8. 1개월 개발 범위 제안

### 1주차: 프로젝트 세팅 및 인증

- 프론트엔드 Vite 프로젝트 세팅
- 백엔드 Spring Boot 프로젝트 세팅
- MySQL 연결
- 라우팅 구조 구성
- 공통 레이아웃 구성
- 회원가입 API
- 로그인 API
- JWT 인증 처리
- 회원가입 화면
- 로그인 화면

### 2주차: 주식 조회 및 거래

- 주식 더미 데이터 생성
- 주식 목록 API
- 주식 상세 API
- 주식 목록 화면
- 주식 상세 화면
- 간단한 차트 표시
- 매수 API
- 매도 API
- 포트폴리오 갱신 로직
- 매수/매도 UI
- 거래 예외 처리

### 3주차: 포트폴리오와 거래 내역

- 포트폴리오 조회 API
- 거래 내역 조회 API
- 포트폴리오 화면
- 거래 내역 화면
- 자산 계산 로직 보완
- 랭킹 API
- 랭킹 화면

### 4주차: 랭킹 및 마무리

- UI 정리
- 버그 수정
- README 업데이트
- 발표 자료 또는 시연 플로우 정리

## 9. MVP 완료 기준

- 회원가입 후 초기 가상 현금이 지급됩니다.
- 로그인 후 JWT로 인증된 API를 호출할 수 있습니다.
- 주식 9개 목록을 조회할 수 있습니다.
- 특정 주식의 상세 정보를 볼 수 있습니다.
- 보유 현금 내에서 매수할 수 있습니다.
- 보유 수량 내에서 매도할 수 있습니다.
- 포트폴리오에서 보유 현금, 보유 주식, 총 자산을 확인할 수 있습니다.
- 거래 내역에서 매수/매도 기록을 확인할 수 있습니다.
- 랭킹에서 총 자산 기준 상위 유저를 확인할 수 있습니다.
