기능 구현 TIL 템플릿
기능 하나를 구현한 뒤, 내가 무엇을 만들었고 무엇을 배웠는지 정리하기 위한 템플릿입니다.
모든 항목을 길게 작성할 필요는 없고, 직접 설명할 수 있을 정도로만 적습니다.
기본 정보
작성자: mac (본인 이름으로 수정 가능)
작성일: 2026년 7월 2일
관련 기능: 회원가입, 로그인 API 명세 수정 및 빌드 컴파일 에러 트러블슈팅
관련 브랜치 또는 PR: feat/auth-and-bugfix
1. 구현 기능
   이번에 구현한 기능을 간단히 적습니다.
   작성:
   회원가입 API 명세 원상복구: 기존 name 필드로 조율되던 명세를 팀 최종 스펙인 nickname으로 전면 수정하여 반영 (User 엔티티, SignUpRequest, UserResponse, AuthService).
   로그인 API 응답 Key 변경: 프론트엔드 팀의 요청에 따라 로그인 성공 시 반환되는 토큰의 JSON Key 이름을 accessToken에서 token으로 간결하게 변경 (LoginResponse, AuthController).
   프로젝트 전반의 컴파일 에러 트러블슈팅: 팀원들의 패키지 구조 변경 및 미완성 껍데기 코드로 인해 발생한 18개 이상의 대규모 빌드 에러(:compileJava FAILED)를 추적하고 전면 해결.
2. 사용 기술
   이번 기능을 구현하면서 사용한 기술을 적습니다.
   작성:
   Java 21
   Spring Boot
   Spring Data JPA
   Lombok (@Getter, @NoArgsConstructor)
   MySQL 8.0
   Gradle Build Tool
3. 새롭게 알게 된 점
   구현하면서 새로 배운 개념이나 이해한 내용을 적습니다.
   작성:
   MySQL 외래 키 제약 조건과 초기화: 다른 테이블(portfolios)이 users 테이블의 PK를 참조하고 있을 때, 단순히 DROP TABLE을 하면 제약조건 에러(ERROR 3730)가 발생한다는 것을 배웠습니다. 이를 해결하기 위해 SET FOREIGN_KEY_CHECKS = 0;으로 잠시 안전장치를 끄고 초기화하는 정석적인 DB 관리 방법을 익혔습니다.
   Entity 속성 통일성: 팀원이 작성한 Trade 엔티티 스타일에 맞춰, 자바 하이버네이트 어노테이션 대신 @Column(insertable = false, updatable = false) 설정을 활용해 created_at, updated_at 생성을 DB 엔진(MySQL)에 온전히 위임하는 방식을 맞추었습니다.
   협업 시 패키지 구조의 중요성: 자바 컴파일러는 패키지 경로(import)의 대소문자나 미세한 폴더 구조 변경에 민감하다는 것을 알았습니다. 팀원 간 소통 미스로 패키지 주소가 꼬이면 다른 도메인 기능까지 도미노처럼 컴파일 에러가 터질 수 있음을 직접 체감했습니다.
4. 어려웠던 점
   막혔던 부분과 해결 과정을 적습니다.
   작성:
   어떤 에러가 발생했는지:
   users 테이블 드롭 시 외래 키 참조 실패 에러.
   내 코드가 아닌 팀원들의 Stock, Portfolio, Ranking 관련 서비스/컨트롤러에서 대량의 cannot find symbol 및 missing return statement 컴파일 에러 발생.
   처음에 어떤 부분을 이해하지 못했는지: 내 컴퓨터에 분명히 Stock.java 파일이 존재하는데도 자바가 계속 파일이 없다며 컴파일을 거부하여 원인을 찾기 어려웠습니다.
   어떤 방식으로 원인을 찾았는지: 그레들의 빌드 에러 로그를 꼼꼼히 역추적하여, 파일 유무의 문제가 아니라 팀원들이 새로 짠 DTO와 서비스/컨트롤러 내 상단 import 패키지 경로(com.stocksim.stock.Stock vs com.stocksim.entity.Stock)가 완전히 어긋나 길을 잃었다는 것을 발견했습니다. 또한 미완성된 랭킹 기능에서 return문이 누락된 것도 확인했습니다.
   최종적으로 어떻게 해결했는지: 외래 키 체크를 해제하여 DB 테이블을 깔끔하게 명세 초안대로 다시 밀고 구축했습니다. 꼬여있던 타 도메인 파일들의 임포트 경로를 실제 com.stocksim.dto 및 entity 구조에 맞게 일일이 수정해 주었고, 미완성된 빈 메서드들(RankingController, RankingService)에는 임포트 추가 및 임시 리턴값(return new ArrayList<>();)을 넣어 컴파일 지옥을 탈출하고 정상적으로 BUILD SUCCESSFUL을 띄웠습니다.
5. 개선 사항
   지금 코드를 더 좋게 만들 수 있는 부분을 적습니다.
   작성:
   패키지 및 폴더 구조 그라운드 룰 설정: 도메인별(stock, auth, user)로 묶을지, 계층별(dto, entity, service)로 묶을지 팀원들과 패키지 대원칙을 확실히 정의하여 더 이상 임포트 경로가 꼬이지 않도록 조율이 필요합니다.
   Stub/Mock 코드 작성 규칙: 미완성 기능을 커밋할 때는 최소한 컴파일 에러가 나지 않도록 빈 리턴값이라도 적어두는 규칙이 필요합니다.
6. 다음 목표
   다음에 구현하거나 공부할 내용을 적습니다.
   작성:
   포스트맨으로 token 변경 사항 및 전체 API 정상 동작 최종 테스트
   주식 매수/매도 핵심 로직 비즈니스 코드 분석 및 구현 참여
   회의 때 설명할 내용
   금요일 회의에서 팀원에게 설명할 내용을 3줄 정도로 정리합니다.
   API 명세 변경 완료: 유저 엔티티 및 DTO 스펙을 최종 안인 nickname 기준으로 복구했고, 프론트엔드 요청에 따라 로그인 토큰 반환 Key를 token으로 수정했으니 포스트맨 확인 부탁드립니다.
   패키지 임포트 꼬임 및 컴파일 에러 해결: Stock 관련 DTO와 컨트롤러/서비스 간의 패키지 경로가 어긋나 빌드가 터지던 현상을 실제 파일 경로(com.stocksim.dto 등)에 맞게 일일이 동기화하여 빌드를 정상화해 두었습니다.
   미완성 코드 빌드 통과 조치: 현재 구현 중인 RankingController와 RankingService가 빈 상태라 컴파일 에러가 나서, 우선 임시로 빈 리스트(new ArrayList<>())를 반환하도록 조치해 두었으니 해당 기능 작업자분은 이어서 로직을 구현하시면 됩니다.