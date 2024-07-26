package io.hhplus.server_construction.application.user.facade;

import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.domain.user.service.UserService;
import io.hhplus.server_construction.support.aop.annotation.RedissonLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public User findUserById(Long userId) {
        return userService.findUserById(userId);
    }

    @Transactional
    public User charge(Long userId, BigDecimal amount) {
        User user = userService.pessimisticFindById(userId);
        return userService.charge(user, amount);
    }
}
