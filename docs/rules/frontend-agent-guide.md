# Frontend Agent Guide

## 역할

- React + TypeScript + Vite 화면 구현
- 더미 데이터 기반 UI 우선 개발
- API 명세에 맞춘 타입과 fetch 함수 작성

## 기준

- 화면 경로는 `docs/wbs/page-spec.md`를 따릅니다.
- API 요청/응답 타입은 `docs/architecture/api-spec.md`를 기준으로 합니다.
- 공통 색상은 `docs/architecture/color-guide.md`를 따릅니다.

## 주의

- 백엔드 API가 준비되기 전에는 mock 데이터로 화면을 완성합니다.
- 금액 표시는 천 단위 콤마를 적용합니다.
- 매수/매도 성공 후 포트폴리오와 거래 내역이 갱신되는 흐름을 유지합니다.
