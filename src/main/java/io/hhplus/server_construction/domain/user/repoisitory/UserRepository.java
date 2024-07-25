package io.hhplus.server_construction.domain.user.repoisitory;


import io.hhplus.server_construction.domain.user.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;

public interface UserRepository {

    User findByUserById(Long userId);

    User pessimisticFindById(Long userId);

    User save(User charge);
}
