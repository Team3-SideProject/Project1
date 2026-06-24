# docs 폴더 구조

StockSim 프로젝트 문서는 학습형 Spring Boot 팀 프로젝트에 맞춰 아래 기준으로 정리합니다.

```text
docs/
├── setup/
├── wbs/
├── til/
├── rules/
└── architecture/
```

## setup

로컬 개발 환경 설정과 실행 방법을 정리합니다.

| 파일명 | 목적 |
| --- | --- |
| `backend-local-setup.md` | Spring Boot + MySQL 백엔드 로컬 실행 가이드 |

SQL 실행 파일은 실제 백엔드 리소스에 가까우므로 `backend/sql/`에 유지합니다.

## wbs

프로젝트 일정, MVP 범위, 화면 흐름처럼 작업 범위를 정리합니다.

| 파일명 | 목적 |
| --- | --- |
| `project-schedule.md` | 프로젝트 기획과 주차별 개발 일정 |
| `mvp-scope.md` | MVP 기능 명세와 API 초안 |
| `page-spec.md` | 페이지별 화면 스펙 |
| `user-flow.md` | 사용자 흐름 |
| `meeting-note-ex.md` | 회의록 예시 또는 회의 기록 |

## til

기능 구현 후 배운 내용을 기록합니다.

| 파일명 | 목적 |
| --- | --- |
| `feature-til-template.md` | 기능 구현 후 작성할 TIL 템플릿 |

## rules

팀 규칙, AI 활용 규칙, 프론트/백엔드 작업 기준을 정리합니다.

| 파일명 | 목적 |
| --- | --- |
| `ai-usage-rules.md` | AI 활용 원칙과 문서 반영 기준 |
| `backend-agent-guide.md` | 백엔드 작업 기준 |
| `frontend-agent-guide.md` | 프론트엔드 작업 기준 |

## architecture

구현 기준이 되는 시스템 구조, API, DB, 디자인, 기술 결정 문서를 정리합니다.

| 파일명 | 목적 |
| --- | --- |
| `system-architecture.md` | 전체 시스템 구조 |
| `api-spec.md` | REST API 명세 |
| `endpoint-list.md` | API 엔드포인트 목록 |
| `database-schema.md` | DB 테이블 명세 |
| `schema.sql` | DB 스키마 초안 |
| `erd.md` | ERD |
| `sequence-diagram.md` | 주요 기능 시퀀스 다이어그램 |
| `design-system.md` | 디자인 시스템 |
| `color-guide.md` | 색상 가이드 |
| `wireframe.md` | 와이어프레임 |
| `adr/` | 주요 기술 결정 기록 |

## 문서 작성 기준

- 처음 실행할 때 필요한 내용은 `setup/`에 작성합니다.
- 일정과 MVP 범위는 `wbs/`에 작성합니다.
- 기능 구현 후 배운 내용은 `til/`에 작성합니다.
- 팀 규칙과 AI 사용 규칙은 `rules/`에 작성합니다.
- 구현 기준이 되는 설계 문서는 `architecture/`에 작성합니다.
