package io.hhplus.server_construction.infra.concert;

import io.hhplus.server_construction.domain.concert.vo.ConcertSeatStatus;
import io.hhplus.server_construction.infra.concert.entity.ConcertScheduleEntity;
import io.hhplus.server_construction.infra.concert.entity.ConcertSeatEntity;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ConcertRepositoryImplTest {

    @Autowired
    ConcertJpaRepository concertJpaRepository;
    @Autowired
    ConcertScheduleJpaRepository concertScheduleJpaRepository;
    @Autowired
    ConcertSeatJpaRepository concertSeatJpaRepository;


    @Test
    void findAllConcertSeatByStatus() {
        ConcertScheduleEntity concertScheduleEntity = concertScheduleJpaRepository.findById(1L).orElseThrow(EntityNotFoundException::new);

        List<ConcertSeatEntity> concertSeatEntityList = concertSeatJpaRepository.findAllByConcertScheduleAndStatus(concertScheduleEntity, ConcertSeatStatus.POSSIBLE);
    }
}
