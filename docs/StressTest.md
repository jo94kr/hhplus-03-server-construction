
## 목차
- [**개요**](#개요)
- [테스트 케이스](#테스트-케이스)
- [**테스트 시나리오**](#테스트-시나리오)
    * [테스트 플로우](#테스트-플로우)
        + [토큰 발급 시나리오](#토큰-발급-시나리오)
        + [콘서트 예약 시나리오](#콘서트-예약-시나리오)
- [부하테스트 진행](#부하테스트-진행)
    * [토큰 발급 시나리오](#토큰-발급-시나리오)
        + [Peak Test](#peak-test)
        + [Load Test](#load-test)
    * [콘서트 예약 시나리오](#콘서트-예약-시나리오)
        + [Load Test](#load-test-1)
- [성능 개선 대상 및 방법](#성능-개선-대상-및-방법)
    * [개선 방안](#개선-방안)
- [결론](#결론)

---

## 개요

- 다양한 부하 테스트 시나리오를 통해 시스템의 성능을 평가하고, 발견된 문제점을 개선하기 위한 방안을 제시한다.

## 테스트 케이스

**Load Test ( 부하 테스트 )**

- 시스템이 예상되는 부하를 정상적으로 처리할 수 있는지 평가
- 특정한 부하를 제한된 시간 동안 제공해 이상이 없는지 파악
- 목표치를 설정해 적정한 Application 배포 Spec 또한 고려해 볼 수 있음

**Endurance Test ( 내구성 테스트 )**

- 시스템이 장기간 동안 안정적으로 운영될 수 있는지 평가
- 특정한 부하를 장기간 동안 제공했을 때, 발생하는 문제가 있는지 파악
- 장기적으로 Application 을 운영할 때 발생할 수 있는 숨겨진 문제를 파악해 볼 수 있음 ( feat. Memory Leak, Slow Query 등 )

**Stress Test ( 스트레스 테스트 )**

- 시스템이 지속적으로 증가하는 부하를 얼마나 잘 처리할 수 있는지 평가
- 점진적으로 부하를 증가시켰을 때, 발생하는 문제가 있는지 파악
- 장기적으로 Application 을 운영하기 위한 Spec 및 확장성과 장기적인 운영 계획을 파악해 볼 수 있음

**Peak Test ( 최고 부하 테스트 )**

- 시스템에 일시적으로 많은 부하가 가해졌을 때, 잘 처리하는지 평가
- 목표치로 설정한 임계 부하를 일순간에 제공했을 때, 정상적으로 처리해내는지 파악
- 선착순 이벤트 등을 준비하면서 정상적으로 서비스를 제공할 수 있을지 파악해 볼 수 있음

## 테스트 시나리오

콘서트 예약 시스템에서 대규모 트래픽 상황을 가정하여, 가상 사용자의 증가에 따른 시스템의 안정성을 평가한다.

### 테스트 플로우

가장 부하가 많이 발생할 것으로 예상되는 시나리오를 선정하여 테스트를 진행한다.

#### 토큰 발급 시나리오

1. 토큰 발급
    - 콘서트 조회를 위한 대기열 토큰을 발급 받는다.
2. 토큰 조회
    - 진입 가능한 토큰인지 조회 한다.

토큰을 발급받는 케이스로 가장 많은 부하가 몰릴것으로 예상되는 시나리오 이다. 적합한 테스트 종류로는 Load Test, Peak Test이 있을 것이다.

#### 콘서트 예약 시나리오

1. 토큰 발급
    - 콘서트 조회를 위한 대기열 토큰을 발급 받는다.
2. 콘서트 목록 조회
    - 토큰 발급 후, 3초 간격을 두고 사용자가 콘서트 목록을 조회한다.
3. 콘서트 일정 조회
    - 콘서트 목록 조회 후, 3초 간격을 두고 콘서트 일정을 조회한다.
4. 예약 가능한 좌석 조회
    - 콘서트 일정을 조회 후, 2초 간격을 두고 예약 가능한 좌석 정보를 조회한다.
5. 좌석 예약
    - 좌석 조회 후, 3초 간격을 두고 좌석 예약 절차를 진행.

콘서트 예약 시나리오는 Load Test를 이용하여 테스트해 볼것이다.
콘서트 예약 시나리오를 통해서 대기열 토큰을 활성화 하는 대략적인 수치를 도출해 낼 수 있을것이다.

## 3. 부하테스트 진행

자바스크립트를 이용해서 손쉽게 테스트를 할 수 있는 K6를 이용하여 부하 테스트를 진행한다.

### 토큰 발급 시나리오

#### Peak Test

- 100명의 가상 사용자가 100번씩 사용하여 독립적으로 시나리오를 수행하도록 한다.

```javascript
import http from 'k6/http';  
import {check} from 'k6';  
  
export let options = {  
  scenarios: {  
    token_scenario: {  
      vus: 100, // 가상 사용자  
      exec: 'token_scenario',  
      executor: 'per-vu-iterations', // 각각의 가상 사용자들이 정확한 반복 횟수만큼 실행  
      iterations: 100
    }  
  }  
};  
  
export function token_scenario() {  
  
  let token = getToken();  
  
  checkToken(token)  
}  
  
function getToken() {  
  let res = http.get(`http://host.docker.internal:8080/waiting/check`);  
  
  // 요청이 성공했는지 확인  
  const isStatus200 = check(res, {'is status 200': (r) => r.status === 200});  
  
  if (isStatus200) {  
    // JSON 응답 처리  
    let responseData = JSON.parse(res.body);  
  
    // 필요한 데이터를 리턴  
    return responseData.token; // 예: 응답에서 'token' 키의 값을 반환  
  } else {  
    console.log(`Request failed with status: ${res.status}`);  
    return null; // 요청이 실패했을 때 null 반환  
  }  
}  
  
function checkToken(token) {  
  let headers = {  
    'Authorization': `${token}` // Authorization 헤더에 토큰 추가  
  };  
  
  let res = http.get(`http://host.docker.internal:8080/waiting/check`, {  
    headers: headers  
  });  
  
  // 요청이 성공했는지 확인  
  const isStatus200 = check(res, {'is status 200': (r) => r.status === 200});  
  
  // 응답 값을 로그로 출력  
  if (isStatus200) {  
    console.log(`Response Body: ${res.body}`);  
  } else {  
    console.log(`Request failed with status: ${res.status}`);  
    console.log(`Response Body: ${res.body}`);  
  }  
}
```
![token_peak.png](images%2Ftoken_peak.png)
![token_peak2.png](images%2Ftoken_peak2.png)

- 주요 지표
  - 서버 연결 시간 (http_req_connecting)
    - 서버에 연결하는 데 걸린 평균 시간은 502.54µs였으며, 연결이 거의 즉시 이루어졌다.

  - HTTP 요청 응답 시간 (http_req_duration)
    - HTTP 요청을 보내고 응답을 받는 데 걸린 평균 시간은 420.64ms였다. 90%의 요청이 1.18초 이내에, 95%는 1.46초 이내에 완료되었으며, 최대 응답 시간은 1.9초였다.

  - 서버 응답 대기 시간 (http_req_waiting)
    - 서버에서 응답을 기다리는 데 걸린 평균 시간은 420.55ms였다.

- 요약
  - 평균 HTTP 요청 응답 시간은 420.64ms로, 응답 시간이 빠르고 안정적이었다. 다만, 일부 요청에서 1.9초까지 지연이 발생했다.
  - 전체적인 평균과 중앙값이 낮은 수치를 기록한 점을 감안하면, 전반적으로 안정적이고 빠른 응답 성능을 확인할 수 있다.

#### Load Test

- 테스트가 시작되면, 1초 동안 가상 사용자 수를 5명으로 증가시킨다. 그 후 5초 동안 가상 사용자 수를 100명까지 늘린다. 그 후 5초마다 가상 사용자 수를 200명, 300명, 500명으로 점진적으로 증가. 그런 다음, 10초 동안 가상 사용자 수를 600명으로 더 확장하여 시스템에 높은 부하를 준다. 이후 10초 동안 가상 사용자 수를 다시 500명으로 줄이며, 마지막으로 5초 동안 모든 가상 사용자를 종료하여 테스트를 마무리 한다.

```javascript
// Load Test는 option 부분만 수정하여 테스트 진행한다.
export let options = {
   stages: [
      {duration: '1s', target: 5},
      { duration: '5s', target: 100 },
      { duration: '5s', target: 200 },
      { duration: '5s', target: 300 },
      { duration: '5s', target: 500 },
      { duration: '10s', target: 600 },
      { duration: '10s', target: 500 },
      { duration: '5s', target: 0 }
   ]
};

// 생략...
```
![token_load.png](images%2Ftoken_load.png)
![token_load2.png](images%2Ftoken_load2.png)

- 주요 지표
  - 서버 연결 시간 (http_req_connecting)
    - 서버에 연결하는 데 걸린 평균 시간은 372.58µs였으며, 연결이 거의 즉시 이루어졌다.
  - HTTP 요청 응답 시간 (http_req_duration)
    - HTTP 요청을 보내고 응답을 받는 데 걸린 평균 시간은 988.08ms였다.
    - 90%의 요청이 2.34초 이내에, 95%의 요청이 2.52초 이내에 완료되었으며, 최대 응답 시간은 3.29초였다.
  - 서버 응답 대기 시간 (http_req_waiting)
    - 서버에서 응답을 기다리는 데 걸린 평균 시간은 987.98ms였다.

- 요약
  - 서버 연결 시간은 평균 372.58µs로, 연결이 거의 즉시 이루어졌다.
  - HTTP 요청 응답 시간은 평균 988.08ms였으며, 대부분의 요청이 1초 이내에 완료되었다. 상위 10%의 요청에서는 응답 시간이 2.34초에서 2.52초 사이였고, 최대 3.29초가 소요되었다.

### 콘서트 예약 시나리오


#### Load Test

```javascript
import http from 'k6/http';
import {check, sleep, group} from 'k6';
import {randomItem, randomIntBetween} from 'https://jslib.k6.io/k6-utils/1.2.0/index.js';

export let options = {
    stages: [
        {duration: '1s', target: 5},
        { duration: '5s', target: 100 },
        { duration: '5s', target: 200 },
        { duration: '5s', target: 300 },
        { duration: '5s', target: 500 },
        { duration: '10s', target: 600 },
        { duration: '5s', target: 0 }
    ],
};

export default function () {
    // 전체 시나리오를 그룹으로 묶음
    group('Load Test Scenario', function () {
        // 토큰 발급
        let token = group('Get Token', function () {
            return getToken();
        });
        if (!token) {
            return; // 토큰이 없으면 이후 요청을 실행하지 않음
        }

        sleep(3);

        // 콘서트 목록 조회
        let concertList = group('Fetch Concert List', function () {
            return getConcertList(token);
        });
        if (!concertList || concertList.length === 0) {
            console.log('No concerts available.');
            return;
        }

        sleep(3);

        // 첫 번째 콘서트의 일정 목록 조회
        let schedules = group('Fetch Concert Schedules', function () {
            return getConcertSchedules(token, concertList[0].concertId);
        });
        if (!schedules || schedules.length === 0) {
            console.log('No schedules available.');
            return;
        }

        sleep(2);

        // 예약가능 좌석 조회
        let availableSeats = group('Fetch Available Seats', function () {
            return getAvailableSeats(token, schedules[0].concertScheduleId);
        });
        if (!availableSeats || availableSeats.length === 0) {
            console.log('No available seats.');
            return;
        }

        sleep(3);

        // 좌석 예약
        group('Make Reservation', function () {
            let userId = randomIntBetween(1, 10000); // 사용자 ID를 1부터 10,000까지 랜덤하게 선택
            let selectedSeats = selectRandomSeats(availableSeats);
            reservationSeat(token, selectedSeats, userId);
        });
    });
}


function getToken() {
    let res = http.get(`http://host.docker.internal:8080/waiting/check`);

    // 요청이 성공했는지 확인
    const isStatus200 = check(res, {'is status 200': (r) => r.status === 200});

    if (isStatus200) {
        // JSON 응답 처리
        let responseData = JSON.parse(res.body);

        // 필요한 데이터를 리턴
        return responseData.token; // 예: 응답에서 'token' 키의 값을 반환
    } else {
        console.log(`Request failed with status: ${res.status}`);
        return null; // 요청이  실패했을 때 null 반환
    }
}

function getConcertList(token) {
    let headers = {'Authorization': `${token}`};
    let res = http.get(`http://host.docker.internal:8080/concerts`, {headers: headers});

    const isStatus200 = check(res, {'is status 200': (r) => r.status === 200});

    if (isStatus200) {
        let responseData = JSON.parse(res.body);
        return responseData.content;
    } else {
        console.log(`Failed to fetch concert list. Status: ${res.status}`);
        return null;
    }
}

function getConcertSchedules(token, concertId) {
    let headers = {'Authorization': `${token}`};
    let startDate = '2024-08-01'; // 필터링을 위한 시작일 (예시)
    let endDate = '2024-08-31';   // 필터링을 위한 종료일 (예시)

    let res = http.get(`http://host.docker.internal:8080/concerts/${concertId}/schedules?startDate=${startDate}&endDate=${endDate}`, {headers: headers});

    const isStatus200 = check(res, {'is status 200': (r) => r.status === 200});

    if (isStatus200) {
        let responseData = JSON.parse(res.body);
        return responseData; // Assuming the response is a list of schedules
    } else {
        console.log(`Failed to fetch concert schedules. Status: ${res.status}`);
        return null;
    }
}

function getAvailableSeats(token, concertScheduleId) {
    let headers = {'Authorization': `${token}`};
    let res = http.get(`http://host.docker.internal:8080/concerts/schedules/${concertScheduleId}/seats`, {headers: headers});

    const isStatus200 = check(res, {'is status 200': (r) => r.status === 200});

    if (isStatus200) {
        let responseData = JSON.parse(res.body);
        let possibleSeatIds = responseData
            .filter(seat => seat.status === "POSSIBLE")
            .map(seat => seat.concertSeatId);
        return possibleSeatIds; // Assuming the response is a list of available seats
    } else {
        console.log(`Failed to fetch available seats. Status: ${res.status}`);
        return null;
    }
}

function reservationSeat(token, concertSeatIdList, userId) {
    let headers = {
        'Authorization': `${token}`,
        'Content-Type': 'application/json'
    };
    let payload = JSON.stringify({
        concertSeatIdList: concertSeatIdList,
        userId: userId
    });
    let res = http.post(`http://host.docker.internal:8080/reservations`, payload, {headers: headers});

    const isStatus200 = check(res, {'is status 200': (r) => r.status === 200});

    if (isStatus200) {
        console.log(`Reservation successful for seat ID: ${concertSeatIdList}`);
    } else {
        console.log(`Reservation failed. Status: ${res.status}`);
    }
}

function selectRandomSeats(availableSeats) {
    let numberOfSeats = randomIntBetween(1, 2); // 1개 혹은 2개의 좌석을 선택
    let selectedSeats = [];

    for (let i = 0; i < numberOfSeats; i++) {
        let randomSeat = randomItem(availableSeats);
        selectedSeats.push(randomSeat);

        // 중복되지 않도록 배열에서 선택된 좌석 제거
        availableSeats = availableSeats.filter(seat => seat !== randomSeat);
    }

    return selectedSeats;
}
```

![reservation_load.png](images%2Freservation_load.png)
![reservation_load2.png](images%2Freservation_load2.png)

- 주요 지표
  - 서버 연결 시간 (http_req_connecting)
    - 서버에 연결하는 데 걸린 평균 시간은 616.7µs였으며, 최대 108.94ms가 소요되었다.
  - HTTP 요청 응답 시간 (http_req_duration)
    - HTTP 요청을 보내고 응답을 받는 데 걸린 평균 시간은 180.55ms였다. 90%의 요청이 487.58ms 이내에, 95%는 917.36ms 이내에 완료되었으며, 최대 응답 시간은 2.77초였다.
  - 서버 응답 대기 시간 (http_req_waiting)
    - 서버에서 응답을 기다리는 데 걸린 평균 시간은 179.89ms였다.

 
## 성능 개선 대상 및 방법

- 테스트 결과, HTTP 요청 응답 시간의 평균은 180.55ms였지만, 일부 요청에서 최대 2.77초까지 지연이 발생했다.
- 콘서트 예약 시나리오의 좌석 예약 단계에서 눈에 뛰는 성능 저하가 나타났다.

### 개선 방안
  - 좌석 예약 단계에서의 지연을 줄이기 위해 DB 쿼리를 최적화(인덱스, 쿼리 리팩토링 등..)
  - 자주 조회되는 콘서트 목록, 일정, 좌석 정보에 대한 캐싱을 적용하여 DB 조회 빈도를 줄인다. (이미 캐시 처리 되어있음)

## 결론
부하 테스트를 통해 시스템의 대략적인 성능과 문제점을 확인할 수 있엇다. 좌석 예약 과정에서의 성능 저하와 일부 요청의 지연 문제가 발견 되었고
이를 개선하기 위해 DB 쿼리 최적화, 캐싱 전략 강화, 서비스 스케일링 등의 작업이 필요 할 것으로 판단된다.

