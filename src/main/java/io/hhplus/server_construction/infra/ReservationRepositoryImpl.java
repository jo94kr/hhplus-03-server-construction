package io.hhplus.server_construction.infra;

import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.ReservationItem;
import io.hhplus.server_construction.domain.reservation.repoisitory.ReservationRepository;
import io.hhplus.server_construction.domain.reservation.vo.ReservationStatusEnums;
import io.hhplus.server_construction.infra.entity.ReservationItemEntity;
import io.hhplus.server_construction.infra.mapper.ReservationItemMapper;
import io.hhplus.server_construction.infra.mapper.ReservationMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.management.relation.RelationService;
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
    public List<Reservation> findTemporaryReservationSeatByTargetDate(ReservationStatusEnums status, LocalDateTime targetDate) {
        return reservationJpaRepository.findAllByStatusAndCreateDatetimeAfter(status, targetDate).stream()
                .map(ReservationMapper::toDomain)
                .toList();
    }

    @Override
    public List<Reservation> saveAllReservation(List<Reservation> temporaryReservationList) {
        return reservationJpaRepository.saveAll(temporaryReservationList.stream()
                        .map(ReservationMapper::toEntity)
                        .toList()).stream()
                .map(ReservationMapper::toDomain)
                .toList();
    }
}
