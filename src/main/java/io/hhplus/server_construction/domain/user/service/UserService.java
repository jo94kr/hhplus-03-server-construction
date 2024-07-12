package io.hhplus.server_construction.domain.user.service;

import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.domain.user.repoisitory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findUserById(Long userId) {
        return userRepository.findById(userId);
    }
}
