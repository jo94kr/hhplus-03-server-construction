package io.hhplus.server_construction.application.user.facade;

import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public User findUserById(Long userId) {
        return userService.findUserById(userId);
    }

    public User charge(Long userId, BigDecimal amount) {
        User user = userService.findUserById(userId);
        return userService.charge(user, amount);
    }
}
