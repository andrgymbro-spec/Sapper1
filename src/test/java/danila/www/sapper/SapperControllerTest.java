package danila.www.sapper;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SapperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SapperService sapperService;

    @Test
    @DisplayName("Успешное создание новой игры")
    void testCreateGameSuccess() throws Exception {
        SapperDto.GameResponse mockResponse = new SapperDto.GameResponse(
                1L, 10, 10, 10, false, new String[10][10]
        );
        when(sapperService.createGame(any())).thenReturn(mockResponse);
        mockMvc.perform(post("/api/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"width\":10, \"height\":10, \"mines_count\":10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.game_id").value(1));
    }
    @Test
    @DisplayName("Обработка ошибки при неверном ходе")
    void testTurnError() throws Exception {
        // Убедись, что в сервисе метод называется turn()
        when(sapperService.turn(any()))
                .thenThrow(new IllegalArgumentException("Игра уже завершена"));

        mockMvc.perform(post("/api/turn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"game_id\":1, \"col\":0, \"row\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Игра уже завершена"));
    }
}