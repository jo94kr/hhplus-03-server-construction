package io.hhplus.server_construction.infra.user;

import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.domain.user.repoisitory.UserRepository;
import io.hhplus.server_construction.infra.user.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User findByUserById(Long userId) {
        return UserMapper.toDomain(userJpaRepository.findById(userId)
                .orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public User pessimisticFindById(Long userId) {
        return UserMapper.toDomain(userJpaRepository.pessimisticLockFindById(userId)
                .orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public User save(User user) {
        return UserMapper.toDomain(userJpaRepository.save(UserMapper.toEntity(user)));
    }
}
