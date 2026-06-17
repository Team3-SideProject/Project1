# Stock Simulator

실시간 주식 시뮬레이션 웹게임 프로젝트입니다.  
유저는 가상의 자산으로 여러 종목을 매수/매도하고, 30분 단위로 변동되는 주가를 보며 다른 유저들과 자산 랭킹을 경쟁할 수 있습니다.

## 프로젝트 목표

- 실제 투자와 비슷한 흐름을 가진 가상 주식 거래 경험 제공
- 30분 단위로 변동되는 주가 차트 제공
- 매수/매도, 보유 자산, 수익률, 랭킹 등 핵심 투자 게임 기능 구현
- 실시간 채팅을 통해 유저 간 소통 가능한 게임성 강화

## MVP 기능

### 유저 기능

- 회원가입
- 로그인
- 로그아웃
- 마이페이지에서 내 자산 현황 확인

### 주식 시뮬레이션

- 총 9개 종목 제공
- 종목별로 서로 다른 가격 변동폭 적용
- 주가는 30분 단위로 변경
- 실시간 차트 형태로 가격 흐름 표시
- 유저는 보유 현금 내에서 매수 가능
- 유저는 보유 주식 수량 내에서 매도 가능

### 거래 기능

- 종목 선택
- 현재가 확인
- 매수 수량 입력
- 매도 수량 입력
- 매수/매도 결과 반영
- 보유 현금, 보유 주식, 평가 자산 갱신

### 실시간 채팅

- 로그인한 유저 간 실시간 채팅
- 메인페이지에서 채팅창 제공
- 닉네임과 메시지 표시

### 랭킹

- 전체 유저 자산 랭킹 조회
- 총 자산 기준 정렬
- 총 자산 = 보유 현금 + 보유 주식 평가 금액

## 페이지 구성

### 로그인/회원가입 페이지

- 이메일 또는 아이디 입력
- 비밀번호 입력
- 회원가입
- 로그인

### 메인페이지

- 주식 목록
- 선택한 종목의 실시간 차트
- 현재가
- 매수/매도 패널
- 내 보유 현금
- 내 보유 종목
- 실시간 채팅

### 마이페이지

- 내 기본 정보
- 보유 현금
- 보유 주식 목록
- 종목별 보유 수량
- 종목별 평균 매수가
- 현재 평가 금액
- 총 자산
- 수익률

### 랭킹 페이지

- 유저별 총 자산 랭킹
- 순위
- 닉네임
- 총 자산
- 수익률

## 기술 스택

### Frontend

- TypeScript
- React
- React Router
- Axios 또는 Fetch API
- 차트 라이브러리 후보
  - Recharts
  - Chart.js
  - Lightweight Charts

### Backend

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- WebSocket

### Database

- MySQL

### Infra / Tooling

- Git
- GitHub
- Docker Compose
- Gradle
- npm 또는 pnpm

## 기본 게임 규칙

- 신규 유저는 초기 가상 현금을 지급받습니다.
- 주식 가격은 30분마다 서버에서 갱신됩니다.
- 각 종목은 고유한 변동폭을 가집니다.
- 매수 시 유저의 현금이 차감되고 보유 주식 수량이 증가합니다.
- 매도 시 보유 주식 수량이 감소하고 현금이 증가합니다.
- 랭킹은 현재 보유 현금과 주식 평가 금액을 합산한 총 자산 기준으로 계산합니다.

## 종목 예시

| 종목 코드 | 종목명 | 변동성 |
| --- | --- | --- |
| ALP | Alpha Tech | 낮음 |
| BET | Beta Bio | 높음 |
| CRN | Crown Energy | 중간 |
| DLT | Delta Mobility | 중간 |
| ECH | Echo Games | 높음 |
| FRN | Front Finance | 낮음 |
| GLD | Gold Retail | 낮음 |
| HYP | Hyper AI | 높음 |
| IVY | Ivy Foods | 중간 |

## 주요 도메인

### User

- id
- email
- password
- nickname
- cash
- createdAt

### Stock

- id
- code
- name
- currentPrice
- volatility
- createdAt
- updatedAt

### StockPriceHistory

- id
- stockId
- price
- recordedAt

### Portfolio

- id
- userId
- stockId
- quantity
- averageBuyPrice

### Trade

- id
- userId
- stockId
- type
- quantity
- price
- totalAmount
- createdAt

### ChatMessage

- id
- userId
- nickname
- message
- createdAt

## API 초안

### Auth

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/api/auth/signup` | 회원가입 |
| POST | `/api/auth/login` | 로그인 |
| POST | `/api/auth/logout` | 로그아웃 |

### Stocks

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/stocks` | 전체 종목 조회 |
| GET | `/api/stocks/{stockId}` | 종목 상세 조회 |
| GET | `/api/stocks/{stockId}/prices` | 종목 가격 히스토리 조회 |

### Trades

| Method | Endpoint | Description |
| --- | --- | --- |
| POST | `/api/trades/buy` | 주식 매수 |
| POST | `/api/trades/sell` | 주식 매도 |
| GET | `/api/trades/me` | 내 거래 내역 조회 |

### Portfolio

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/portfolio/me` | 내 포트폴리오 조회 |

### Ranking

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/rankings/assets` | 총 자산 랭킹 조회 |

### Chat

| Type | Endpoint | Description |
| --- | --- | --- |
| WebSocket | `/ws/chat` | 실시간 채팅 연결 |

## 개발 우선순위

1. 프로젝트 초기 세팅
2. 회원가입/로그인 구현
3. 주식 9개 초기 데이터 구성
4. 30분 단위 주가 변경 로직 구현
5. 메인페이지 차트 구현
6. 매수/매도 기능 구현
7. 마이페이지 포트폴리오 구현
8. 자산 랭킹 구현
9. 실시간 채팅 구현

## 문서

- [프로젝트 기획서](docs/PROJECT_PLAN.md)
- [MVP 명세서](docs/MVP_SPEC.md)

## 향후 확장 아이디어

- 일간/주간/월간 랭킹
- 종목별 뉴스 이벤트
- 급등/급락 이벤트
- 거래 수수료
- 지정가 주문
- 친구 기능
- 업적 시스템
- 시즌제 랭킹

## 실행 방법

프로젝트 구조와 실행 스크립트가 확정되면 아래 내용을 업데이트합니다.

```bash
# frontend
cd frontend
npm install
npm run dev
```

```bash
# backend
cd backend
./gradlew bootRun
```

## 라이선스

개인 사이드 프로젝트 용도로 시작합니다.
