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
    Long findLastProceedingWaiting(WaitingStatus status);

    List<Waiting> findAllByStatusNotAndExpiredDatetimeIsBefore(WaitingStatus waitingStatus, LocalDateTime targetDatetime);

    void saveAll(List<Waiting> waitingList);

    List<Waiting> findWaitingByStatusAndAccessDatetimeIsBefore(WaitingStatus waitingStatus, LocalDateTime targetDatetime);

    Boolean addWaitingQueue(String token);

    Long findWaitingRank(String token);

    Long findQueueCnt(String key);

    List<String> popWaitingTokenList(Long range);

    void addActiveQueue(String token);

    Boolean isActiveToken(String token);

    void refreshTimeout(String token);

    void removeActiveToken(String token);

    void activateTokens(List<String> tokenList);
}
