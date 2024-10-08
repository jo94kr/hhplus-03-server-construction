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
    * [캐시 문제점 및 해결 방안](#캐시-문제점-및-해결-방안)
        + [캐시 스탬피드 현상](#캐시-스탬피드-현상)
        + [해결방안](#해결방안)
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
- **주의:** 캐시 스탬피드 현상. TTL에 도달한 캐시가 삭제되면 여러 애플리케이션이 한꺼번에 DB를 조회해 문제 발생. 적절한 TTL과 미리 캐시에 데이터를 넣어두는 방법(Cache Warming)으로 해결
  가능.

![Look-Aside](https://github.com/jo94kr/hhplus-03-server-construction/blob/main/docs/images/lock-aside.png)

#### Read Through 패턴

- **정의:** 캐시에서만 데이터를 읽어옴. 데이터 동기화를 캐시 제공자에게 위임.
- **장점:** 데이터 정합성 유지.
- **단점:** 캐시 장애 시 서비스 이용 불가.

![Read-Through](https://github.com/jo94kr/hhplus-03-server-construction/blob/main/docs/images/read-through.png)

### 캐시 쓰기 전략 (Write Cache Strategy)

#### Write Back 패턴

- **정의:** 데이터 저장 시 캐시에 먼저 저장하고, 일정 주기마다 DB로 반영.
- **장점:** DB 부하 감소.
- **단점:** 캐시 장애 시 데이터 유실 가능성.
- **적합:** Write가 빈번하고 Read 시 많은 리소스를 소모하는 서비스.

![Write-Back](https://github.com/jo94kr/hhplus-03-server-construction/blob/main/docs/images/write-back.png)

#### Write Through 패턴

- **정의:** 데이터 저장 시 캐시와 DB에 동시에 저장.
- **장점:** 데이터 일관성 유지.
- **단점:** 쓰기 성능 저하 가능성. 요청마다 두번의 쓰기가 발생하므로 빈번한 수정이 발생하는 곳에선 부적합
- **적합:** 데이터 유실이 발생하면 안 되는 상황.

![Write-Through](https://github.com/jo94kr/hhplus-03-server-construction/blob/main/docs/images/write-through.png)

#### Write Around 패턴

- **정의:** 데이터 저장 시 DB에만 저장하고, 캐시 갱신 없음.
- **장점:** 쓰기 성능이 높음.
- **단점:** 데이터 불일치 가능성, 읽기 시 캐시 miss 발생.

![Write-Around](https://github.com/jo94kr/hhplus-03-server-construction/blob/main/docs/images/write-around.png)

---

## 캐시 문제점 및 해결 방안

### 캐시 스탬피드 현상

- 분산환경에서 여러 애플리케이션이 참조하고있던 캐시 키가 만료되었을때 한번에 DB에 조회
- 여러 애플리케이션이 만약 무거운 쿼리를 동시에 호출하게 된다면 DB에 부하가 발생
- 이후 각 애플리케이션에서 읽어온 데이터를 캐시에 등록할때 중복쓰기가 발생

### 해결방안

- 적절한 캐시 만료 시간을 설정
- Cache Warming 적용
    - 캐시를 미리 생성시켜 주는 작업
    - 이벤트등 부하가 몰리기전 미리 정보를 캐시해두는 작업
- PER 알고리즘
    - 확률적 조기 재계산 알고리즘
    - 캐시 값이 만료되기 전에 언제 데이터베이스에 접근해서 값을 읽어오면 되는지 최적으로 계산
    - 무작위성을 도입하여 여러 클라이언트가 동시에 캐시를 갱신하려고 하는 문제(캐시 스탬피드 현상)를 줄인다.
    - 만료 시간에 가까워질수록 true를 반환할 확률이 증가하므로, 만료된 캐시 항목을 더 자주 확인하게 된다. 이를 통해 캐시의 신뢰성을 높이고 성능을 최적화할 수 있다.
  > - currentTime: 현재 남은 만료 시간
  > - timeToCompute: 캐시된 값을 다시 계산하는 데 걸리는 시간
  > - beta: 무작위성 조절 변수 (기본값은 1, 0보다 큰 값으로 설정 가능)
  > - random(): 0과 1 사이의 랜덤 값을 반환하는 함수
  > - expiry: 키를 재설정할 때 새로 넣어줄 만료 시간  
  > - `currentTime - (timeToCompute * beta * Math.log(Math.random())) > expiry` 조건이 참이면 DB조회 해서 새로 캐싱

---

## 캐시 문제점

### 캐시 스탬피드 현상

- 분산환경에서 여러 애플리케이션이 참조하고있던 캐시 키가 만료되었을때 한번에 DB에 조회
- 여러 애플리케이션이 만약 무거운 쿼리를 동시에 호출하게 된다면 DB에 부하가 발생
- 이후 각 애플리케이션에서 읽어온 데이터를 캐시에 등록할때 중복쓰기가 발생

### 해결방안

- 적절한 캐시 만료 시간을 설정
- Cache Warming 적용
    - 캐시를 미리 생성시켜 주는 작업
    - 이벤트등 부하가 몰리기전 미리 정보를 캐시해두는 작업
- PER 알고리즘
    - 확률적 조기 재계산 알고리즘
    - 캐시 값이 만료되기 전에 언제 데이터베이스에 접근해서 값을 읽어오면 되는지 최적으로 계산
    - 무작위성을 도입하여 여러 클라이언트가 동시에 캐시를 갱신하려고 하는 문제(캐시 스탬피드 현상)를 줄인다.
    - 만료 시간에 가까워질수록 true를 반환할 확률이 증가하므로, 만료된 캐시 항목을 더 자주 확인하게 된다. 이를 통해 캐시의 신뢰성을 높이고 성능을 최적화할 수 있다.
    > - currentTime: 현재 남은 만료 시간
    > - timeToCompute: 캐시된 값을 다시 계산하는 데 걸리는 시간
    > - beta: 무작위성 조절 변수 (기본값은 1, 0보다 큰 값으로 설정 가능)
    > - random(): 0과 1 사이의 랜덤 값을 반환하는 함수
    > - expiry: 키를 재설정할 때 새로 넣어줄 만료 시간
    > `currentTime - (timeToCompute * beta * Math.log(Math.random())) > expiry` 조건이 참이면 DB조회 해서 새로 캐싱

## 비즈니스 로직 중 캐시를 적용해 볼만한 포인트

### 콘서트 조회

```java
public Page<Concert> findConcertList(Pageable pageable) {
    return concertRepository.findAllConcert(pageable);
}
```

- **적용 이유:** 콘서트 정보는 빈번하게 수정되지 않으므로 캐시를 적용하는 데 적합.

#### 구현 방법

![look-aside+write-around](https://github.com/jo94kr/hhplus-03-server-construction/blob/main/docs/images/look-aside%2Bwrite-around.png)

- `Look Aside + Write Around` 패턴을 사용하는 Expiration 방식으로 구현
- 빈번하게 수정되는 데이터가 아니지만 조회는 빈번하게 발생 하고 Redis가 사용 불가할때도 정상적인 서비스가 가능하도록 하기위함

```java

@Cacheable(cacheNames = CacheConstants.CONCERT_LIST, key = "#pageable", cacheManager = "cacheManager")
public Page<Concert> findConcertListWithCache(Pageable pageable) {
    return concertRepository.findAllConcert(pageable);
}
```

#### 성능 비교

- AS-IS  
  캐시를 걸지않은 DB 조회 약 10000건 조회 시 `116ms` 소요 됨  
  ![콘서트_조회_일반조회.png](https://github.com/jo94kr/hhplus-03-server-construction/blob/main/docs/images/%EC%BD%98%EC%84%9C%ED%8A%B8_%EC%A1%B0%ED%9A%8C_%EC%9D%BC%EB%B0%98%EC%A1%B0%ED%9A%8C.png)

- TO-BE
  Redis 캐시를 사용한 약 10000건 조회 시 `47ms`로 DB 조회보다 약 2배 가량 차이  
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

- 참고
  - [https://velog.io/@hwsa1004/Spring-Redis-Cache](https://velog.io/@hwsa1004/Spring-Redis-Cache%EB%A5%BC-%ED%86%B5%ED%95%B4-%EC%84%B1%EB%8A%A5-%EA%B0%9C%EC%84%A0%ED%95%98%EA%B8%B0)
  - [https://inpa.tistory.com/entry/REDIS](https://inpa.tistory.com/entry/REDIS-%F0%9F%93%9A-%EC%BA%90%EC%8B%9CCache-%EC%84%A4%EA%B3%84-%EC%A0%84%EB%9E%B5-%EC%A7%80%EC%B9%A8-%EC%B4%9D%EC%A0%95%EB%A6%AC#look_aside_%ED%8C%A8%ED%84%B4)
  - [https://velog.io/@claraqn](https://velog.io/@claraqn/%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A5%BC-%EC%9C%84%ED%95%9C-%EB%A0%88%EB%94%94%EC%8A%A4-5%EC%9E%A5-%EB%A0%88%EB%94%94%EC%8A%A4%EB%A5%BC-%EC%BA%90%EC%8B%9C%EB%A1%9C-%EC%82%AC%EC%9A%A9%ED%95%98%EA%B8%B0#1-%EB%A0%88%EB%94%94%EC%8A%A4%EC%97%90%EC%84%9C-%EB%A7%8C%EB%A3%8C%EB%90%9C-%ED%82%A4%EB%A5%BC-%EC%82%AD%EC%A0%9C%ED%95%98%EB%8A%94-2%EA%B0%80%EC%A7%80-%EB%B0%A9%EB%B2%95)
  - [https://yozm.wishket.com/magazine/detail/2296](https://yozm.wishket.com/magazine/detail/2296/)
