package io.hhplus.server_construction.domain.user.repoisitory;


import io.hhplus.server_construction.domain.user.User;

public interface UserRepository {

    User findById(Long userId);

    User save(User charge);
}
