# 기본 정보

---

작성자 : 안상민

작성일 : 2026/07/01

관련 기능 : 포트폴리오, 랭킹

관련 브랜치 또는 PR : feature/portfolio

# 1. 구현 기능

---

- portfolio, ranking 관련 코드 뼈대 작성
- 로그인 한 유저의 포트폴리오 조회 기능 구현

# 2. 사용 기능

---

- Java 21
- Spring Boot
- Spring Data JPA
- MySQL

# 3. 새롭게 알게 된 점

---

- 스프링부트의 동작 과정

```java
프로그램 실행
↓
@SpringBootApplication 발견
↓
컴포넌트 스캔
(@Controller, @Service, @Repository 등)
↓
Bean(Spring이 생성하고 관리하는 객체) 생성 및 등록
↓
의존성 주입(DI)  = private final ~  + @RequiredArgsConstructor
↓
내장 톰캣 실행
↓
사용자의 요청 대기
↓
요청 발생
↓
Controller
↓
Service
↓
Repository(JPA/MyBatis)
↓
DB
↓
결과 반환(JSON)
```

- Mybatis때는 Mapper로 일일이 함수에 SQL문 링크시켜서 했었는데 JPA는 JpaRepository에서 기본적인 메서드를 제공해줌

# 4. 어려웠던 점

---

service코드에 작성한 기능 구현 할 때 주석으로 정리는 했는데 막상 코드로 하려니 어려워서 AI의 도움을 받음..

# 5. 개선 사항

---

- 예외 처리 만들기
- 메서드 분리
- N+1 문제 해결

# 6. 다음 목표

---

- 개선 사항 반영
- Ranking 기능 구현

