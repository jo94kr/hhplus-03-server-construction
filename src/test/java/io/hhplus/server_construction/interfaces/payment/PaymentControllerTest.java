package io.hhplus.server_construction.interfaces.payment;

import io.hhplus.server_construction.application.payment.dto.PaymentCommand;
import io.hhplus.server_construction.application.payment.dto.PaymentResult;
import io.hhplus.server_construction.application.payment.facade.PaymentFacade;
import io.hhplus.server_construction.domain.payment.Payment;
import io.hhplus.server_construction.domain.reservation.Reservation;
import io.hhplus.server_construction.domain.reservation.vo.ReservationStatus;
import io.hhplus.server_construction.domain.user.User;
import io.hhplus.server_construction.interfaces.controller.payment.PaymentController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PaymentFacade paymentFacade;

    private static final String PATH = "/payment";
    private static final String TOKEN = "DUMMY_TOKEN";

    @Test
    @DisplayName("예약한 좌석을 결제한다.")
    void payment() throws Exception {
        // given
        Long reservationId = 1L;
        Long userId = 1L;
        String requestBody = """
                {
                    "userId": 1,
                    "reservationId": 1
                }
                """;

        PaymentCommand paymentCommand = new PaymentCommand(userId, reservationId);
        LocalDateTime now = LocalDateTime.now();
        User user = User.create(1L, "조진우", BigDecimal.valueOf(1000L), now, now);
        Reservation reservation = Reservation.create(1L,
                user,
                ReservationStatus.PAYMENT_WAITING,
                BigDecimal.valueOf(1000L),
                0L);
        Payment payment = Payment.pay(reservation, user);
        PaymentResult paymentResult = PaymentResult.create(payment);


        // when
        when(paymentFacade.payment(paymentCommand, TOKEN)).thenReturn(paymentResult);
        ResultActions response = mockMvc.perform(post(PATH)
                .header("Authorization", TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));

        // then
        response.andDo(print())
                .andExpect(status().isOk());
    }
}
