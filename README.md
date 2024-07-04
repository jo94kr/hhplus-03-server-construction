# hhplus-03-server-construction

## Description

- `콘서트 예약 서비스`를 구현해 봅니다.
- 대기열 시스템을 구축하고, 예약 서비스는 작업 가능한 유저만 수행할 수 있도록 해야합니다.
- 사용자는 좌석예약 시에 미리 충전한 잔액을 이용합니다.
- 좌석 예약 요청시에, 결제가 이루어지지 않더라도 일정 시간동안 다른 유저가 해당 좌석에 접근할 수 없도록 합니다.

## Requirements

- 아래 5가지 API 를 구현합니다.
    - 유저 토큰 발급 API
    - 예약 가능 날짜 / 좌석 API
    - 좌석 예약 요청 API
    - 잔액 충전 / 조회 API
    - 결제 API
- 각 기능 및 제약사항에 대해 단위 테스트를 반드시 하나 이상 작성하도록 합니다.
- 다수의 인스턴스로 어플리케이션이 동작하더라도 기능에 문제가 없도록 작성하도록 합니다.
- 동시성 이슈를 고려하여 구현합니다.
- 대기열 개념을 고려해 구현합니다.

## API Specs

1️⃣ **`주요` 유저 대기열 토큰 기능**

- 서비스를 이용할 토큰을 발급받는 API를 작성합니다.
- 토큰은 유저의 UUID 와 해당 유저의 대기열을 관리할 수 있는 정보 ( 대기 순서 or 잔여 시간 등 ) 를 포함합니다.
- 이후 모든 API 는 위 토큰을 이용해 대기열 검증을 통과해야 이용 가능합니다.

> 기본적으로 폴링으로 본인의 대기열을 확인한다고 가정하며, 다른 방안 또한 고려해보고 구현해 볼 수 있습니다.

2️⃣ **`기본` 예약 가능 날짜 / 좌석 API**

- 예약가능한 날짜와 해당 날짜의 좌석을 조회하는 API 를 각각 작성합니다.
- 예약 가능한 날짜 목록을 조회할 수 있습니다.
- 날짜 정보를 입력받아 예약가능한 좌석정보를 조회할 수 있습니다.

> 좌석 정보는 1 ~ 50 까지의 좌석번호로 관리됩니다.

3️⃣ **`주요` 좌석 예약 요청 API**

- 날짜와 좌석 정보를 입력받아 좌석을 예약 처리하는 API 를 작성합니다.
- 좌석 예약과 동시에 해당 좌석은 그 유저에게 약 (예시 : 5분)간 임시 배정됩니다. ( 시간은 정책에 따라 자율적으로 정의합니다. )
- 만약 배정 시간 내에 결제가 완료되지 않는다면 좌석에 대한 임시 배정은 해제되어야 하며 만약 임시배정된 상태라면 다른 사용자는 예약할 수 없어야 한다.

4️⃣ **`기본`**  **잔액 충전 / 조회 API**

- 결제에 사용될 금액을 API 를 통해 충전하는 API 를 작성합니다.
- 사용자 식별자 및 충전할 금액을 받아 잔액을 충전합니다.
- 사용자 식별자를 통해 해당 사용자의 잔액을 조회합니다.

5️⃣ **`주요` 결제 API**

- 결제 처리하고 결제 내역을 생성하는 API 를 작성합니다.
- 결제가 완료되면 해당 좌석의 소유권을 유저에게 배정하고 대기열 토큰을 만료시킵니다.

---

### 심화 과제

6️⃣ **`심화` 대기열 고도화**

- 다양한 전략을 통해 합리적으로 대기열을 제공할 방법을 고안합니다.
- e.g. 특정 시간 동안 N 명에게만 권한을 부여한다.
- e.g. 한번에 활성화된 최대 유저를 N 으로 유지한다.

<aside>
💡 KEY POINT
</aside>

- 유저간 대기열을 요청 순서대로 정확하게 제공할 방법을 고민해 봅니다.
- 동시에 여러 사용자가 예약 요청을 했을 때, 좌석이 중복으로 배정 가능하지 않도록 합니다.

## Milestone

## ERD

``` mermaid
erDiagram  
    concert ||--|{ concert_schedule: contains
	concert_schedule ||--|{ concert_seat: contains    
	concert {  
        long id PK "콘서트 PK"
		String name "콘서트 명 (not null)"
		datetime create_datetime "생성일시"  
        datetime modify_datetime "수정일시"  
    }    
    concert_schedule {        
		long id PK "콘서트 스케쥴 PK"        
		long concert_id "콘서트 PK"        
		datetime concert_datetime "콘서트 일시 (not null)"        
		datetime create_datetime "생성일시"  
		datetime modify_datetime "수정일시"  
    }    
    concert_seat {        
		long id PK "콘서트 좌석 PK"        
		long concert_schedule_id "콘서트 스케쥴 PK"        
		String grade "좌석 등급"  
		decimal price "좌석 가격 (not null)"        
		String status "좌석 상태 (not null)"        
		datetime create_datetime "생성일시"  
		datetime modify_datetime "수정일시"  
    }  
    user {        
		long id PK "사용자 PK"        
		String name "사용자 명 (not null)"        
		decimal amount "잔액"  
		datetime create_datetime "생성일시"  
		datetime modify_datetime "수정일시"  
    }  
    reservation_payment ||--|| reservation: contains
	reservation_payment {        
		long id PK "결제 PK"        
		long reservation_id "예약 PK"        
		decimal price "결제 금액 (not null)"        
		String status "결제 상태 (not null)"        
		datetime paid_datetime "결제일시"  
		datetime create_datetime "생성일시"  
    }    
    reservation ||--|{ reservation_item: contains    
    reservation {        
		long id PK "예약 PK"        
		long user_id "사용자 PK"        
		datetime create_datetime "생성일시"  
    }    
    reservation_item {        
		long id PK "예약 좌석 PK"        
		long reservation_id "예약 PK"        
		long concert_seat_id "콘서트 좌석 PK"        
		decimal price "가격 (not null)"        
		String status "예약 상태 (예약, 결제완료, 취소) (not null)"  
		datetime create_datetime "생성일시"  
    }  
    waiting {        
		long id PK "대기열 PK"        
		String token "대기열 토큰"  
		String status "대기열 상태"  
		datetime create_datetime "생성일시"  
		datetime create_datetime "생성일시"  
    }
```  

## Sequence Diagram

### 대기열 체크 API

> 대기열 체크와 동시에 토큰이 없을 경우 토큰을 발급한다.

```mermaid
sequenceDiagram
    autonumber
    actor 사용자
    사용자 ->>+ 대기열: 대기열 체크 API 호출 (대기열 토큰)
    break 토큰 없으면 토큰 발급
        대기열 ->> 대기열: 대기열 토큰 저장
        대기열 -->> 사용자: 대기열 토큰 정보
    end
    대기열 ->> 대기열: 참가열 정보 조회
    Note over 대기열, 대기열: 현재 참가열 수, ...
    alt 참가열 진입 가능
        대기열 ->> 대기열: 참가열 추가
        대기열 -->> 사용자: 진입 가능 반환
    else 참가열 진입 불가
        대기열 -->>- 사용자: 대기열 정보
        Note over 대기열, 사용자: 대기자 수, 현재 순번, ...
    end  
```    

### 대기열 토큰 만료 스케쥴러

> 프로세스 완료, 대기열 이탈 등 대기열 토큰 만료 처리

```mermaid  
sequenceDiagram
    autonumber
    loop 10초 마다 스케쥴
        스케쥴러 ->> 대기열: 대기열 토큰 만료 처리
    end  
```  

### 콘서트 조회 API

```mermaid  
sequenceDiagram
    autonumber
    actor 사용자
    사용자 ->>+ 콘서트 목록 조회: 콘서트 목록 조회 요청
    콘서트 목록 조회 ->>+ 대기열: 대기열 토큰 검증
    break 유효하지 않은 토큰
        대기열 -->> 사용자: 검증 실패
    end
    대기열 -->>- 콘서트 목록 조회: 검증 성공
    콘서트 목록 조회 -->>- 사용자: 콘서트 목록  
```  

### 날짜 조회 API

```mermaid  
sequenceDiagram
    autonumber
    actor 사용자
    사용자 ->>+ 콘서트 스케쥴: 날짜 조회 요청 (콘서트 정보)
    콘서트 스케쥴 ->>+ 대기열: 대기열 토큰 검증
    break 유효하지 않은 토큰
        대기열 -->> 사용자: 검증 실패
    end
    대기열 -->>- 콘서트 스케쥴: 검증 성공
    콘서트 스케쥴 -->>- 사용자: 예약 가능한 콘서트 스케쥴  
```  

### 좌석 조회 API

```mermaid  
sequenceDiagram
    autonumber
    actor 사용자
    사용자 ->>+ 예약 가능 좌석: 좌석 조회 요청(콘서트 스케쥴 정보)
    예약 가능 좌석 ->>+ 대기열: 대기열 토큰 검증
    break 유효하지 않은 토큰
        대기열 -->> 사용자: 검증 실패
    end
    대기열 -->>- 예약 가능 좌석: 검증 성공
    예약 가능 좌석 -->>- 사용자: 예약 가능한 좌석  
```  

### 좌석 예약 API

```mermaid  
sequenceDiagram
    autonumber
    actor 사용자
    사용자 ->>+ 좌석 예약: 예약 요청 (좌석정보, 사용자 정보)
    좌석 예약 ->>+ 대기열: 대기열 토큰 검증
    break 유효하지 않은 토큰
        대기열 -->> 사용자: 검증 실패
    end
    대기열 -->>- 좌석 예약: 검증 성공
    좌석 예약 ->> 좌석 예약: 좌석 임시 배정
    좌석 예약 -->>- 사용자: 결제정보  
```  

### 결제 API

```mermaid  
sequenceDiagram
    autonumber
    actor 사용자
    사용자 ->>+ 결제: 결제 요청 (결제정보)
    결제 ->>+ 좌석 예약: 임시 배정 체크 (좌석 배정 번호)
    좌석 예약 -->>- 결제: 임시 배정 여부
    break 좌석 임시 배정 안됨 (or 만료 5분)
        결제 -->> 사용자: 임시 배정 안됨
    end
    alt 잔액 있음
        결제 ->>+ 잔액 충전/조회: 잔액 차감 (사용자, 결제 금액)
        잔액 충전/조회 -->> 결제: 결제 성공
        결제 ->> 좌석 예약: 결제 완료 (좌석 배정 번호)
    %% 스케쥴러에서 토큰 만료 처리 한다면 한 사람당 예약할 수 있는 좌석 수를 정해야 할까?   
        결제 ->> 대기열: 대기열 토큰 완료 (토큰)
        결제 -->> 사용자: 예약 완료
    else 잔액 없음
        결제 ->>+ 잔액 충전/조회: 잔액 차감 (사용자, 결제 금액)
        잔액 충전/조회 -->>- 결제: 잔액 부족
        결제 -->>- 사용자: 잔액 부족
    end  
```
