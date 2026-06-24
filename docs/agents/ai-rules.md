# AI 작업 규칙

## 공통

- 변경 전 관련 문서를 먼저 확인합니다.
- 기존 사용자 변경을 되돌리지 않습니다.
- 문서와 구현이 달라지면 문서도 함께 업데이트합니다.
- AI는 학습과 설명을 위한 도구로 사용합니다.
- 핵심 비즈니스 로직은 팀원이 직접 이해하고 구현합니다.
- 이해하지 못한 코드는 커밋하지 않습니다.
- AI가 생성한 코드는 직접 분석한 뒤 프로젝트에 반영합니다.

## 프론트 작업

- `frontend/` 아래에서 작업합니다.
- UI는 MVP 범위를 넘지 않게 구현합니다.
- API 미완성 시 mock 데이터로 연결합니다.

## 백엔드 작업

- `backend/` 아래에서 작업합니다.
- Controller, Service, Repository, 예외 처리, 데이터 검증은 학습을 위해 직접 구현합니다.
- 거래 로직은 테스트를 우선 고려합니다.
- DB 컬럼명은 snake_case를 사용합니다.

## 문서 작업

- 페이지 변경은 `docs/requirements/page-spec.md`에 반영합니다.
- API 변경은 `docs/api/api-spec.md`와 `endpoint-list.md`에 반영합니다.
- DB 변경은 `docs/database/table-spec.md`와 `schema.sql`에 반영합니다.
