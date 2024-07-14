package io.hhplus.server_construction.infra.user;


import io.hhplus.server_construction.infra.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

}
