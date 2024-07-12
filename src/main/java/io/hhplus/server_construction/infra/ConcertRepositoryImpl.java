package io.hhplus.server_construction.infra;

import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.repoisitory.ConcertRepository;
import io.hhplus.server_construction.infra.mapper.ConcertMapper;
import io.hhplus.server_construction.infra.mapper.ConcertScheduleMapper;
import io.hhplus.server_construction.infra.mapper.ConcertSeatMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
    private final ConcertSeatJpaRepository concertSeatJpaRepository;

    @Override
    public Concert findConcertById(Long concertId) {
        return ConcertMapper.toDomain(concertJpaRepository.findById(concertId)
                .orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public Page<Concert> findAllConcert(Pageable pageable) {
        return concertJpaRepository.findAll(pageable)
                .map(ConcertMapper::toDomain);
    }

    @Override
    public List<ConcertSchedule> findAllConcertSchedule(Concert concert, LocalDate startDate, LocalDate endDate) {
        return concertScheduleJpaRepository.findAllByConcertAndConcertDatetimeBetween(ConcertMapper.toEntity(concert),
                        startDate.atStartOfDay(),
                        endDate.atTime(LocalTime.MAX)).stream()
                .map(ConcertScheduleMapper::toDomain)
                .toList();
    }

    @Override
    public ConcertSchedule findConcertScheduleById(Long concertScheduleId) {
        return ConcertScheduleMapper.toDomain(concertScheduleJpaRepository.findById(concertScheduleId)
                .orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public List<ConcertSeat> findAllConcertSeat(Concert concert, ConcertSchedule concertSchedule) {
        return concertSeatJpaRepository.findAllByConcertSchedule_ConcertAndConcertSchedule(ConcertMapper.toEntity(concert),
                        ConcertScheduleMapper.toEntity(concertSchedule)).stream()
                .map(ConcertSeatMapper::toDomain)
                .toList();
    }

    @Override
    public ConcertSeat pessimisticLockFindById(Long concertSeatId) {
        return ConcertSeatMapper.toDomain(concertSeatJpaRepository.pessimisticLockFindById(concertSeatId)
                .orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public ConcertSeat saveConcertSeat(ConcertSeat concertSeat) {
        return ConcertSeatMapper.toDomain(concertSeatJpaRepository.save(ConcertSeatMapper.toEntity(concertSeat)));
    }

    @Override
    public void saveAllConcertSeat(List<ConcertSeat> temporarySeatList) {
        concertSeatJpaRepository.saveAll(temporarySeatList.stream()
                .map(ConcertSeatMapper::toEntity)
                .toList());
    }
}
