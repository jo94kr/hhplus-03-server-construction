package io.hhplus.server_construction.application.user.facade;

import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.domain.user.exceprtion.UserException;
import io.hhplus.server_construction.domain.user.exceprtion.UserExceptionEnums;
import io.hhplus.server_construction.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {

    @InjectMocks
    private UserFacade userFacade;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("사용자를 조회한다.")
    void findUserById() {
        // given
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        User user = User.create(1L, "조진우", BigDecimal.valueOf(1000L), now, now, 0L);

        // when
        when(userService.findUserById(userId)).thenReturn(user);
        User result = userFacade.findUserById(userId);

        // then
        assertNotNull(result);
    }

    @Test
    @DisplayName("사용자의 잔액을 0원 이하로 충전할 경우 예외")
    void chargeAmountException() {
        // given
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();
        User user = User.create(1L, "조진우", BigDecimal.valueOf(1000L), now, now, 0L);
        BigDecimal chargeAmount = BigDecimal.valueOf(-100L);

        // when
        when(userService.findUserById(userId)).thenReturn(user);
        when(userService.charge(user, chargeAmount)).thenThrow(new UserException(UserExceptionEnums.INVALID_AMOUNT_VALUE));

        // then
        assertThatThrownBy(() -> userFacade.charge(userId, chargeAmount))
                .isInstanceOf(UserException.class)
                .hasMessageContaining(UserExceptionEnums.INVALID_AMOUNT_VALUE.getMessage());
    }
}
