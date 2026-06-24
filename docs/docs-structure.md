# docs 폴더 구조 설계안

이 문서는 StockSim 프로젝트의 협업 문서를 어떤 기준으로 정리할지 제안하는 문서입니다.

현재 프로젝트는 Spring Boot 학습형 사이드 프로젝트이므로, 문서 구조도 기능 명세뿐 아니라 개발 환경, 작업 계획, 학습 기록, 팀 규칙을 쉽게 찾을 수 있도록 구성합니다.

## 추천 구조

```text
docs/
├── setup/
├── wbs/
├── til/
├── rules/
└── architecture/
```

## 1. setup

### 목적

프로젝트를 처음 받은 팀원이 로컬에서 실행할 수 있도록 개발 환경 설정 방법을 정리합니다.

Java, MySQL, Spring Boot 실행, SQL 적용, 프론트엔드 실행 등 환경 세팅과 관련된 문서를 둡니다.

### 추천 파일명

| 파일명 | 목적 |
| --- | --- |
| `backend-local-setup.md` | Spring Boot + MySQL 백엔드 로컬 실행 가이드 |
| `frontend-local-setup.md` | React + Vite 프론트엔드 로컬 실행 가이드 |
| `database-setup.md` | MySQL DB 생성, 계정 설정, SQL 실행 방법 |
| `env-guide.md` | `application.yaml`, 환경 변수, 로컬 설정 값 정리 |
| `troubleshooting.md` | 자주 발생하는 실행 오류와 해결 방법 |

### 현재 문서 이동 후보

| 현재 위치 | 추천 위치 |
| --- | --- |
| `backend/LOCAL_SETUP.md` | `docs/setup/backend-local-setup.md` |
| `backend/sql/*.sql` | SQL 실행 파일은 `backend/sql/` 유지, 설명 문서는 `docs/setup/database-setup.md` |

## 2. wbs

### 목적

한 달 동안 어떤 기능을 언제까지 구현할지 작업 범위와 일정을 정리합니다.

팀원별 역할을 고정하기보다, 기능 단위로 작업을 쪼개고 진행 상태를 확인하는 용도로 사용합니다.

### 추천 파일명

| 파일명 | 목적 |
| --- | --- |
| `project-schedule.md` | 1개월 전체 개발 일정 |
| `weekly-plan.md` | 주차별 목표와 완료 기준 |
| `task-list.md` | 기능별 작업 목록 |
| `mvp-scope.md` | MVP 포함/제외 범위 |
| `progress-board.md` | 진행 중, 완료, 보류 작업 정리 |

### 현재 문서 이동 후보

| 현재 위치 | 추천 위치 |
| --- | --- |
| `docs/requirements/project-plan.md` | `docs/wbs/project-schedule.md` |
| `docs/requirements/feature-spec.md` | `docs/wbs/mvp-scope.md` 또는 `docs/architecture/feature-spec.md` |

## 3. til

### 목적

기능 구현 후 배운 내용을 기록합니다.

단순 결과물이 아니라 Spring Boot, JPA, MySQL, REST API를 직접 구현하며 배운 점을 남기는 공간입니다.

### 추천 파일명

| 파일명 | 목적 |
| --- | --- |
| `feature-til-template.md` | 기능 구현 후 작성할 TIL 템플릿 |
| `2026-06-24-signup.md` | 회원가입 기능 구현 TIL 예시 |
| `2026-06-25-login.md` | 로그인 기능 구현 TIL 예시 |
| `2026-06-26-stock-list.md` | 주식 목록 조회 구현 TIL 예시 |
| `2026-06-27-trade-buy.md` | 매수 기능 구현 TIL 예시 |

### 현재 문서 이동 후보

| 현재 위치 | 추천 위치 |
| --- | --- |
| `docs/til/feature-til-template.md` | 유지 |

## 4. rules

### 목적

팀 개발 규칙, AI 활용 규칙, Git 규칙, PR 규칙을 정리합니다.

프로젝트의 핵심 목표가 학습이므로, “AI를 어떻게 사용할 것인가”와 “직접 구현해야 하는 범위”를 명확히 남깁니다.

### 추천 파일명

| 파일명 | 목적 |
| --- | --- |
| `ai-usage-rules.md` | AI 활용 원칙과 금지 사항 |
| `git-rules.md` | 브랜치, 커밋 메시지, PR 규칙 |
| `code-review-rules.md` | 코드 리뷰 기준 |
| `meeting-rules.md` | 금요일 회의 진행 방식 |
| `til-rules.md` | TIL 작성 기준 |

### 현재 문서 이동 후보

| 현재 위치 | 추천 위치 |
| --- | --- |
| `docs/agents/ai-rules.md` | `docs/rules/ai-usage-rules.md` |
| README의 `개발 규칙` 섹션 | `docs/rules/ai-usage-rules.md`와 연결 |
| `docs/meeting/` | 회의록은 유지하거나 `docs/rules/meeting-rules.md`로 규칙만 분리 |

## 5. architecture

### 목적

서비스 구조, API 구조, DB 구조, 시퀀스 다이어그램 등 구현 기준이 되는 설계 문서를 둡니다.

백엔드 구현 시 Controller, Service, Repository, Entity, DB 테이블이 어떻게 연결되는지 확인하는 용도입니다.

### 추천 파일명

| 파일명 | 목적 |
| --- | --- |
| `system-architecture.md` | 전체 시스템 구조 |
| `backend-architecture.md` | Spring Boot 패키지 구조와 계층 설명 |
| `api-spec.md` | REST API 명세 |
| `endpoint-list.md` | API 엔드포인트 목록 |
| `database-schema.md` | DB 테이블 구조 설명 |
| `erd.md` | ERD |
| `sequence-diagram.md` | 주요 기능 흐름 |

### 현재 문서 이동 후보

| 현재 위치 | 추천 위치 |
| --- | --- |
| `docs/architecture/architecture.md` | `docs/architecture/system-architecture.md` |
| `docs/architecture/sequence-diagram.md` | 유지 |
| `docs/api/api-spec.md` | `docs/architecture/api-spec.md` |
| `docs/api/endpoint-list.md` | `docs/architecture/endpoint-list.md` |
| `docs/database/table-spec.md` | `docs/architecture/database-schema.md` |
| `docs/database/erd.md` | `docs/architecture/erd.md` |

## 최종 추천 형태

```text
docs/
├── setup/
│   ├── backend-local-setup.md
│   ├── frontend-local-setup.md
│   ├── database-setup.md
│   ├── env-guide.md
│   └── troubleshooting.md
│
├── wbs/
│   ├── project-schedule.md
│   ├── weekly-plan.md
│   ├── task-list.md
│   ├── mvp-scope.md
│   └── progress-board.md
│
├── til/
│   ├── feature-til-template.md
│   └── YYYY-MM-DD-feature-name.md
│
├── rules/
│   ├── ai-usage-rules.md
│   ├── git-rules.md
│   ├── code-review-rules.md
│   ├── meeting-rules.md
│   └── til-rules.md
│
└── architecture/
    ├── system-architecture.md
    ├── backend-architecture.md
    ├── api-spec.md
    ├── endpoint-list.md
    ├── database-schema.md
    ├── erd.md
    └── sequence-diagram.md
```

## 정리 기준

- 처음 실행할 때 필요한 문서는 `setup/`에 둡니다.
- 일정과 작업 범위는 `wbs/`에 둡니다.
- 기능 구현 후 배운 내용은 `til/`에 둡니다.
- 팀 규칙과 AI 사용 규칙은 `rules/`에 둡니다.
- 구현 기준이 되는 설계 문서는 `architecture/`에 둡니다.

## 적용 순서 제안

1. 새 폴더 구조를 먼저 생성합니다.
2. 기존 문서를 바로 삭제하지 않고 새 위치로 복사 또는 이동합니다.
3. README와 문서 링크를 새 경로에 맞게 수정합니다.
4. 팀원들이 새 구조를 확인한 뒤 기존 중복 문서를 정리합니다.
