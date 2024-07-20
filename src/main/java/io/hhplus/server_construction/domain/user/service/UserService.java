package io.hhplus.server_construction.domain.user.service;

import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.domain.user.repoisitory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 사용자 조회
     * @param userId 사용자 Id
     * @return User
     */
    public User findUserById(Long userId) {
        return userRepository.findById(userId);
    }

    /**
     * 잔액 충전
     * @param user 사용자
     * @param amount 충전 금액
     * @return User
     */
    public User charge(User user, BigDecimal amount) {
        return userRepository.save(user.charge(amount));
    }

    /**
     * 잔액 사용
     * @param user 사용자
     * @param amount 사용 금액
     * @return User
     */
    public User use(User user, BigDecimal amount) {
        return userRepository.save(user.use(amount));
    }
}
