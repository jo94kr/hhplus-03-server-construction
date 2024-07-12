package io.hhplus.server_construction.application.user.facade;

import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public User findUserById(Long userId) {
        return userService.findUserById(userId);
    }
}
