# 성능 개선을 위한 쿼리 파악 및 Index 처리
  
---  

## 목차

- [개요](#개요)
- [Index 생성 기준](#index-생성-기준)
    * [단일 컬럼 인덱스](#단일-컬럼-인덱스)
    * [복합 컬럼 인덱스](#복합-컬럼-인덱스)
    * [주의 사항](#주의-사항)
- [주요 조회 쿼리 목록](#주요-조회-쿼리-목록)
- [쿼리 성능 분석 및 개선](#쿼리-성능-분석-및-개선)
  
---  

## 개요
서비스에서 자주 수행되는 조회 쿼리들의 성능을 분석하고, 필요한 경우 인덱스를 추가하여 성능을 개선한 결과를 담고 있다. 주요 조회 쿼리들을 파악하고 인덱스 추가 전후의 성능을 비교하여 서비스의 부하를 적절하게 축소하는 데 목적을 두었다.

---

## Index 생성 기준

### 단일 컬럼 인덱스

- 카디널리티(Cardinality): 단일 컬럼 인덱스를 생성할 때는 해당 컬럼의 카디널리티가 높은 것을 선택해야 함. 카디널리티가 높은 컬럼은 중복된 값이 적어 인덱스를 통해 데이터의 대부분을 효율적으로 필터링할 수 있음.
  - 카디널리티가 높은 컬럼 예시: 주민등록번호, 계좌번호 등등...
  - 카디널리티가 낮은 컬럼 예시: 성별, 학년 등등...

### 복합 컬럼 인덱스

- 카디널리티 순서: 여러 컬럼으로 인덱스를 구성할 때는 카디널리티가 높은 순서에서 낮은 순서로 인덱스를 구성하는 것이 좋음. 인덱스가 효율적으로 많은 데이터를 걸러낼 수 있도록 하기 위함.
  - Ex) CREATE INDEX idx_example ON table_name (high_cardinality_column, low_cardinality_column);

### 주의 사항

- 복합 컬럼 인덱스 사용시 반드시 인덱스의 첫 번째 컬럼은 조회 조건에 포함 되어야 인덱스가 적용됨. 중간이나 마지막 컬럼이 누락된 경우에는 인덱스를 사용할 수 있지만, 첫 번째 컬럼이 누락된 경우에는 인덱스를 사용할 수 없음.
  - Ex) 인덱스가 idx_example (column1, column2, column3)로 구성된 경우, column1은 반드시 조회 조건에 포함되어야 함.
- `BETWEEN`, `LIKE`, `<`, `>` 등 범위 조건을 사용하면 해당 컬럼까지만 인덱스가 사용되고, 그 이후의 컬럼은 인덱스를 사용하지 않음.
  - Ex) WHERE `column1`, `column2`, `column3` 가 인덱스로 잡혀있고 `column1 = value1 AND column3 = value3 AND column2 > value2` 인 경우, column3은 인덱스가 사용되지 않음.
  - `=`, `in`의 경우 다음 컬럼도 인덱스 사용
- AND 연산자: 각 조건이 읽어야 할 행(row)의 수를 줄이는 역할을 하므로 인덱스 효율을 높임.
- OR 연산자: 비교해야 할 행의 수를 늘리기 때문에 풀 테이블 스캔이 발생할 확률이 높음.
- where salary * 10 > 150000;는 인덱스를 못타지만, where salary > 150000 / 10; 은 인덱스를 사용.

---

## 주요 조회 쿼리 목록

서비스에서 자주 수행되는 주요 조회 쿼리:

- `Concert findConcertById(Long concertId);`
- `Page<Concert> findAllConcert(Pageable pageable);`
- `List<ConcertSchedule> findAllConcertSchedule(Concert concert, LocalDate startDate, LocalDate endDate);`
- `ConcertSchedule findConcertScheduleById(Long concertScheduleId);`
- `List<ConcertSeat> findAllConcertSeatByStatus(ConcertSchedule concertSchedule, ConcertSeatStatus concertSeatStatus);`
- `ConcertSeat findById(Long concertSeatId);`
- `Reservation pessimisticFindReservationById(Long reservationId);`
- `List<ReservationItem> findAllReservationItemByReservationId(Long reservationId);`
- `List<ReservationItem> findAllReservationItemByReservationIdIn(List<Long> reservationIdList);`

---

## 쿼리 성능 분석 및 개선

각 쿼리에 대한 성능 분석 및 인덱스 추가 전후의 성능 비교 결과:

- `Concert findConcertById(Long concertId);`
    - 쿼리 설명: 특정 콘서트를 Id로 조회
    - 인덱스 추가 전/후: 변경 없음 (기본키 인덱스 사용중)
    - 성능 개선 정도: 기본키 인덱스를 사용 중이므로 추가 작업 불필요

> 콘서트 Id 조회 처럼 기본키 인덱스로 조회되는 쿼리는 분석 대상에서 제외

- `Page<Concert> findAllConcert(Pageable pageable);`
    - 쿼리 설명: 모든 콘서트를 페이징하여 조회
    - 인덱스 추가 전/후: 변경 없음 (페이징 처리)
    - 성능 개선 정도: 페이징 처리되어 있으므로 작업 불필요
    - 고려 해볼만한 사항:
        - 커버링 인덱스 사용
        - 커버링 인덱스는 쿼리를 충족시키는 데 필요한 모든 데이터를 갖고 있는 인덱스를 의미. select, where, order by, limit, group by 등에서 사용되는 모든 컬럼이 인덱스 컬럼 안에 포함된 경우. 이 방식은 인덱스가 쿼리에서 필요한 모든 데이터를 포함하고 있어, 테이블을 추가로 조회할 필요가 없다.

- `List<ConcertSchedule> findAllConcertSchedule(Concert concert, LocalDate startDate, LocalDate endDate);`
    - 쿼리 설명: 특정 콘서트의 일정을 지정된 기간으로 조회
    - 데이터 양: 총 10만건
    - 인덱스 추가 전:

      ```shell
      concert_test> select *
              from concert_schedule
              where concert_id = 1
                and concert_datetime between '2024-08-07' and '2024-09-07'
      > 8,481 rows retrieved starting from 1 in 1 s 49 ms (execution: 8 ms, fetching: 1 s 41 ms)
      ```
        - Explain 결과
          - type: `ALL` - 전체 테이블을 스캔
          - rows: 99,079 (총 10만 건 중 필터링된 행 수)
          - Extra: `Using where` - 조건에 따라 필터링됨

      | id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
      | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
      | 1 | SIMPLE | concert\_schedule | null | ALL | null | null | null | null | 99079 | 1.11 | Using where |

    - 인덱스 추가 후:
        - `concert_id`에 인덱스 추가

          ```shell
          concert_test> select *
              from concert_schedule
              where concert_id = 1
                and concert_datetime between '2024-08-07' and '2024-09-07'
          > 8,481 rows retrieved starting from 1 in 952 ms (execution: 16 ms, fetching: 936 ms)
          ```
            - Explain 결과
              - type: `ref` - 인덱스 기반으로 부분 스캔
              - rows: 49,539 (총 10만 건 중 필터링된 행 수)

          | id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
          | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
          | 1 | SIMPLE | concert\_schedule | null | ref | concert\_schedule\_concert\_id\_index | concert\_schedule\_concert\_id\_index | 9 | const | 49539 | 11.11 | Using where |

        - `concert_id`, `concert_datetime`에 인덱스 추가

          ```shell
          concert_test> select *
              from concert_schedule
              where concert_id = 1
                and concert_datetime between '2024-08-07' and '2024-09-07'
          > 8,481 rows retrieved starting from 1 in 840 ms (execution: 13 ms, fetching: 827 ms)
          ```
            - Explain
              - type: `range` - 인덱스 범위 스캔
              - rows: 15,864 (총 10만 건 중 필터링된 행 수)
              - Extra: `Using index condition` - 인덱스 조건을 사용하여 추가 필터링

          | id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
          | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
          | 1 | SIMPLE | concert\_schedule | null | range | concert\_schedule\_concert\_id\_concert\_datetime\_index | concert\_schedule\_concert\_id\_concert\_datetime\_index | 18 | null | 15864 | 100 | Using index condition |

        - `concert_datetime`, `concert_id`에 인덱스 추가

          ```shell
          concert_test> select *
              from concert_schedule
              where concert_id = 1
                and concert_datetime between '2024-08-07' and '2024-09-07'
          > 8,481 rows retrieved starting from 1 in 890 ms (execution: 22 ms, fetching: 868 ms)
          ```
            - Explain
              - type: `range` - 인덱스 범위 스캔
              - rows: 15,864 (총 10만 건 중 필터링된 행 수)
              - Extra: `Using index condition` - 인덱스 조건을 사용하여 추가 필터링

          | id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
          | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
          | 1 | SIMPLE | concert\_schedule | null | range | concert\_schedule\_concert\_datetime\_concert\_id\_index | concert\_schedule\_concert\_datetime\_concert\_id\_index | 18 | null | 15864 | 10 | Using index condition |
    
    - 성능 개선 정도:
      - `concert_id`에 인덱스 추가: 약 10%
      - `concert_id`, `concert_datetime`에 인덱스 추가: 약 20%
      - `concert_datetime`, `concert_id`에 인덱스 추가: 약 15%
    - 결론
      - `concert_id` 컬럼으로 한번 필터링한 다음 분포도가 높은 `concert_datetime`를 필터링 하는방법이 성능 향상에 도움이 되는것으로 확인 되었다.

- `List<ConcertSeat> findAllConcertSeatByStatus(ConcertSchedule concertSchedule, ConcertSeatStatus concertSeatStatus);`
    - 쿼리 설명: 특정 콘서트 일정의 좌석을 상태 조건으로 조회
    - 데이터 양: 약 1,000만 건

      ```shell
      concert_test> select count(*) from concert_seat
      > 1 row retrieved starting from 1 in 3 s 746 ms (execution: 3 s 671 ms, fetching: 75 ms)
      ```
    - 인덱스 추가 전:

      ```shell
      concert_test> select * from concert_seat
                    where concert_schedule_id = 1 and status = 'POSSIBLE'
      > 368,216 rows retrieved starting from 1 in 13 s 667 ms (execution: 2 ms, fetching: 13 s 665 ms)
      ```
        - Explain
          - type: `ALL` - 전체 테이블을 스캔
          - rows: 10,055,514 (약 1,000만 건 중 필터링된 행 수)
          - Extra: Using where - 조건에 따라 필터링됨

      | id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
      | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
      | 1 | SIMPLE | concert\_seat | null | ALL | null | null | null | null | 10055514 | 1 | Using where |

    - 인덱스 추가 후:
        - `concert_schedule_id`에 인덱스 추가

          ```shell
          concert_test> select * from concert_seat
                        where concert_schedule_id = 1 and status = 'POSSIBLE'
          > 368,216 rows retrieved starting from 1 in 11 s 620 ms (execution: 10 ms, fetching: 11 s 610 ms)
          ```
            - Explain
              - type: `ref` - 인덱스 기반으로 부분 스캔
              - rows: 2,398,122 (약 1,000만 건 중 필터링된 행 수)

          | id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
          | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
          | 1 | SIMPLE | concert\_seat | null | ref | concert\_seat\_concert\_schedule\_id\_index | concert\_seat\_concert\_schedule\_id\_index | 9 | const | 2398122 | 10 | Using where |

        - `status`에 인덱스 추가

          ```shell
          concert_test> select * from concert_seat
                        where concert_schedule_id = 1 and status = 'POSSIBLE'
          > 368,216 rows retrieved starting from 1 in 1 m 2 s 960 ms (execution: 24 ms, fetching: 1 m 2 s 936 ms)
          ```
            - Explain
              - type: `ref` - 인덱스 기반으로 부분 스캔
              - rows: 5,027,757 (약 1,000만 건 중 필터링된 행 수)

          | id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
          | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
          | 1 | SIMPLE | concert\_seat | null | ref | concert\_seat\_status\_index | concert\_seat\_status\_index | 1023 | const | 5027757 | 10 | Using where |

        - `concert_schedule_id`, `status`에 인덱스 추가

          ```shell
          concert_test> select * from concert_seat
                        where concert_schedule_id = 1 and status = 'POSSIBLE'
          > 368,216 rows retrieved starting from 1 in 7 s 968 ms (execution: 4 ms, fetching: 7 s 964 ms)
          ```
            - Explain
              - type: `ref` - 인덱스 기반으로 부분 스캔
              - rows: 733,992 (총 1,000만 건 중 필터링된 행 수)

          | id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
          | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
          | 1 | SIMPLE | concert\_seat | null | ref | concert\_seat\_concert\_schedule\_id\_status\_index | concert\_seat\_concert\_schedule\_id\_status\_index | 1032 | const,const | 733992 | 100 | null |

        - `status`, `concert_schedule_id`에 인덱스 추가

          ```shell
          concert_test> select * from concert_seat
                        where concert_schedule_id = 1 and status = 'POSSIBLE'
          > 368,216 rows retrieved starting from 1 in 7 s 732 ms (execution: 7 ms, fetching: 7 s 725 ms)
          ```
            - Explain
              - type: `ref` - 인덱스 기반으로 부분 스캔
              - rows: 733,992 (총 1,000만 건 중 필터링된 행 수)

          | id | select\_type | table | partitions | type | possible\_keys | key | key\_len | ref | rows | filtered | Extra |
          | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
          | 1 | SIMPLE | concert\_seat | null | ref | concert\_seat\_status\_concert\_schedule\_id\_index | concert\_seat\_status\_concert\_schedule\_id\_index | 1032 | const,const | 733992 | 100 | null |

    - 성능 개선 정도
      - `concert_schedule_id`에 인덱스 추가: 약 15% 성능 개선
      - `status`에 인덱스 추가: 성능 저하 (데이터 분포도에 따라 비효율적일 수 있음)
      - `concert_schedule_id`, `status`에 인덱스 추가: 약 42% 성능 개선
      - `status`, `concert_schedule_id`에 인덱스 추가: 약 43% 성능 개선
    - 결론
      - `concert_schedule_id` 컬럼으로 한번 필터링한 다음 분포도가 높은 `status`를 필터링 하는방법이 성능 향상에 도움이 되는것으로 확인 되었다.
      - `concert_schedule_id`, `status` 와 `status`, `concert_schedule_id`의 성능 개선 차이가 미미한 이유는 `concert_schedule_id`와 `status`의 데이터가 비교적 균등하게 분포 되어 있어서 비슷한 수의 레코드를 필터링하기 때문

---
