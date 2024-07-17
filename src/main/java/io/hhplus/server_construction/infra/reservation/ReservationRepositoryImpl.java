package io.hhplus.server_construction.infra.reservation;

import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.ReservationItem;
import io.hhplus.server_construction.domain.reservation.repoisitory.ReservationRepository;
import io.hhplus.server_construction.domain.reservation.vo.ReservationStatus;
import io.hhplus.server_construction.infra.reservation.entity.ReservationItemEntity;
import io.hhplus.server_construction.infra.reservation.mapper.ReservationItemMapper;
import io.hhplus.server_construction.infra.reservation.mapper.ReservationMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;
    private final ReservationItemJpaRepository reservationItemJpaRepository;

    @Override
    public Reservation saveReservation(Reservation reservation) {
        return ReservationMapper.toDomain(reservationJpaRepository.save(ReservationMapper.toEntity(reservation)));
    }

    @Override
    public ReservationItem saveReservationItem(ReservationItem reservationItem) {
        return ReservationItemMapper.toDomain(reservationItemJpaRepository.save(ReservationItemMapper.toEntity(reservationItem)));
    }

    @Override
    public List<ReservationItem> saveAllReservationItems(List<ReservationItem> reservationItemList) {
        List<ReservationItemEntity> reservationItemEntityList = reservationItemJpaRepository.saveAll(reservationItemList.stream()
                .map(ReservationItemMapper::toEntity)
                .toList());

        return reservationItemEntityList.stream()
                .map(ReservationItemMapper::toDomain)
                .toList();
    }

    @Override
    public Reservation findReservationById(Long reservationId) {
        return ReservationMapper.toDomain(reservationJpaRepository.findById(reservationId)
                .orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public List<ReservationItem> findAllReservationItemByReservationId(Long reservationId) {
        return reservationItemJpaRepository.findByReservationId(reservationId).stream()
                .map(ReservationItemMapper::toDomain)
                .toList();
    }

    @Override
    public List<ReservationItem> findAllReservationItemByReservationIdIn(List<Long> reservationIdList) {
        return reservationItemJpaRepository.findByReservationIdIn(reservationIdList).stream()
                .map(ReservationItemMapper::toDomain)
                .toList();
    }

    @Override
    public List<Reservation> findReservationByStatusAndTargetDate(ReservationStatus status, LocalDateTime targetDate) {
        return reservationJpaRepository.findAllByStatusAndCreateDatetimeBefore(status, targetDate).stream()
                .map(ReservationMapper::toDomain)
                .toList();
    }

    @Override
    public List<Reservation> saveAllReservation(List<Reservation> reservationList) {
        return reservationJpaRepository.saveAll(reservationList.stream()
                        .map(ReservationMapper::toEntity)
                        .toList()).stream()
                .map(ReservationMapper::toDomain)
                .toList();
    }
}
