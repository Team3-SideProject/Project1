# API 명세 초안

## 공통

- Base URL: `/api`
- 인증 방식: `Authorization: Bearer {accessToken}`
- 응답 데이터는 MVP 단계에서 공통 래퍼 없이 DTO를 바로 반환합니다.

## 인증

### POST `/auth/signup`

```json
{
  "email": "user@example.com",
  "nickname": "stockUser",
  "password": "password123"
}
```

### POST `/auth/login`

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

Response

```json
{
  "accessToken": "jwt-token",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "nickname": "stockUser"
  }
}
```

## 주식

### GET `/stocks`

Response

```json
[
  {
    "id": 1,
    "code": "ALP",
    "name": "Alpha Tech",
    "currentPrice": 50000
  }
]
```

## 거래

### POST `/trades/buy`

```json
{
  "stockId": 1,
  "quantity": 3
}
```

### POST `/trades/sell`

```json
{
  "stockId": 1,
  "quantity": 2
}
```

## 에러 응답

```json
{
  "code": "INSUFFICIENT_CASH",
  "message": "보유 현금이 부족합니다."
}
```
