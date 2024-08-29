import http from 'k6/http';
import {check} from 'k6';

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

export default function () {

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
