package io.hhplus.server_construction.infra.waiting;

import io.hhplus.server_construction.domain.waiting.Waiting;
import io.hhplus.server_construction.domain.waiting.repoisitory.WaitingRepository;
import io.hhplus.server_construction.domain.waiting.vo.WaitingStatus;
import io.hhplus.server_construction.infra.waiting.mapper.WaitingMapper;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static io.hhplus.server_construction.domain.waiting.vo.WaitingConstant.*;

@Repository
@RequiredArgsConstructor
public class WaitingRepositoryImpl implements WaitingRepository {

    private final WaitingJpaRepository waitingJpaRepository;

    private final StringRedisTemplate redisTemplate;
    private ZSetOperations<String, String> zSetOperations;
    private SetOperations<String, String> setOperations;

    @PostConstruct
    public void init() {
        zSetOperations = redisTemplate.opsForZSet();
        setOperations = redisTemplate.opsForSet();
    }

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
        return waitingJpaRepository.findWaitingEntity(status, PageRequest.of(0, 1))
                .orElse(0L);
    }

    @Override
    public List<Waiting> findAllByStatusNotAndExpiredDatetimeIsBefore(WaitingStatus waitingStatus, LocalDateTime targetDatetime) {
        return waitingJpaRepository.findAllByStatusNotAndExpiredDatetimeIsBefore(waitingStatus, targetDatetime).stream()
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

    @Override
    public Boolean addWaitingQueue(String token) {
        long score = System.currentTimeMillis();
        return zSetOperations.add(WAITING_KEY, token, score);
    }

    @Override
    public Long findWaitingRank(String token) {
        return zSetOperations.rank(WAITING_KEY, token);
    }

    @Override
    public Long removeWaitingQueue(String token) {
        return zSetOperations.remove(WAITING_KEY, token);
    }

    @Override
    public Long findQueueCnt(String key) {
        return (long) Objects.requireNonNull(redisTemplate.keys(key + "*")).size();
    }

    @Override
    public void addActiveQueue(String token) {
        String key = ACTIVE_KEY_PREFIX + token;
        setOperations.add(key, token);
        redisTemplate.expire(key, 10, TimeUnit.MINUTES);
    }
}
