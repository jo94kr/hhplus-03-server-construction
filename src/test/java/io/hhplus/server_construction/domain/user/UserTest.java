package io.hhplus.server_construction.domain.user;

import io.hhplus.server_construction.domain.user.exceprtion.UserException;
import io.hhplus.server_construction.domain.user.exceprtion.UserExceptionEnums;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    @DisplayName("0보다 작거나 같은 금액 충전")
    void chargeLessThanZero() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.create(1L, "조진우", BigDecimal.valueOf(10000L), now, now, 0L);
        BigDecimal zero = BigDecimal.ZERO;

        // when
        // then
        assertThatThrownBy(() -> user.charge(zero))
                .isInstanceOf(UserException.class)
                .hasMessageContaining(UserExceptionEnums.INVALID_AMOUNT_VALUE.getMessage());
    }

    @Test
    @DisplayName("사용 하려는 금액이 0보다 작거나 같은경우")
    void useLessThanZero() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.create(1L, "조진우", BigDecimal.valueOf(10000L), now, now, 0L);
        BigDecimal zero = BigDecimal.ZERO;

        // when
        // then
        assertThatThrownBy(() -> user.use(zero))
                .isInstanceOf(UserException.class)
                .hasMessageContaining(UserExceptionEnums.INVALID_AMOUNT_VALUE.getMessage());
    }

    @Test
    @DisplayName("사용 하려는 금액이 보유중인 금액보다 많을 같은경우")
    void useBiggerThanAmountHeld() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User user = User.create(1L, "조진우", BigDecimal.valueOf(10000L), now, now, 0L);
        BigDecimal useAmount = BigDecimal.valueOf(50000);

        // when
        // then
        assertThatThrownBy(() -> user.use(useAmount))
                .isInstanceOf(UserException.class)
                .hasMessageContaining(UserExceptionEnums.INVALID_AMOUNT_VALUE.getMessage());
    }
}
