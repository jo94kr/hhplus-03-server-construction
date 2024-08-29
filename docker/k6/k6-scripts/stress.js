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

