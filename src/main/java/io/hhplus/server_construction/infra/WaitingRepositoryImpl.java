package io.hhplus.server_construction.infra;

import io.hhplus.server_construction.domain.waiting.Waiting;
import io.hhplus.server_construction.domain.waiting.repoisitory.WaitingRepository;
import io.hhplus.server_construction.domain.waiting.vo.WaitingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WaitingRepositoryImpl implements WaitingRepository {

    @Override
    public Waiting findWaitingByToken(String token) {
        return null;
    }

    @Override
    public Waiting save(Waiting waiting) {
        return null;
    }

    @Override
    public Long findLastProceedingWaiting() {
        return 0L;
    }

    @Override
    public Long findThroughputPerMinute(LocalDateTime targetDatetime) {
        return 0L;
    }

    @Override
    public List<Waiting> findWaitingByStatusAndExpireDatetimeIsBefore(WaitingStatus waitingStatus, LocalDateTime targetDatetime) {
        return List.of();
    }

    @Override
    public void saveAll(List<Waiting> waitingList) {

    }

    @Override
    public List<Waiting> findWaitingByStatusAndRemainingDatetimeIsBefore(WaitingStatus waitingStatus, LocalDateTime targetDatetime) {
        return List.of();
    }
}
