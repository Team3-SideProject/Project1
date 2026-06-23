# ADR 002: Spring Boot

## 상태

Accepted

## 결정

백엔드는 Spring Boot를 사용합니다.

## 이유

- Java 기반 웹 API 개발에 적합합니다.
- Spring Security, JPA, Validation 등 MVP 구현에 필요한 기능을 빠르게 사용할 수 있습니다.
- 거래 로직을 트랜잭션으로 관리하기 좋습니다.

## 결과

- 백엔드 코드는 `backend/`에 둡니다.
- REST API를 우선 구현합니다.
