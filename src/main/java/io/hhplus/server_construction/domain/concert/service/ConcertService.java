package io.hhplus.server_construction.domain.concert.service;

import io.hhplus.server_construction.domain.concert.Concert;
import io.hhplus.server_construction.domain.concert.ConcertSchedule;
import io.hhplus.server_construction.domain.concert.ConcertSeat;
import io.hhplus.server_construction.domain.concert.exceprtion.ConcertException;
import io.hhplus.server_construction.domain.concert.exceprtion.ConcertExceptionEnums;
import io.hhplus.server_construction.domain.concert.repoisitory.ConcertRepository;
import io.hhplus.server_construction.domain.concert.vo.ConcertSeatStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    public Page<Concert> findConcertList(Pageable pageable) {
        return concertRepository.findAllConcert(pageable);
    }

    public List<ConcertSchedule> findConcertScheduleList(Long concertId, LocalDate startDate, LocalDate endDate) {
        Concert concert = concertRepository.findConcertById(concertId);
        return concertRepository.findAllConcertSchedule(concert, startDate, endDate);
    }

    public List<ConcertSeat> findAllConcertSeatList(Long concertId, Long concertScheduleId) {
        Concert concert = concertRepository.findConcertById(concertId);
        ConcertSchedule concertSchedule = concertRepository.findConcertScheduleById(concertScheduleId);
        return concertRepository.findAllConcertSeat(concert, concertSchedule);
    }

    public List<ConcertSeat> reservationSeat(List<Long> seatIdList) {
        List<ConcertSeat> concertSeatList = new ArrayList<>();
        try {
            for (Long seatId : seatIdList) {
                ConcertSeat concertSeat = concertRepository.findConcertSeatById(seatId);
                if (!concertSeat.isPossible()) {
                    throw new ConcertException(ConcertExceptionEnums.ALREADY_RESERVATION);
                }

                // 좌석 임시 예약 상태로 변경
                concertSeatList.add(concertRepository.saveConcertSeat(concertSeat.changeStatus(ConcertSeatStatus.PENDING)));
            }

        } catch (ObjectOptimisticLockingFailureException e) {
            throw new ConcertException(ConcertExceptionEnums.ALREADY_RESERVATION);
        }

        return concertSeatList;
    }

    public void saveAllConcertSeat(List<ConcertSeat> concertSeatList) {
        concertRepository.saveAllConcertSeat(concertSeatList);
    }
}
