# 아키텍처 초안

## 구성

```text
React + TypeScript + Vite
        |
        | REST API / JWT
        v
Java 21 + Spring Boot
        |
        | JPA
        v
MySQL
```

## 책임 분리

| 영역 | 책임 |
| --- | --- |
| Frontend | 화면 렌더링, 사용자 입력, API 호출, 토큰 저장 |
| Backend | 인증, 거래 검증, 자산 계산, API 응답 |
| Database | 유저, 주식, 포트폴리오, 거래 내역 저장 |

## MVP 기준

- 실시간 통신은 사용하지 않습니다.
- 주식 가격은 DB 더미 데이터를 기준으로 합니다.
- 랭킹은 요청 시점에 계산합니다.
