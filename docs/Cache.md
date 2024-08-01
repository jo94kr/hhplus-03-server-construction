# 캐시를 통한 성능 개선

---

## 목차

- [캐시를 통한 성능 개선](#캐시를-통한-성능-개선)
    * [캐싱의 이해와 효과](#캐싱의-이해와-효과)
        + [캐싱이란?](#캐싱이란)
        + [효과](#효과)
    * [로컬 캐싱 vs 글로벌 캐싱](#로컬-캐싱-vs-글로벌-캐싱)
        + [로컬 캐싱](#로컬-캐싱)
        + [글로벌 캐싱](#글로벌-캐싱)
    * [캐싱 전략 패턴](#캐싱-전략-패턴)
        + [캐시 읽기 전략 (Read Cache Strategy)](#캐시-읽기-전략-read-cache-strategy)
            - [Look Aside 패턴](#look-aside-패턴)
            - [Read Through 패턴](#read-through-패턴)
        + [캐시 쓰기 전략 (Write Cache Strategy)](#캐시-쓰기-전략-write-cache-strategy)
            - [Write Back 패턴](#write-back-패턴)
            - [Write Through 패턴](#write-through-패턴)
            - [Write Around 패턴](#write-around-패턴)
    * [비즈니스 로직 중 캐시를 적용해 볼만한 포인트](#비즈니스-로직-중-캐시를-적용해-볼만한-포인트)
        + [콘서트 조회](#콘서트-조회)
            - [구현 방법](#구현-방법)
            - [성능 비교](#성능-비교)
        + [콘서트 일정 조회](#콘서트-일정-조회)
        + [콘서트 좌석 조회](#콘서트-좌석-조회)

---

## 캐싱의 이해와 효과

### 캐싱이란?

캐싱은 데이터나 연산 결과를 임시 저장하여 반복되는 데이터 요청 시 더 빠르게 처리하는 기술. 주로 메모리(RAM)를 사용해 데이터베이스(DB)보다 빠른 응답 속도를 제공. 조회 빈도가 높은 데이터나 변동이 자주
일어나지 않는 데이터 등에 적합.

### 효과

- **성능 향상:** 자주 조회되는 데이터를 캐시에 저장해 DB 접근을 줄여 성능을 높임.
- **부하 감소:** 캐시 히트를 통해 DB 부하를 줄이고 시스템 안정성을 높임.

---

## 로컬 캐싱 vs 글로벌 캐싱

### 로컬 캐싱

- **정의:** 애플리케이션 내부에서 캐시를 사용.
- **장점:** 응답 속도가 빠르고 네트워크 비용이 적음.
- **단점:** 캐시 크기가 제한되고, 분산된 환경에서 데이터 일관성 문제가 발생할 수 있음.

### 글로벌 캐싱

- **정의:** 외부 캐시 서버(예: Redis)를 사용.
- **장점:** 확장성과 데이터 일관성이 좋음.
- **단점:** 네트워크 비용이 발생.

---

## 캐싱 전략 패턴

### 캐시 읽기 전략 (Read Cache Strategy)

#### Look Aside 패턴

- **정의:** 캐시에 저장된 데이터가 있는지 확인하고, 없으면 DB에서 조회.
- **장점:** 반복적인 읽기가 많은 호출에 적합. 캐시 장애 시에도 DB에서 데이터 조회 가능.
- **단점:** 초기 조회 시 DB 호출 발생.
- **주의:** 캐시 스탬피드 현상. TTL에 도달한 캐시가 삭제되면 여러 애플리케이션이 한꺼번에 DB를 조회해 문제 발생. 적절한 TTL과 미리 캐시에 데이터를 넣어두는 방법으로 해결 가능.

#### Read Through 패턴

- **정의:** 캐시에서만 데이터를 읽어옴. 데이터 동기화를 캐시 제공자에게 위임.
- **장점:** 데이터 정합성 유지.
- **단점:** 캐시 장애 시 서비스 이용 불가.

### 캐시 쓰기 전략 (Write Cache Strategy)

#### Write Back 패턴

- **정의:** 데이터 저장 시 캐시에 먼저 저장하고, 일정 주기마다 DB로 반영.
- **장점:** DB 부하 감소.
- **단점:** 캐시 장애 시 데이터 유실 가능성.
- **적합:** Write가 빈번하고 Read 시 많은 리소스를 소모하는 서비스.

#### Write Through 패턴

- **정의:** 데이터 저장 시 캐시와 DB에 동시에 저장.
- **장점:** 데이터 일관성 유지.
- **단점:** 쓰기 성능 저하 가능성.
- **적합:** 데이터 유실이 발생하면 안 되는 상황.

#### Write Around 패턴

- **정의:** 데이터 저장 시 DB에만 저장하고, 캐시 갱신 없음.
- **장점:** 쓰기 성능이 높음.
- **단점:** 데이터 불일치 가능성, 읽기 시 캐시 miss 발생.

---

## 비즈니스 로직 중 캐시를 적용해 볼만한 포인트

### 콘서트 조회

```java
public Page<Concert> findConcertList(Pageable pageable) {
    return concertRepository.findAllConcert(pageable);
}
```

- **적용 이유:** 콘서트 정보는 빈번하게 수정되지 않으므로 캐시를 적용하는 데 적합.

#### 구현 방법

`Look Aside + Write Around` 패턴을 사용하는 Expiration 방식으로 구현

```java

@Cacheable(cacheNames = CacheConstants.CONCERT_LIST, key = "#pageable", cacheManager = "cacheManager")
public Page<Concert> findConcertListWithCache(Pageable pageable) {
    return concertRepository.findAllConcert(pageable);
}
```

#### 성능 비교

- AS-IS  
  캐시를 걸지않은 DB 조회 약 `116ms` 소요 됨  
  ![콘서트_조회_일반조회.png](https://github.com/jo94kr/hhplus-03-server-construction/blob/main/docs/images/%EC%BD%98%EC%84%9C%ED%8A%B8_%EC%A1%B0%ED%9A%8C_%EC%9D%BC%EB%B0%98%EC%A1%B0%ED%9A%8C.png)

- TO-BE  
  Redis 캐시를 사용한 조회시 약 `47ms`로 DB 조회보다 약 2배 가량 차이  
  ![콘서트_조회_캐시조회.png](https://github.com/jo94kr/hhplus-03-server-construction/blob/main/docs/images/%EC%BD%98%EC%84%9C%ED%8A%B8_%EC%A1%B0%ED%9A%8C_%EC%BA%90%EC%8B%9C%EC%A1%B0%ED%9A%8C.png)

### 콘서트 일정 조회

```java
public List<ConcertSchedule> findConcertScheduleList(Long concertId, LocalDate startDate, LocalDate endDate) {
    Concert concert = concertRepository.findConcertById(concertId);
    return concertRepository.findAllConcertSchedule(concert, startDate, endDate);
}
```

- 조회 조건에 날짜 범위가 들어가서 Key를 잡기가 애매하므로 캐시 사용에 부적합.
- 날짜 컬럼에 인덱스 처리 등으로 성능 개선

### 콘서트 좌석 조회

```java
public List<ConcertSeat> findAllConcertSeatList(Long concertId, Long concertScheduleId) {
    Concert concert = concertRepository.findConcertById(concertId);
    ConcertSchedule concertSchedule = concertRepository.findConcertScheduleById(concertScheduleId);
    return concertRepository.findAllConcertSeat(concert, concertSchedule);
}
```

- 좌석의 경우 상태값이 빈번하게 변동되므로 캐시를 적용하기 부적합.

---
