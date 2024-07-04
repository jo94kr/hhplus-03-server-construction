```text
├─controller
│  ├─concert
│  │  │  ConcertController.java
│  │  │
│  │  └─dto
│  │          FindConcertListDto.java
│  │          FindConcertScheduleDto.java
│  │          FindConcertSeatDto.java
│  │
│  ├─reservation
│  │  │  ReservationController.java
│  │  │
│  │  └─dto
│  │          PaymentDto.java
│  │          ReservationConcert.java
│  │
│  ├─user
│  │  │  UserController.java
│  │  │
│  │  └─dto
│  │          AmountDto.java
│  │          ChargeDto.java
│  │
│  └─waiting
│      │  WaitingController.java
│      │
│      └─dto
│              CheckWaitingDto.java
│
└─domain
├─concert
│  │  Concert.java
│  │  ConcertEnums.java
│  │  ConcertSchedule.java
│  │  ConcertSeat.java
│  │
│  ├─exceprtion
│  │
│  ├─repoisitory
│  │
│  └─service
│
├─reservation
│  │  Reservation.java
│  │  ReservationEnums.java
│  │  ReservationItem.java
│  │  ReservationPayment.java
│  │
│  ├─exceprtion
│  │
│  ├─repoisitory
│  │
│  └─service
│
├─user
│  │  User.java
│  │
│  ├─exceprtion
│  │
│  ├─repoisitory
│  │
│  └─service
│
└─waiting
│  Participants.java
│  Waiting.java
│  WaitingEnums.java
│
├─exceprtion
│
├─repoisitory
│
└─service
```
