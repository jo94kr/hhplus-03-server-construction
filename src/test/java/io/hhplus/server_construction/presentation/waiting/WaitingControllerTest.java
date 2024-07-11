package io.hhplus.server_construction.presentation.waiting;

import io.hhplus.server_construction.application.dto.CheckTokenResult;
import io.hhplus.server_construction.application.facade.WaitingFacade;
import io.hhplus.server_construction.domain.waiting.vo.WaitingStatus;
import io.hhplus.server_construction.presentation.concert.ConcertController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.Assert.state;

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
                .header("token", TOKEN));

        // then
        response.andDo(print())
                .andExpect(status().isOk());
    }
}
