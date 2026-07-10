기본 정보

작성자: 박진우

작성일: 2026-07-09

관련 기능: JWT 인증 연동, Trade-Portfolio-CashHistory 연동, 프론트 API 연동

관련 브랜치 또는 PR: feature/trade

⸻

1. 구현 기능

* JWT 기반 사용자 인증을 Trade 기능에 연동
* TradeService에서 하드코딩된 User ID 제거
* Trade, Portfolio, User Cash를 하나의 거래 흐름으로 연동
* @Transactional을 적용하여 거래의 원자성 보장
* Swagger JWT 인증 설정 추가
* React 프론트와 API 연동 완료
* 로그인, 회원가입, 거래내역, 포트폴리오 조회 및 매수/매도 기능 정상 동작 확인

⸻

2. 사용 기술

* Java 21
* Spring Boot
* Spring Data JPA
* JWT
* MySQL
* Swagger(OpenAPI)
* React
* Git

⸻

3. 새롭게 알게 된 점

* JWT를 이용하여 로그인한 사용자를 식별하고 비즈니스 로직에 적용하는 방법을 이해했다.
* JPA 연관관계(User, Stock)를 활용하면 객체 중심으로 비즈니스 로직을 구성할 수 있다는 점을 배웠다.
* @Transactional을 통해 여러 DB 작업을 하나의 작업 단위로 처리하여 데이터 일관성을 보장하는 방법을 이해했다.
* 프론트와 백엔드를 실제 연동하면서 API 명세와 DTO 구조의 중요성을 체감했다.

⸻

4. 어려웠던 점

* UserId 기반 구조를 JPA 연관관계(User, Stock) 기반 구조로 변경하면서 Repository와 Service를 함께 수정해야 했다.
* Git Merge Conflict를 해결하며 서로 다른 구조의 코드를 통합하는 과정을 경험했다.
* 프론트 연동 과정에서 Header 및 JWT 전달 방식을 맞추는 과정에서 여러 오류를 해결했다.

⸻

5. 개선 사항

* Ranking API를 프론트와 연동
* StockPriceHistory 기능 구현 및 차트 Mock 제거
* 테스트 코드(JUnit, Mockito) 작성
* UI/UX 개선 및 예외 처리 메시지 보완
* README 및 API 문서 정리

⸻

6. 다음 목표

* 테스트 코드 작성
* 프로젝트 배포 및 문서화

⸻

회의 때 설명할 내용

* Trade 기능을 JWT 기반 사용자 인증과 연동하여 하드코딩을 제거했습니다.
* @Transactional을 적용하여 거래 과정에서 데이터 일관성을 보장하도록 개선했습니다.
