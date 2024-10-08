# 동시성 문제와 극복

---
## 목차
- [동시성 제어 방안](#동시성-제어-방안)
  + [비관적 락](#비관적-락)
  + [낙관적 락](#낙관적-락)
  + [Redis](#redis)
  + [Kafka](#kafka)
- [비즈니스 로직 중 발생 가능한 동시성 이슈 파악](#비즈니스-로직-중-발생-가능한-동시성-이슈-파악)
  + [동시성 이슈가 발생할 포인트](#동시성-이슈가-발생할-포인트)
  + [기능별 동시성 고려 포인트](#기능별-동시성-고려-포인트)
    - [좌석 예약](#좌석-예약)
    - [결제](#결제)
    - [잔액 충전](#잔액-충전)

---

## 동시성 제어 방안

### 비관적 락

> 자원 요청에 따른 동시성문제가 발생할것이라고 예상하고 락을 거는 방법

- 장점
  - 충돌이 자주 발생하는 환경에서는 롤백 횟수를 줄여서 성능상 유리
  - 데이터 무결성을 보장하는 수준이 높다
  - 트랜잭션이 자원을 점유하는 동안 다른 트랜잭션의 접근을 차단하여 데이터의 일관성을 보장
- 단점
  - 하나의 트랜잭션이 자원에 접근시 락을 걸고 다른트랜잭션이 접근하지 못하게하므로 읽기가 많은 경우 성능상 손해
  - 서로 자원이 필요한 경우에, 락이 걸려있으므로 데드락이 일어날 가능성이 있음
  - 높은 락 경쟁으로 인해 성능 저하가 발생할 수 있음
- 구현 복잡도
  - ★☆☆☆☆
  - JPA 에서 지원하는 어노테이션을 사용하기만 해도 구현 가능 기존 소스를 거의 수정 안해도 된다

#### 구현 방법
JPA 에서 지원하는 `@Lock`어노테이션의 `LockModeType`로 사용할 수 있다 
- `PESSIMISTIC_WRITE` (주로사용)
  - 베타 락 (쓰기에 락) 사용. 다른 트랜잭션에서 읽기/쓰기 모두 불가
  - 더티리드가 발생하지않음
- `PESSIMISTIC_READ`
  - 공유 락 사용. 다른 트랜잭션에서 읽기는 가능하나 쓰기는 불가능
- `PESSIMISTIC_FORCE_INCREMENT`
  - 베타 락 사용하지만 낙관적 락처럼 버전 정보를 사용.
  - 락을 획득하면 버전이 업데이트된다

```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT r FROM ReservationEntity r WHERE r.id = :reservationId")
Optional<ReservationEntity> pessimisticFindReservationById(@Param("reservationId") Long reservationId);
```  

mysql 기준 `select` 문에 `for update`를 추가하여 특정 데이터에 대한 비관적 락을 설정
```sql
select r.id, r.reserved 
from reservation_entity r 
where r.id = ? 
for update
```

### 낙관적 락

> 자원에 락을 걸어서 선점하지않고 동시성 문제가 발생하면 처리하는 방법

- 장점
  - 충돌이 발생하지 않는다는 가정하에 동시 요청에 대한 처리 성능이 유리
  - 트랜잭션 충돌이 발생할 때만 재시도를 하므로, 낮은 충돌율을 가진 시스템에서 효율적
- 단점
  - 잦은 충돌이 일어나는경우 롤백처리에 대한 비용이 많이 들어 오히려 성능상 손해
  - 버전 관리와 재시도 로직 구현이 필요하며, 복잡성이 증가할 수 있음
- 구현 복잡도
  - ★★☆☆☆
  - 엔티티에 버전 컬럼을 추가해줘야 하고 버전 충돌에 대한 예외 처리를 별도로 해줘야한다. 재시도등 고려할 사항도 존재

#### 구현 방법

```java
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "reservation")
public class ReservationEntity extends BaseEntity {
    
    // 생략
  
    @Version
    private Long version = 0L;
}
```

조회 시점의 버전과 커밋 시점의 버전이 다르면 충돌이 발생한것으로 판단하고 예외를 발생 실제론 아래처럼 쿼리가 생성된다
```sql
update reservation
set
    생략...,
    version = 2
where
    id = ?
    and version = 1
```
이미 이전 요청에서 버전이 2로 증가된 상태 이므로 예외가 발생

### Redis

> 메모리 기반의 데이터 저장소 키-밸류(key-value) 데이터 구조에 기반한 다양한 형태의 자료 구조를 제공하며, 데이터들을 저장할 수 있는 저장소
> - 스핀락 (Lettuce)
>   - 락을 획득하기 위해 계속해서 Redis에 락 획득 요청을 보내야 하는 구조
>   - 지속적으로 락 요청을 시도하여 CPU 자원을 소모할 수 있음
> - pub/sub (Redisson)
>   - 스핀락과 다르게 락점유할때 까지 대기하지 않음 
>   - 대기 없이 tryLock 메소드를 이용해서 lock 획득에 성공하면 true를 반환
>   - lock을 구독하는 클라이언트는 해제 신호를 받고 lock 획득을 시도

- 장점
  - 메모리 기반이므로 빠르게 락을 획득 및 해제 가능
  - 분산 환경에서 락을 관리할 수 있어 확장성이 높음
- 단점
  - 레디스 서버가 다운되거나 문제가 발생하면 해당 레디스 서버에 접근하는 모든 클라이언트가 영향을 받을 수 있기에, 레디스 서버가 단일 장애 지점이 될 위험이 있음.
- 구현 복잡도
  - ★★★☆☆
  - 설정, 클라이언트 구현까지 작업이 필요하지만 AOP등으로 재사용 가능하도록 구현하면 실 사용시 기존 소스를 거의 수정 안해도 된다.

#### 구현 방법

Pub/Sub방식 `Redisson`을 AOP 방식으로 구현 AOP 에서 메서드를 실행시키기전 트랜잭션을 강제로 생성시켜 Lock이 생성되기전엔 트랜잭션이 시작 되지않도록 구현
- `AOP 시작 -> Lock -> 트랜잭션 -> 로직 실행 -> 커밋 -> Lock 해제`
- 트랜잭션 중 Lock이 생성될 경우 조회시점이 달라질 수 있고 락 획득 대기시간동안 DB 커낵션이 유지되므로 부하가 발생
```java
// lock 이 시작하고 트랜잭션을 생성시켜주기 위한 컴포넌트
@Component
public class AopTransaction {
  @Transactional(propagation = Propagation.REQUIRES_NEW) // 신규 트랜잭션 생성
  public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
    return joinPoint.proceed(); // 로직 실행
  }
}

// 커스텀 어노테이션에 정의한 파라미터 값을 가져오기위한 클래스 SpEL 사용
public class CustomSpringELParser {
  public static List<String> getDynamicValue(String[] parameterNames, Object[] args, String key) {
    ExpressionParser parser = new SpelExpressionParser();
    StandardEvaluationContext context = new StandardEvaluationContext();

    for (int i = 0; i < parameterNames.length; i++) {
      context.setVariable(parameterNames[i], args[i]);
    }

    Object value = parser.parseExpression(key).getValue(context, Object.class);
    if (value == null) {
      return Collections.emptyList();
    } else if (value instanceof List<?> list) {
      List<String> stringList = new ArrayList<>(list.size());
      for (Object obj : list) {
        stringList.add(obj.toString());
      }
      return stringList;
    } else {
      return Collections.singletonList(value.toString());
    }
  }
}

// Lock 이 시작하고 트랜잭션을 생성해주는 구현부
public class RedissonLockAspect {
  // 생략
  @Around("@annotation(io.hhplus.server_construction.support.aop.annotation.RedissonLock)")
  public Object redissonLock(ProceedingJoinPoint joinPoint) throws Throwable {
    // 생략  
    // lock 키 : 메서드명 + 지정한 키값
    List<String> dynamicValue = CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), annotation.value());
    RLock lock;
    
    // 키값이 여러개일 경우 다중 Lock 사용
    if (dynamicValue.size() > 1) {
      List<String> lockKeys = dynamicValue.stream()
              .map(value -> method.getName() + LOCK_PREFIX + value)
              .toList();
      lock = redissonClient.getMultiLock(lockKeys.stream()
              .map(redissonClient::getLock)
              .toArray(RLock[]::new));
    } else {
      lock = redissonClient.getLock(dynamicValue.get(0));
    }

    try {
      // 락 획득 여부 확인
      boolean isLocked = lock.tryLock(annotation.waitTime(), annotation.leaseTime(), TimeUnit.MILLISECONDS);
      if (!isLocked) {
        throw new IllegalStateException("failed to acquire lock");
      }
      
      // 트랜잭션 생성 + 로직 실행
      return aopTransaction.proceed(joinPoint);
    } catch (InterruptedException e) {
      log.error(e.getMessage(), e);
      throw e;
    } finally {
      // 락 해제
      lock.unlock();
    }
  }
}

@RedissonLock(value = "#reservationConcertCommand.concertSeatIdList")
public ReservationConcertResult setConcertReservation(ReservationConcertCommand reservationConcertCommand) {
  // 사용자 조회
  User user = userService.findUserById(reservationConcertCommand.userId());

  // 콘서트 좌석 조회 - 임시 예약 처리
  List<ConcertSeat> concertSeatList = concertService.setSeatReservation(reservationConcertCommand.concertSeatIdList());

  // 콘서트 예약
  Reservation reservation = reservationService.setConcertReservation(concertSeatList, user);

  return ReservationConcertResult.from(reservation);
}
```

### Kafka

> 파이프라인, 스트리밍 분석, 데이터 통합 및 미션 크리티컬 애플리케이션을 위해 설계된 고성능 분산 이벤트 스트리밍 플랫폼.
> Pub-Sub 모델의 메시지 큐 형태로 동작하며 분산환경에 특화.

- 장점
  - 다수의 브로커로 구성되어 있어, 하나의 브로커가 다운되더라도 다른 브로커가 대신 처리할 수 있어, 고가용성을 보장
- 단점
  - 서버의 수와 스토리지의 용량에 따라 비용이 증가, 관리 복잡
- 구현 복잡도
  - ★★★★☆

---

## 비즈니스 로직 중 발생 가능한 동시성 이슈 파악

### 동시성 이슈가 발생할 포인트

- 좌석 예약: 다수의 사용자가 동시에 같은 좌석을 예약하려는 경우 충돌 가능성이 높음
- 결제: 결제 처리 중 데이터 일관성 및 무결성을 유지해야 하므로 동시성 문제가 발생할 수 있음
- 잔액 충전: 사용자의 잔액을 충전하는 과정에서 데이터 불일치나 무결성 문제가 발생할 수 있음

### 기능별 동시성 고려 포인트

#### 좌석 예약

- [비관적 락](https://github.com/jo94kr/hhplus-03-server-construction/pull/33/commits/867ede835ca10ba79e40d36ef038454820ef10d6)
  - 여러 유저가 동일한 좌석에 요청을 많이 한다는 가정하에 적용
  - 좌석 예약을 요청이 많이 몰리기 때문에 트랜잭션 충돌에 대한 실패를 적절하게 방지 할 수 있음
  - 인기가 없는 좌석은 충돌빈도가 적으므로 불필요한 자원 낭비가 존재
  - ![좌석예약_비관적락.png](images%2F%EC%A2%8C%EC%84%9D%EC%98%88%EC%95%BD_%EB%B9%84%EA%B4%80%EC%A0%81%EB%9D%BD.png)
- [낙관적 락](https://github.com/jo94kr/hhplus-03-server-construction/pull/37) 
  - 잦은 충돌이 발생하는 시점(인기있는 좌석)엔 트랜잭션 롤백 자원이 오히려 더 많이 들어갈 것으로 예상
  - 공유 자원에 대한 읽기 비율이 높은 좌석 예약에선 적절할듯 하지만 충돌 빈도가 높으므로 성능이 저하 될 수 있음
  - ![좌석예약_낙관적락.png](images%2F%EC%A2%8C%EC%84%9D%EC%98%88%EC%95%BD_%EB%82%99%EA%B4%80%EC%A0%81%EB%9D%BD.png)
- [분산락 (Redis)](https://github.com/jo94kr/hhplus-03-server-construction/pull/39)
  - 락을 점유해야 트랜잭션이 시작되므로 사용자가 몰리는 좌석 예약의 경우엔 DB의 부하를 줄일수있음
  - ![좌석예약_레디스.png](images%2F%EC%A2%8C%EC%84%9D%EC%98%88%EC%95%BD_%EB%A0%88%EB%94%94%EC%8A%A4.png)
- 최종 선택
  - 분산 락
    - DB 자원을 무한대로 늘릴수 없으므로 결국 분산락을 사용해야 한다고 생각
    - 한사람이 여러좌석을 예약할 수 있는 구조기 때문에 Redisson의 MultiLock을 이용해서 구현
    - 트랜잭션 뿐만아니라 별도의 작업들도 락을 획득해야 처리하도록 해서 불필요한 부하를 사전에 차단할 수 있음
    - 비관적 락의 단점인 병목현상을 보완할 수 있음
    - 기존에 Redis를 사용중이라면 (대기열 등등..) 적용해볼만하다고 판단
    - ~~그리고 한번 써보자는 이유..~~

#### 결제

- [비관적 락](https://github.com/jo94kr/hhplus-03-server-construction/pull/40/commits/2199943f9b7e558f7ba854ebb8245541996db05f)
  - 데이터 무결성을 보장해야하는 결제에서는 비관적 락이 적합하다고 판단
  - 본인의 자원을 본인이 사용하는 구조이기 때문에 자원에 접근못해도 문제가 없다고 생각
  - ![결제_비관적락.png](images%2F%EA%B2%B0%EC%A0%9C_%EB%B9%84%EA%B4%80%EC%A0%81%EB%9D%BD.png)
- [낙관적 락](https://github.com/jo94kr/hhplus-03-server-construction/pull/37)
  - 충돌이 발생하는 경우가 거의 없으므로 성능상으로는 이점
  - ![결제_낙관적락.png](images%2F%EA%B2%B0%EC%A0%9C_%EB%82%99%EA%B4%80%EC%A0%81%EB%9D%BD.png)
- [분산락 (Redis)](https://github.com/jo94kr/hhplus-03-server-construction/pull/38)
  - 충돌이 빈번하게 일어나지 않고 DB로도 충분히 처리 가능하므로 오버엔지니어링 이라고 판단
  - 하지만 기존에 분산락을 사용중이라면 사용하는덴 지장 없어 보임
  - ![결제_레디스.png](images%2F%EA%B2%B0%EC%A0%9C_%EB%A0%88%EB%94%94%EC%8A%A4.png)
- 최종 선택
  - 비관적 락
    - 금액관련 중요한 데이터 처리, 본인 자원에 접근하기 때문에 병목현상도 발생하지 않기 때문에 적용

#### 잔액 충전

- [비관적 락](https://github.com/jo94kr/hhplus-03-server-construction/pull/40/commits/2199943f9b7e558f7ba854ebb8245541996db05f)
  - 데이터 무결성을 보장해야하는 결제에서는 비관적 락이 적합하다고 판단
  - 본인의 자원을 본인이 사용하는 구조이기 때문에 자원에 접근못해도 문제가 없다고 생각
  - ![잔액충전_비관적락.png](images%2F%EC%9E%94%EC%95%A1%EC%B6%A9%EC%A0%84_%EB%B9%84%EA%B4%80%EC%A0%81%EB%9D%BD.png)
- [낙관적 락](https://github.com/jo94kr/hhplus-03-server-construction/pull/35)
  - 충돌이 발생하는 경우가 거의 없으므로 성능상으로는 이점
  - ![잔액충전_낙관적락.png](images%2F%EC%9E%94%EC%95%A1%EC%B6%A9%EC%A0%84_%EB%82%99%EA%B4%80%EC%A0%81%EB%9D%BD.png)
- [분산락 (Redis)](https://github.com/jo94kr/hhplus-03-server-construction/pull/36)
  - 충돌이 빈번하게 일어나지 않고 DB로도 충분히 처리 가능하므로 오버엔지니어링 이라고 판단
  - 하지만 기존에 분산락을 사용중이라면 사용하는덴 지장 없어 보임
  - ![잔액충전_레디스.png](images%2F%EC%9E%94%EC%95%A1%EC%B6%A9%EC%A0%84_%EB%A0%88%EB%94%94%EC%8A%A4.png)
- 최종 선택
  - 비관적 락
    - 결제와 동일한 이유로 비관적 락이 적합하다고 판단

---
