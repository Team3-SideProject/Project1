# Stock Simulator

생성형 AI를 활용하여 Spring Boot 백엔드 기술을 학습하고 실습하기 위한 학습형 사이드 프로젝트입니다.

주식 시뮬레이터 서비스를 구현하는 과정에서 Spring Boot, JPA, MySQL, REST API, Git 협업 등의 기술을 경험하며 백엔드 개발 역량 향상을 목표로 합니다.

## 프로젝트 개요

- 프로젝트명: StockSim
- 개발 인원: 4명
- 개발 기간: 1개월
- 작업 시간: 평일 하루 1~2시간
- 프로젝트 성격: 백엔드 부트캠프 학습형 팀 프로젝트

## 프로젝트 목표

- Spring Boot 기반 웹 애플리케이션 구조 이해
- REST API 설계 및 구현 경험
- JPA 및 MySQL 활용 경험
- Git 협업 및 코드 리뷰 경험
- 생성형 AI를 활용한 개발 생산성 향상
- 구현 과정에 대한 학습 내용 기록 및 공유

## 개발 방향

### AI 활용 영역

- 프론트엔드 초안 제작
- 데이터베이스 설계 초안
- API 명세 작성
- 문서 작성 및 정리
- 오류 분석 및 코드 설명

### 직접 구현 영역

- Controller
- Service
- Repository
- 비즈니스 로직
- 예외 처리
- 데이터 검증
- API 설계

## 협업 규칙

1. AI는 학습과 설명을 위한 도구로 사용합니다.
2. 핵심 비즈니스 로직은 직접 구현합니다.
3. 이해하지 못한 코드는 커밋하지 않습니다.
4. AI가 생성한 코드는 직접 입력하고 분석합니다.
5. 구현한 기능은 금요일 회의에서 설명합니다.
6. 구현 과정과 학습 내용을 문서로 기록합니다.

## 개발 프로세스

1. 기능 선정
2. 로직 설계
3. AI를 통한 개념 학습
4. 직접 구현
5. 코드 리뷰 및 설명
6. TIL 작성

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
- 거래 내역

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

- React
- TypeScript
- Vite
- React Router
- Axios 또는 Fetch API
- AI 기반 UI 초안

### Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA

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
## 개발 우선순위

1. 프로젝트 초기 세팅
2. 회원가입/로그인 구현
3. 주식 9개 초기 데이터 구성
4. 30분 단위 주가 변경 로직 구현
5. 메인페이지 차트 구현
6. 매수/매도 기능 구현
7. 마이페이지 포트폴리오 구현
8. 자산 랭킹 구현
9. 구현 내용 및 학습 내용 문서화

## 학습 기록

각 기능 구현 후 다음 내용을 기록합니다.

- 구현 내용
- 사용 기술
- 새롭게 알게 된 점
- 어려웠던 점
- 개선 사항

이를 통해 프로젝트 결과물뿐 아니라 성장 과정 또한 기록합니다.

## 최종 목표

단순한 주식 시뮬레이터 제작이 아니라, AI를 활용하여 백엔드 개발 역량을 향상시키고 실무에 가까운 개발 경험을 쌓기 위한 학습형 프로젝트를 목표로 합니다.

## 문서

- [프로젝트 기획서](docs/requirements/project-plan.md)
- [페이지 스펙](docs/requirements/page-spec.md)
- [기능 명세](docs/requirements/feature-spec.md)
- [API 명세](docs/api/api-spec.md)
- [DB 테이블 명세](docs/database/table-spec.md)

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

현재 간이 프론트 프로토타입은 `frontend` 폴더에 구성되어 있습니다.

```bash
cd frontend
npm install
npm run dev
```

브라우저에서 `http://127.0.0.1:5173/`로 접속합니다.

```bash
# backend
cd backend
./gradlew bootRun
```

## 라이선스

개인 사이드 프로젝트 용도로 시작합니다.
