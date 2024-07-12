package io.hhplus.server_construction.infra;

import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.ReservationItem;
import io.hhplus.server_construction.domain.reservation.repoisitory.ReservationRepository;
import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.domain.user.repoisitory.UserRepository;
import io.hhplus.server_construction.infra.entity.ReservationItemEntity;
import io.hhplus.server_construction.infra.mapper.ReservationItemMapper;
import io.hhplus.server_construction.infra.mapper.ReservationMapper;
import io.hhplus.server_construction.infra.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User findById(Long userId) {
        return UserMapper.toDomain(userJpaRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public User save(User user) {
        return UserMapper.toDomain(userJpaRepository.save(UserMapper.toEntity(user)));
    }
}
