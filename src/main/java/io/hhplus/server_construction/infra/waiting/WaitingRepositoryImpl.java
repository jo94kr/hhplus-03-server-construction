package io.hhplus.server_construction.infra.waiting;

import io.hhplus.server_construction.domain.waiting.Waiting;
import io.hhplus.server_construction.domain.waiting.repoisitory.WaitingRepository;
import io.hhplus.server_construction.domain.waiting.vo.WaitingStatus;
import io.hhplus.server_construction.infra.waiting.mapper.WaitingMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WaitingRepositoryImpl implements WaitingRepository {

    private final WaitingJpaRepository waitingJpaRepository;

    @Override
    public Waiting findWaitingByToken(String token) {
        return WaitingMapper.toDomain(waitingJpaRepository.findByToken(token)
                .orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public Waiting save(Waiting waiting) {
        return WaitingMapper.toDomain(waitingJpaRepository.save(WaitingMapper.toEntity(waiting)));
    }

    @Override
    public Long findLastProceedingWaiting(WaitingStatus status) {
        return waitingJpaRepository.findLastProceedingWaiting(status)
                .orElse(0L);
    }

    @Override
    public List<Waiting> findWaitingByStatusAndExpireDatetimeIsBefore(WaitingStatus waitingStatus, LocalDateTime targetDatetime) {
        return waitingJpaRepository.findAllByStatusAndExpiredDatetimeIsBefore(waitingStatus, targetDatetime).stream()
                .map(WaitingMapper::toDomain)
                .toList();
    }

    @Override
    public void saveAll(List<Waiting> waitingList) {
        waitingJpaRepository.saveAll(waitingList.stream()
                .map(WaitingMapper::toEntity)
                .toList());
    }

    @Override
    public List<Waiting> findWaitingByStatusAndAccessDatetimeIsBefore(WaitingStatus waitingStatus, LocalDateTime targetDatetime) {
        return waitingJpaRepository.findWaitingByStatusAndAccessDatetimeIsBefore(waitingStatus, targetDatetime).stream()
                .map(WaitingMapper::toDomain)
                .toList();
    }
}