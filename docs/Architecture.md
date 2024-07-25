```text
├───application
│   ├───concert
│   │   ├───dto
│   │   │       FindConcertScheduleResult.java
│   │   │
│   │   └───facade
│   │           ConcertFacade.java
│   │
│   ├───payment
│   │   ├───dto
│   │   │       PaymentCommand.java
│   │   │       PaymentResult.java
│   │   │
│   │   └───facade
│   │           PaymentFacade.java
│   │
│   ├───reservation
│   │   ├───dto
│   │   │       ReservationConcertCommand.java
│   │   │       ReservationConcertResult.java
│   │   │
│   │   └───facade
│   │           ReservationFacade.java
│   │
│   ├───user
│   │   └───facade
│   │           UserFacade.java
│   │
│   └───waiting
│       ├───dto
│       │       CheckTokenResult.java
│       │
│       └───facade
│               WaitingFacade.java
│
├───domain
│   ├───concert
│   │   │   Concert.java
│   │   │   ConcertSchedule.java
│   │   │   ConcertSeat.java
│   │   │
│   │   ├───exceprtion
│   │   │       ConcertException.java
│   │   │       ConcertExceptionEnums.java
│   │   │
│   │   ├───repoisitory
│   │   │       ConcertRepository.java
│   │   │
│   │   ├───service
│   │   │       ConcertService.java
│   │   │
│   │   └───vo
│   │           ConcertScheduleStatus.java
│   │           ConcertSeatGrade.java
│   │           ConcertSeatStatus.java
│   │
│   ├───payment
│   │   │   Payment.java
│   │   │
│   │   ├───exception
│   │   │       PaymentException.java
│   │   │       PaymentExceptionEnums.java
│   │   │
│   │   ├───repository
│   │   │       PaymentRepository.java
│   │   │
│   │   ├───service
│   │   │       PaymentService.java
│   │   │
│   │   └───vo
│   │           PaymentStatus.java
│   │
│   ├───reservation
│   │   │   Reservation.java
│   │   │   ReservationItem.java
│   │   │
│   │   ├───repoisitory
│   │   │       ReservationRepository.java
│   │   │
│   │   ├───service
│   │   │       ReservationService.java
│   │   │
│   │   └───vo
│   │           ReservationStatus.java
│   │
│   ├───user
│   │   │   User.java
│   │   │
│   │   ├───exceprtion
│   │   │       UserException.java
│   │   │       UserExceptionEnums.java
│   │   │
│   │   ├───repoisitory
│   │   │       UserRepository.java
│   │   │
│   │   └───service
│   │           UserService.java
│   │
│   └───waiting
│       │   Waiting.java
│       │
│       ├───exceprtion
│       │       WaitingException.java
│       │       WaitingExceptionEnums.java
│       │
│       ├───repoisitory
│       │       WaitingRepository.java
│       │
│       ├───service
│       │       WaitingService.java
│       │
│       └───vo
│               WaitingConstant.java
│               WaitingStatus.java
│
├───infra
│   │   BaseCreateDatetimeEntity.java
│   │   BaseEntity.java
│   │
│   ├───concert
│   │   │   ConcertJpaRepository.java
│   │   │   ConcertRepositoryImpl.java
│   │   │   ConcertScheduleJpaRepository.java
│   │   │   ConcertSeatJpaRepository.java
│   │   │
│   │   ├───entity
│   │   │       ConcertEntity.java
│   │   │       ConcertScheduleEntity.java
│   │   │       ConcertSeatEntity.java
│   │   │
│   │   └───mapper
│   │           ConcertMapper.java
│   │           ConcertScheduleMapper.java
│   │           ConcertSeatMapper.java
│   │
│   ├───payment
│   │   │   PaymentJpaRepository.java
│   │   │   PaymentRepositoryImpl.java
│   │   │
│   │   ├───entity
│   │   │       PaymentEntity.java
│   │   │
│   │   └───mapper
│   │           PaymentMapper.java
│   │
│   ├───reservation
│   │   │   ReservationItemJpaRepository.java
│   │   │   ReservationJpaRepository.java
│   │   │   ReservationRepositoryImpl.java
│   │   │
│   │   ├───entity
│   │   │       ReservationEntity.java
│   │   │       ReservationItemEntity.java
│   │   │
│   │   └───mapper
│   │           ReservationItemMapper.java
│   │           ReservationMapper.java
│   │
│   ├───user
│   │   │   UserJpaRepository.java
│   │   │   UserRepositoryImpl.java
│   │   │
│   │   ├───entity
│   │   │       UserEntity.java
│   │   │
│   │   └───mapper
│   │           UserMapper.java
│   │
│   └───waiting
│       │   WaitingJpaRepository.java
│       │   WaitingRepositoryImpl.java
│       │
│       ├───entity
│       │       WaitingEntity.java
│       │
│       └───mapper
│               WaitingMapper.java
│
├───interfaces
│   ├───controller
│   │   ├───concert
│   │   │   │   ConcertController.java
│   │   │   │
│   │   │   └───dto
│   │   │           FindConcertListDto.java
│   │   │           FindConcertScheduleDto.java
│   │   │           FindConcertSeatDto.java
│   │   │
│   │   ├───payment
│   │   │   │   PaymentController.java
│   │   │   │
│   │   │   └───dto
│   │   │           PaymentDto.java
│   │   │
│   │   ├───reservation
│   │   │   │   ReservationController.java
│   │   │   │
│   │   │   └───dto
│   │   │           ReservationConcert.java
│   │   │
│   │   ├───user
│   │   │   │   UserController.java
│   │   │   │
│   │   │   └───dto
│   │   │           AmountDto.java
│   │   │           ChargeDto.java
│   │   │
│   │   └───waiting
│   │       │   WaitingController.java
│   │       │
│   │       └───dto
│   │               CheckWaitingDto.java
│   │
│   └───scheduler
│           ReservationScheduler.java
│           WaitingScheduler.java
│
└───support
    ├───config
    │       JpaConfig.java
    │       SwaggerConfig.java
    │       WebConfig.java
    │
    ├───enums
    │       EnumConverter.java
    │       EnumInterface.java
    │
    ├───exception
    │       BaseException.java
    │       ExceptionInterface.java
    │
    ├───handler
    │       ApiControllerAdvice.java
    │       ErrorResponse.java
    │
    ├───interceptor
    │   │   LogInterceptor.java
    │   │   WaitingInterceptor.java
    │   │
    │   └───query
    │           ConnectionMethodInterceptor.java
    │           PreparedStatementMethodInterceptor.java
    │           QueryCountAop.java
    │           QueryCounter.java
    │
    └───log
            HttpMessage.java
            LogFilter.java
            ResponseInfoLogData.java
```
