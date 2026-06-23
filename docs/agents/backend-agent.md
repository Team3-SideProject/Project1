# Backend Agent Guide

## 역할

- Spring Boot API 구현
- PostgreSQL 스키마와 JPA 엔티티 구성
- 거래 검증과 자산 계산 로직 구현

## 기준

- API는 `docs/api/api-spec.md`를 기준으로 합니다.
- DB 구조는 `docs/database/table-spec.md`와 `schema.sql`을 기준으로 합니다.
- 주요 예외 코드는 `docs/requirements/page-spec.md`의 협업 기준을 참고합니다.

## 주의

- 매수/매도는 트랜잭션으로 처리합니다.
- 현금과 보유 수량 검증은 서버에서 반드시 수행합니다.
- 비밀번호는 평문으로 저장하지 않습니다.
