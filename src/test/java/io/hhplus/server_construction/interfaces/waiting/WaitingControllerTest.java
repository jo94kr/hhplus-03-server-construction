package io.hhplus.server_construction.interfaces.waiting;

import io.hhplus.server_construction.application.waiting.dto.CheckTokenResult;
import io.hhplus.server_construction.application.waiting.facade.WaitingFacade;
import io.hhplus.server_construction.domain.waiting.vo.WaitingStatus;
import io.hhplus.server_construction.interfaces.controller.waiting.WaitingController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WaitingController.class)
class WaitingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WaitingFacade waitingFacade;

    private static final String PATH = "/waiting";
    private static final String TOKEN = "DUMMY_TOKEN";

    @Test
    @DisplayName("토큰을 발급 받는다.")
    void createToken() throws Exception {
        // given
        // when
        when(waitingFacade.checkToken(null)).thenReturn(CheckTokenResult.create(TOKEN,
                5L,
                5L,
                WaitingStatus.WAITING,
                LocalDateTime.now()));
        ResultActions response = mockMvc.perform(get(PATH + "/check"));

        // then
        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("토큰정보를 체크한다.")
    void checkToken() throws Exception {
        // given
        // when
        when(waitingFacade.checkToken(TOKEN)).thenReturn(CheckTokenResult.create(TOKEN,
                5L,
                5L,
                WaitingStatus.WAITING,
                LocalDateTime.now()));
        ResultActions response = mockMvc.perform(get(PATH + "/check")
                .header("Authorization", TOKEN));

        // then
        response.andDo(print())
                .andExpect(status().isOk());
    }
}
