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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = {Exception.class})
public class ConcertService {

    private final ConcertRepository concertRepository;

    /**
     * 콘서트 목록 조회
     * @param pageable 페이지 정보
     * @return Page<Concert>
     */
    public Page<Concert> findConcertList(Pageable pageable) {
        return concertRepository.findAllConcert(pageable);
    }

    /**
     * 콘서트 스케쥴 조회
     * @param concertId 콘서트 Id
     * @param startDate 날짜 범위 - 시작일
     * @param endDate 날짜 범위 - 종료일
     * @return List<ConcertSchedule>
     */
    public List<ConcertSchedule> findConcertScheduleList(Long concertId, LocalDate startDate, LocalDate endDate) {
        Concert concert = concertRepository.findConcertById(concertId);
        return concertRepository.findAllConcertSchedule(concert, startDate, endDate);
    }

    /**
     * 콘서트 좌석 조회
     * @param concertId 콘서트 Id
     * @param concertScheduleId 콘서트 스케쥴 Id
     * @return List<ConcertSeat>
     */
    public List<ConcertSeat> findAllConcertSeatList(Long concertId, Long concertScheduleId) {
        Concert concert = concertRepository.findConcertById(concertId);
        ConcertSchedule concertSchedule = concertRepository.findConcertScheduleById(concertScheduleId);
        return concertRepository.findAllConcertSeat(concert, concertSchedule);
    }

    /**
     * 좌석 예약
     * @param seatIdList 좌석 Id 목록
     * @throws ConcertException - ALREADY_RESERVATION: 이미 선택된 좌석
     * @return List<ConcertSeat>
     */
    @Transactional(rollbackFor = {Exception.class})
    public List<ConcertSeat> setSeatReservation(List<Long> seatIdList) {
        List<ConcertSeat> concertSeatList = new ArrayList<>();
            for (Long seatId : seatIdList) {
                ConcertSeat concertSeat = concertRepository.pessimisticLockFindById(seatId);
                if (!concertSeat.isPossible()) {
                    throw new ConcertException(ConcertExceptionEnums.ALREADY_RESERVATION);
                }

                // 좌석 임시 예약 상태로 변경
                concertSeatList.add(concertRepository.saveConcertSeat(concertSeat.changeStatus(ConcertSeatStatus.PENDING)));
            }
        return concertSeatList;
    }

    /**
     * 좌석 목록 저장
     * @param concertSeatList 좌석 목록
     */
    @Transactional(rollbackFor = {Exception.class})
    public void saveAllConcertSeat(List<ConcertSeat> concertSeatList) {
        concertRepository.saveAllConcertSeat(concertSeatList);
    }
}
