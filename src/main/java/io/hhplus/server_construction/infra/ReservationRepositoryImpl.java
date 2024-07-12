package io.hhplus.server_construction.infra;

import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.ReservationItem;
import io.hhplus.server_construction.domain.reservation.repoisitory.ReservationRepository;
import io.hhplus.server_construction.infra.entity.ReservationItemEntity;
import io.hhplus.server_construction.infra.mapper.ReservationItemMapper;
import io.hhplus.server_construction.infra.mapper.ReservationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
