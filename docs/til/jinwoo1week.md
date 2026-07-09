

## 기본 정보

- 작성자: 박진우
- 작성일: 2026-07-02
- 관련 기능: Trade(매수/매도) 구현 및 Spring Boot 구조 이해
- 관련 브랜치 또는 PR: feature/trade

---

## 1. 구현 기능

- 매수(BUY) / 매도(SELL) API 구현
- Trade 로직 공통 메서드로 리팩토링
- 수량 검증 및 예외 처리 추가
- User, Portfolio 연동을 위한 구조 설계

---

## 2. 사용 기술

- Java 21
- Spring Boot
- Spring Data JPA
- MySQL
- Swagger(OpenAPI)
- Git

---

## 3. 새롭게 알게 된 점

- Spring Boot의 MVC 구조와 Controller, Service, Repository의 역할을 이해했다.
- DTO는 데이터를 전달하고, Entity는 비즈니스 로직을 포함하는 객체라는 점을 이해했다.
- 중복되는 매수/매도 로직을 공통 메서드로 분리하여 유지보수성을 높일 수 있었다.
- User와 Portfolio가 자신의 상태를 직접 변경하도록 설계하는 것이 객체지향적인 방식이라는 것을 배웠다.

---

## 4. 어려웠던 점

- 프로젝트 구조와 계층 간의 역할을 이해하는 데 시간이 걸렸다.
- Git Merge Conflict를 해결하면서 협업 과정에서 코드 충돌을 경험했다.

---

## 5. 개선 사항

- User 현금 차감/증가 로직 연동
- Portfolio 수량 증가/감소 로직 구현
- TradeService 리팩토링 및 테스트 코드 작성
- Entity 내부의 비즈니스 로직 추가

---

## 6. 다음 목표

- User, Portfolio와 Trade 연동 완료
- Portfolio 조회 API 구현
- 객체지향적인 Entity 설계 보완

---

## 회의 때 설명할 내용

1. 매수/매도 공통 로직을 분리하여 코드 중복을 제거했습니다.
2. Spring Boot의 MVC 구조와 각 계층의 역할을 이해하며 구현했습니다.
3. 다음 단계는 User, Portfolio와 연동하여 실제 거래 흐름을 완성할 예정입니다.
```