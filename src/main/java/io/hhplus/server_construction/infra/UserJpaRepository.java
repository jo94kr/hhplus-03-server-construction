package io.hhplus.server_construction.infra;

import io.hhplus.server_construction.infra.entity.ReservationEntity;
import io.hhplus.server_construction.infra.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

}
