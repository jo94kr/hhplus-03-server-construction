package io.hhplus.server_construction.infra.user;


import io.hhplus.server_construction.infra.user.entity.UserEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM UserEntity s WHERE s.id = :userId")
    Optional<UserEntity> pessimisticLockFindById(@Param("userId") Long userId);
}
