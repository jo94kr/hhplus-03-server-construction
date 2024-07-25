package io.hhplus.server_construction.application.user.facade;

import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.domain.user.exceprtion.UserException;
import io.hhplus.server_construction.domain.user.exceprtion.UserExceptionEnums;
import io.hhplus.server_construction.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
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

    public User charge(Long userId, BigDecimal amount) {
        try {
            User user = userService.findUserById(userId);
            return userService.charge(user, amount);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new UserException(UserExceptionEnums.ALREADY_CHARGE);
        }
    }
}
