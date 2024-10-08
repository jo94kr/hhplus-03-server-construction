package io.hhplus.server_construction.domain.waiting.service;

import io.hhplus.server_construction.domain.waiting.Waiting;
import io.hhplus.server_construction.domain.waiting.exceprtion.WaitingException;
import io.hhplus.server_construction.domain.waiting.exceprtion.WaitingExceptionEnums;
import io.hhplus.server_construction.domain.waiting.repoisitory.WaitingRepository;
import io.hhplus.server_construction.domain.waiting.vo.WaitingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static io.hhplus.server_construction.domain.waiting.exceprtion.WaitingExceptionEnums.EMPTY_TOKEN;
import static io.hhplus.server_construction.domain.waiting.vo.WaitingConstant.*;

@Service
@RequiredArgsConstructor
public class WaitingService {

    private final WaitingRepository waitingRepository;

    /**
     * 대기열 토큰 검증
     * <pre>
     *     - 토큰검증 시 토큰이 없다면 신규 토큰 발급
     *     - 토큰이 만료되는 일시는 유효한 API 를 호출할때마다 갱신해준다
     * </pre>
     *
     * @param reqToken 대기열 토큰
     * @return Waiting
     */
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

    private Waiting getWaitingInfo(String token) {
        Long rank = waitingRepository.findWaitingRank(token);
        if (rank == null) {
            if (Boolean.FALSE.equals(waitingRepository.isActiveToken(token))) {
                throw new WaitingException(EMPTY_TOKEN);
            } else {
                return Waiting.builder()
                        .token(token)
                        .status(WaitingStatus.PROCEEDING)
                        .build();
            }
        }

        long waitingTime = (long) Math.ceil((double) (rank - 1L) / ENTRY_LIMIT) * 10;
        LocalDateTime timeRemaining = LocalDateTime.now().plusSeconds(waitingTime);
        return Waiting.builder()
                .token(token)
                .rank(rank)
                .accessDatetime(timeRemaining)
                .status(WaitingStatus.WAITING)
                .build();
    }

    /**
     * 대기열 순번 계산
     *
     * @param waiting 대상 대기열
     * @return 대기열 순번
     */
    public Long calcWaitingNumber(Waiting waiting) {
        // 가장 최근에 입장한 대기열 번호
        Long lastProceedingWaitingNum = waitingRepository.findLastProceedingWaiting(WaitingStatus.PROCEEDING);
        // 현재 대기열 번호
        Long currentWaitingNum = waiting.getId();

        // 대기열 순번 = 현재 대기열 번호 - 가장 최근에 입장한 대기열 번호
        return currentWaitingNum - lastProceedingWaitingNum;
    }

    /**
     * 남은 시간 계산
     *
     * @param waitingNumber 대기열 순번
     * @return 남은 시간 (분)
     */
    public Long calcTimeRemaining(Long waitingNumber) {
        LocalDateTime now = LocalDateTime.now();

        // 분당 처리량
        Long throughputPerMinute = THROUGHPUT_PER_MINUTE;

        // 한 사이클 시간
        long cycleTime = CYCLE_TIME;

        // 남은 시간 = (본인 순서 / 분당 처리량) * 한 사이클 시간
        LocalDateTime timeRemaining = now.plusMinutes((waitingNumber / throughputPerMinute) * cycleTime);

        return now.until(timeRemaining, ChronoUnit.MINUTES);
    }

    /**
     * 만료일이 지난 대기열 만료 처리
     *
     * @param now 현재시간
     */
    public void expiredToken(LocalDateTime now) {
        // 만료일이 지난 대기열 조회
        List<Waiting> expireWaitingList = waitingRepository.findAllByStatusNotAndExpiredDatetimeIsBefore(WaitingStatus.WAITING, now.minusMinutes(5));

        // 대기열 만료 처리
        waitingRepository.saveAll(expireWaitingList.stream()
                .map(Waiting::expireToken)
                .toList());
    }

    /**
     * 진입 가능한 대기열 활성화 처리
     */
    public void activeToken() {
        List<String> tokenList = waitingRepository.popWaitingTokenList(ENTRY_LIMIT);
        waitingRepository.activateTokens(tokenList);
    }

    /**
     * 대기열 상태 체크
     * <br>
     * 유효한 토큰일 경우 TTL을 연장한다.
     *
     * @param token 대기열 토큰
     */
    public void checkWaitingStatus(String token) {
        if (Boolean.TRUE.equals(waitingRepository.isActiveToken(token))) {
            // 유효한 토큰일 경우 TTL을 연장
            waitingRepository.refreshTimeout(token);
        } else {
            throw new WaitingException(WaitingExceptionEnums.TOKEN_EXPIRED);
        }
    }

    /**
     * 대기열 만료처리
     *
     * @param token 대기열 토큰
     */
    public void expiredToken(String token) {
        waitingRepository.removeActiveToken(token);
    }
}
