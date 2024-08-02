# 대기열 시스템 설계

--- 
- [대기열 시스템 설계](#대기열-시스템-설계)
    * [개요](#개요)
    * [시스템 설계](#시스템-설계)
        + [기존 DB 시스템](#기존-DB-시스템)
        + [Redis 기반 시스템](#Redis-기반-시스템)
            - [선정 이유](#선정-이유)
            - [사용한 자료구조](#사용한-자료구조)
            - [구현 방식](#구현-방식)
---

## 개요

- 일시적으로 많은 트래픽이 발생하는 경우에 과도한 트래픽의 유입을 방지해 주는 대기열 시스템
- Redis를 사용하여 효율적이고 확장 가능한 대기열 시스템을 설계

---

## 시스템 설계

### 기존 DB 시스템

- 문제점
    - 과도한 트래픽 처리 불가: 일시적으로 많은 트래픽이 발생하면 DB 처리 성능 저하
    - 확장성 부족: DB 서버의 확장이 한계가 있다

### Redis 기반 시스템

#### 선정 이유

- 고성능: 메모리 기반의 데이터 저장소 매우 빠른 읽기/쓰기 속도를 제공
- 확장성: 클러스터링을 통해 쉽게 확장이 가능하여 많은 트래픽을 효율적으로 처리할 수 있다
- 다양한: 데이터 구조 지원 Redis는 리스트, 셋, 해시맵 등 다양한 데이터 구조를 지원하여 대기열 관리에 유연성을 제공
- 안정성: Redis는 지속적인 데이터 백업과 복제를 통해 데이터의 안정성을 보장

#### 사용한 자료구조

- waiting queue: **sorted set**
    - Score를 기준으로 정렬된 중복을 허용하지 않는 고유한 값들을 관리하는 컬렉션
    - 대기열에 입장한 순서대로 참가열에 입장해야 하므로 사용
    - `key: waiting` `member: token` `score: timestamp`
- active queue: **set**
    - 중복을 허용하지 않는 고유한 값들의 컬렉션
    - `key: active:{token}` `member: {token}`
    - token을 key로 가진 각각의 set에 TTL을 적용

#### 구현 방식

- 유입된 순서대로 스케쥴러를 통해 지정한 인원수만큼 대기열로 진입시킨다
  > 한 사이클당 진입시키는 인원은 대략적으로 정의한 값이고 추후 운영 환경으로 세팅 후 부하 테스트 등을 통해 조금 더 정확한 수치를 도출해 지정하도록 한다
    - 스케줄러 한 사이클당 진입시키는 인원수 계산
        1. **유저가 예약완료 결제까지 걸리는 시간**: 대략 1분
        2. **DB가 처리하는 트랜잭션 수 (TPS)**: 약 1000 TPS
            - 이 수치는 테스트 수치이므로, 실제 운영환경에서 부하테스트를 통해 의미있는 수치를 도출할 필요 있음
        3. **유저가 예약완료 결제까지 호출하는 API 수**: 대략 3 (ex: 좌석조회, 예약, 예외로 인한 재시도 등등)

        - 계산 과정
            1. **분당 처리 가능한 트랜잭션 수**:
                - 1000 TPS * 60초 = **60000 트랜잭션**
            2. **분당 처리 가능한 유저 수**:
                - 분당 처리하는 트랜잭션 수 / 유저가 호출하는 API 수
                - 60000 트랜잭션 / 3 API = **20000 유저**
            3. **10초마다 처리 가능한 유저 수**:
                - 20000 유저 / 60초 * 10초 = **3333 유저**

            ```java
            // 토큰검증 시 토큰이 없다면 신규 토큰 발급  
            // 토큰이 만료되는 일시는 유효한 API 를 호출할때마다 갱신해준다
            public Waiting checkToken(String reqToken) {  
                String token = reqToken;  
                if (reqToken == null) {  
                    // 토큰없음 -> 신규 진입  
                    token = UUID.randomUUID().toString();  
            
                    // 활성화 수  
                    Long activeCnt = waitingRepository.findQueueCnt(ACTIVE_KEY_PREFIX);  
                    if (activeCnt < ENTRY_LIMIT) {  
                        // 참가열 즉시 진입  
                        waitingRepository.addActiveQueue(token);  
                        return Waiting.builder()  
                                .token(token)  
                                .status(WaitingStatus.PROCEEDING)  
                                .build();  
                    } else {  
                        // 대기열 진입  
                        waitingRepository.addWaitingQueue(token);  
                        return this.getWaitingInfo(token);  
                    }  
                } else {  
                    // 토큰 존재 -> 이미 대기중인 상태, 대기열 정보 조회  
                    return this.getWaitingInfo(token);  
                }  
            }
  
            // 예상 입장 시간 계산: (내 순번 / 스케쥴러 한주기당 진입하는 인원) * 스케쥴러 반복 주기(초)
            private Waiting getWaitingInfo(String token) {  
                Long rank = waitingRepository.findWaitingRank(token);  
                long waitingTime = (long) Math.ceil((double) (rank - 1L) / ENTRY_LIMIT) * 10;  
                LocalDateTime timeRemaining = LocalDateTime.now().plusSeconds(waitingTime);  
            
                return Waiting.builder()  
                        .token(token)  
                        .rank(rank)  
                        .accessDatetime(timeRemaining)  
                        .status(WaitingStatus.WAITING)  
                        .build();  
            }
          
            // sorted set의 score로 지정한 진입 시간을 이용해서 진입한 순서대로 토큰을 pop을 이용하여 별도 삭제로직없이 처리
            @Override  
            public List<String> popWaitingTokenList(Long range) {  
                Set<ZSetOperations.TypedTuple<String>> typedTuples = zSetOperations.popMin(WAITING_KEY, range);  
                return Optional.ofNullable(typedTuples)  
                        .map(set -> set.stream()  
                                .map(ZSetOperations.TypedTuple::getValue)  
                                .toList())  
                        .orElse(null);  
            }
  
            // popWaitingTokenList에서 추출한 참가열 진입가능한 토큰들을 pipline을 이용하여 한번에 추가
            @Override  
            public void activateTokens(List<String> tokenList) {  
                redisTemplate.executePipelined((RedisCallback<Object>) connection -> {  
                    tokenList.forEach(token -> {  
                        String key = ACTIVE_KEY_PREFIX + token;  
                        connection.setCommands().sAdd(key.getBytes(), token.getBytes());  
                        connection.commands().expire(key.getBytes(), 300);  
                    });  
                    return null;  
                });  
            }
            ```

- active token 만료처리
    - 대기열에서 참가열로 진입할때 지정해준 TTL을 가지고 기본적으로 만료시키고 유의미한 API(ex: 좌석 조회)를 호출하면 토큰 검증 로직에서 TTL을 갱신해준다

    ```java
    public void checkWaitingStatus(String token) {
        if (Boolean.TRUE.equals(waitingRepository.isActiveToken(token))) {
            // 유효한 토큰일 경우 TTL을 연장
            waitingRepository.refreshTimeout(token);
        } else {
            throw new WaitingException(WaitingExceptionEnums.TOKEN_EXPIRED);
        }
    }
    ```
---
