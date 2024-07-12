package io.hhplus.server_construction.presentation.user;

import io.hhplus.server_construction.application.user.facade.UserFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserFacade userFacade;

    private static final String PATH = "/users";

    @Test
    @DisplayName("사용자 잔액을 조회한다.")
    void amount() throws Exception {
        // given
        Long userId = 1L;

        // when
        ResultActions response = mockMvc.perform(get(PATH + "/" + userId + "/amount"));

        // then
        response.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(100));
    }

    @Test
    void charge() {
        // given

        // when

        // then
    }
}
