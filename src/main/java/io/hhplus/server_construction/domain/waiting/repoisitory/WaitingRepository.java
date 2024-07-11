package io.hhplus.server_construction.domain.waiting.repoisitory;

import io.hhplus.server_construction.domain.waiting.Waiting;
import io.hhplus.server_construction.domain.waiting.vo.WaitingStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface WaitingRepository {

    Waiting findWaitingByToken(String token);

    Waiting save(Waiting waiting);

    /**
     * 가장 최근에 대기열을 통과한 대기열 번호 조회
     * <p>
     * 대기열 순번 계산용
     *
     * @return 가장 최근에 대기열을 통과한 대기열 Id
     */
    Long findLastProceedingWaiting();

    /**
     * 분당 처리량을 조회
     * <p>
     *
     * @return 분당 처리량
     */
    Long findThroughputPerMinute(LocalDateTime targetDatetime);

    List<Waiting> findWaitingByStatusAndExpireDatetimeIsBefore(WaitingStatus waitingStatus, LocalDateTime targetDatetime);

    void saveAll(List<Waiting> waitingList);

    List<Waiting> findWaitingByStatusAndRemainingDatetimeIsBefore(WaitingStatus waitingStatus, LocalDateTime targetDatetime);
}
