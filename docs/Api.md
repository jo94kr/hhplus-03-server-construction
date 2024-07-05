# GET /waiting/check - 대기열 체크

- Description
    - 헤더에 토큰이 없다면 신규 토큰 발급
- Header
    - token: 대기열 토큰
- Response

```json
{
  "token": "String - 토큰",
  "rank": "long - 대기열 순번",
  "status": "enum - 대기열 상태"
}
```
---

# GET /concert - 콘서트 조회

- Description
    - 콘서트 목록 조회
- Header
  - token: 대기열 토큰
- Request

| Parameter | Parameter Description | Required | etc |
|-----------|-----------------------|----------|-----|
| page      | 페이지                   | Y        |     |
| size      | 페이지 사이즈               | Y        |     |

- Response

```json
[
  {
    "content": [
      {
        "concertId": "long - 콘서트 아이디",
        "name": "String - 콘서트 이름",
        "createDatetime": "datetime - 생성일시",
        "modifyDatetime": "datetime - 생성일시"
      }
    ],
    "pageable": {
      "sort": {
        "sorted": false,
        "unsorted": true,
        "empty": true
      },
      "offset": 0,
      "pageSize": 20,
      "pageNumber": 0,
      "paged": true,
      "unpaged": false
    }
  }
]
```

- Error

| code              | message    | etc |
|-------------------|------------|-----|
| INVALID_TOKEN     | 유효하지 않은 토큰 |     |     

---

# GET /concert/{concertId}/schedule - 콘서트 날짜 조회

- Description
    - 콘서트의 날짜 조회
- Header
    - token: 대기열 토큰
- PathVariable
    - concertId: 콘서트 Id
- Response

```json
[
  {
    "concertScheduleId": "long - 콘서트 스케쥴 Id",
    "concertDatetime": "datetime - 콘서트 일시",
    "isSoldOut": "boolean - 매진 여부"
  }
]
```

- Error

| code              | message    | etc |
|-------------------|------------|-----|
| INVALID_TOKEN     | 유효하지 않은 토큰 |     |     

---

# GET /concert/{concertId}/schedule/{concertScheduleId}/seat - 예약 가능 좌석 조회

- Description
    - 콘서트의 좌석 조회
- Header
    - token: 대기열 토큰
- PathVariable
    - concertId: 콘서트 Id
    - concertScheduleId: 콘서트 스케쥴 Id
- Response

```json
[
  {
    "concertSeatId": "long - 콘서트 좌석 Id",
    "grade": "enum - 좌석 등급",
    "price": "decimal - 가격",
    "status": "enum - 좌석 상태 (임시배정, 예약, 예약가능)"
  }
]
```

- Error

| code          | message    | etc |
|---------------|------------|-----|
| INVALID_TOKEN | 유효하지 않은 토큰 |     |     

# POST /reservation - 좌석 예약

- Header
    - token: 대기열 토큰
- Request Body

```json
{
  "concertSeatIdList": [
    "long - 콘서트 좌석 Id"
  ],
  "userId": "long - 사용자 Id"
}
```

- Response Body

```json
{
  "reservationId": "long - 예약 Id",
  "reservationPaymentId": "long - 결제 Id",
  "totalPrice": "decimal - 총 결제금액",
  "reservationItemList": [
    {
      "concertSeatId": "long - 콘서트 좌석 Id",
      "seatNum": "String - 좌석 번호",
      "price": "decimal - 결제금액"
    }
  ]
}
```

- Error

| code          | message    | etc |
|---------------|------------|-----|
| INVALID_TOKEN | 유효하지 않은 토큰 |     |     
| INVALID_SEAT  | 유효하지 않은 좌석 |     |

---

# GET /user/{userId}/amount - 잔액 조회

- PathVariable
    - userId: 사용자 Id
- Response Body

```json
{
  "amount": "decimal - 잔액"
}
```

---

# PATCH /user/{userId}/charge - 잔액 충전

- PathVariable
    - userId: 사용자 Id
- Request Body

```json
{
  "amount": "decimal - 충전 금액"
}
```

- Response Body

```json
{
  "amount": "decimal - 잔액"
}
```

---

# POST /payment - 결제

- Header
    - token: 대기열 토큰
- Request Body

```json
{
  "userId": "long - 사용자 Id",
  "reservationPaymentId": "long - 결제 Id"
}
```

- Response Body

```json
{
  "reservationPaymentId": "long - 결제 Id",
  "status": "enum - 결제 상태",
  "paymentPrice": "decimal - 결제 금액",
  "amount": "decimal - 잔액"
}
```

- Error

| code              | message    | etc |
|-------------------|------------|-----|
| INVALID_TOKEN     | 유효하지 않은 토큰 |     |     
| INVALID_SEAT      | 유효하지 않은 좌석 |     |
| NOT_ENOUGH_AMOUNT | 잔액 부족      |     |     

---
