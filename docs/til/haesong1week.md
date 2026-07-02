📝 기능 구현 TIL (Today I Learned)
📌 기본 정보
항목	내용
👤 작성자	haesong
📅 작성일	2026년 7월 2일
💡 관련 기능	회원가입/로그인 API 명세 수정 및 전체 빌드 에러 트러블슈팅
🌿 브랜치 / PR	feat/auth-and-bugfix
1. ✨ 구현 기능
   회원가입 API 명세 복구 (nickname)
   엔티티, DTO, 서비스 전반에 걸쳐 기존 name 필드를 팀 최종 스펙인 nickname으로 전면 수정
   로그인 API 응답 Key 변경 (token)
   프론트엔드 요청을 반영하여 JSON 반환 키값을 accessToken에서 token으로 간결하게 변경
   프로젝트 빌드 에러 해결
   타 도메인의 패키지 구조 변경 및 미완성 코드로 인해 발생한 18개 이상의 컴파일 에러(:compileJava FAILED) 추적 및 해결
2. 🛠️ 사용 기술
   Language & Framework: Java 21, Spring Boot, Spring Data JPA
   Library & DB: Lombok (@Getter, @NoArgsConstructor), MySQL 8.0
   Build Tool: Gradle
3. 💡 새롭게 알게 된 점
   외래 키 제약 조건과 테이블 초기화
   참조 중인 테이블이 있을 때 DROP TABLE 시 에러(ERROR 3730)가 발생함을 확인, SET FOREIGN_KEY_CHECKS = 0;을 통해 안전하게 초기화하는 법을 익힘
   엔티티 속성 통일성
   팀원의 Trade 엔티티 스타일에 맞춰, @Column(insertable = false, updatable = false) 설정을 활용해 created_at/updated_at 생성을 DB 엔진에 위임함
   패키지 구조와 협업의 중요성
   잘못된 import 경로 하나가 프로젝트 전체 빌드를 마비시킬 수 있음을 체감, 패키지 구조 동기화의 중요성을 배움
   ⚡ 어려웠던 점 (트러블슈팅)
   문제 상황
   로컬 DB 스키마가 꼬여 삭제가 되지 않고, 분명 존재하는 Stock.java 파일을 자바 컴파일러가 인식하지 못해 18개의 빌드 에러 발생
   원인 분석
   빌드 로그를 추적한 결과, 팀원들이 새로 추가한 DTO와 서비스 간의 상단 import 패키지 경로(com.stocksim.stock vs com.stocksim.entity)가 어긋나 있었고, 랭킹 기능에 return문이 누락된 상태였음
   최종 해결
   외래 키 체크 해제 후 DB를 명세대로 재구축, 꼬인 타 도메인의 임포트 경로를 실제 위치(com.stocksim.dto)로 일일이 수정함. 미완성 랭킹 메서드에는 임시 리턴값(return new ArrayList<>();)을 넣어 빌드를 정상화(BUILD SUCCESSFUL)함
   🚀 개선 사항 & 다음 목표
   [ ] 그라운드 룰 설정: 도메인별/계층별 패키지 대원칙을 팀원들과 명확히 정의하여 경로 꼬임 방지
   [ ] 커밋 규칙 제안: 미완성 기능을 커밋할 때는 최소한 빌드가 깨지지 않도록 빈 리턴값이라도 작성하도록 가이드
   [ ] 변경된 token 및 회원가입 API 포스트맨 최종 정상 동작 테스트
   [ ] 주식 매수/매도 핵심 비즈니스 로직 분석 및 구현 참여
   📢 회의 때 설명할 내용 (3줄 요약)
   🔄 API 명세 변경 완료: 회원가입/로그인 스펙을 팀 최종 안인 nickname 및 token 키값으로 수정 완료했으니 프론트엔드 분들은 확인 부탁드립니다.
   🏗️ 패키지 경로 동기화 및 빌드 정상화: Stock 관련 파일들의 임포트 경로가 서로 어긋나 빌드가 터지던 현상을 실제 구조에 맞게 수정하여 빌드를 살려놓았습니다.
   🏃‍♂️ 미완성 코드 빌드 조치: 현재 작업 중이신 랭킹 컨트롤러/서비스가 빈 상태라 컴파일 에러가 나던 부분은 임시 리턴값을 넣어두었으니, 담당자분은 이어서 구현하시면 됩니다!