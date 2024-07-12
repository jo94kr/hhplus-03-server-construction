package io.hhplus.server_construction.domain.waiting.service;

import io.hhplus.server_construction.domain.waiting.Waiting;
import io.hhplus.server_construction.domain.waiting.exceprtion.TokenExpiredException;
import io.hhplus.server_construction.domain.waiting.repoisitory.WaitingRepository;
import io.hhplus.server_construction.domain.waiting.vo.WaitingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WaitingService {

    private final WaitingRepository waitingRepository;

    public Waiting checkToken(String token) {
        Waiting waiting;
        // 토큰이 없으면 신규 토큰 발급
        if (token == null) {
            waiting = waitingRepository.save(Waiting.create());
        } else {
            waiting = waitingRepository.findWaitingByToken(token);
            if (WaitingStatus.EXPIRED.equals(waiting.getStatus())) {
                throw new TokenExpiredException();
            }

            // 토큰 만료일시 갱신
            waitingRepository.save(waiting.renewalExpiredDatetime());
        }

        return waiting;
    }

    public Long calcWaitingNumber(Waiting waiting) {
        // 가장 최근에 입장한 대기열 번호
        Long lastProceedingWaitingNum = waitingRepository.findLastProceedingWaiting(WaitingStatus.PROCEEDING);
        // 현재 대기열 번호
        Long currentWaitingNum = waiting.getId();

        // 대기열 순번 = 현재 대기열 번호 - 가장 최근에 입장한 대기열 번호
        return currentWaitingNum - lastProceedingWaitingNum;
    }

    public Long calcTimeRemaining(Waiting waiting, Long waitingNumber) {
        LocalDateTime now = LocalDateTime.now();

        // 분당 처리량 (1분전부터 현재까지 대기중이 아닌 대기열 개수 조회)
        Long throughputPerMinute = waitingRepository.findThroughputPerMinute(now, WaitingStatus.WAITING);

        // 한 사이클 시간 (임의로 지정한 사이클 시간(분))
        long cycleTime = 5L;

        // 남은 시간 = (본인 순서 / 분당 처리량) * 한 사이클 시간
        LocalDateTime timeRemaining = now.plusMinutes((waitingNumber / throughputPerMinute) * cycleTime);

        // 진입 시간 업데이트
        waitingRepository.save(waiting.setRemainingDatetime(timeRemaining));

        return now.until(timeRemaining, ChronoUnit.MINUTES);
    }

    public void findExpiredToken(LocalDateTime now) {
        // 만료일이 지난 대기열 조회
        List<Waiting> expireWaitingList = waitingRepository.findWaitingByStatusAndExpireDatetimeIsBefore(WaitingStatus.WAITING, now.minusMinutes(5));

        // 대기열 만료 처리
        waitingRepository.saveAll(expireWaitingList.stream()
                .map(Waiting::expireToken)
                .toList());
    }

    public void findActiveToken(LocalDateTime now) {
        // 진입 가능한 대기열 조회
        List<Waiting> activeWaitingList = waitingRepository.findWaitingByStatusAndAccessDatetimeIsBefore(WaitingStatus.WAITING, now);

        // 대기열 활성화 처리
        waitingRepository.saveAll(activeWaitingList.stream()
                .map(Waiting::remainingToken)
                .toList());
    }

    public boolean checkWaitingStatus(String token) {
        Waiting waiting = waitingRepository.findWaitingByToken(token);
        if (waiting != null) {
            boolean availableToken = waiting.isAvailableToken();
            if (availableToken) {
                // 유효한 토큰일 경우 만료일을 연장
                waiting.renewalExpiredDatetime();
            }
            return availableToken;
        } else {
            return false;
        }
    }

    public void expiredToken(String token) {
        Waiting waiting = waitingRepository.findWaitingByToken(token);
        waitingRepository.save(waiting.expireToken());
    }
}
