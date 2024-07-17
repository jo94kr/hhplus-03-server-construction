package io.hhplus.server_construction.application.user.facade;

import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true, rollbackFor = {Exception.class})
public class UserFacade {

    private final UserService userService;

    public User findUserById(Long userId) {
        return userService.findUserById(userId);
    }

    @Transactional(rollbackFor = {Exception.class})
    public User charge(Long userId, BigDecimal amount) {
        User user = userService.findUserById(userId);
        return userService.charge(user, amount);
    }
}
