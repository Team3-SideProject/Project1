# 엔드포인트 목록

| Method | Endpoint | 설명 |
| --- | --- | --- |
| POST | `/api/auth/signup` | 회원가입 |
| POST | `/api/auth/login` | 로그인 |
| GET | `/api/auth/me` | 내 정보 조회 |
| GET | `/api/stocks` | 주식 목록 조회 |
| GET | `/api/stocks/{stockId}` | 주식 상세 조회 |
| GET | `/api/stocks/{stockId}/prices` | 가격 히스토리 조회 |
| POST | `/api/trades/buy` | 매수 |
| POST | `/api/trades/sell` | 매도 |
| GET | `/api/trades/me` | 내 거래 내역 조회 |
| GET | `/api/portfolio/me` | 내 포트폴리오 조회 |
| GET | `/api/rankings/assets` | 자산 랭킹 조회 |
