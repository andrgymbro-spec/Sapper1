package danila.www.sapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameInstanceTest {
    private GameInstance game;

    @BeforeEach
    void setUp() {
        game = new GameInstance(1L, 3, 3, 1);
        game.init();
    }

    @Test
    void testInitFillsBoardAndMines() {
        assertNotNull(game.getBoard());
        long minesCount = java.util.Arrays.stream(game.getBoard())
                .flatMap(row -> new String(row).chars().mapToObj(c -> (char) c))
                .filter(c -> c == 'X').count();
        assertEquals(1, minesCount, "Должна быть ровно одна мина");
    }
    @Test
    void testWinCondition() {
        char[][] board = game.getBoard();
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if(board[c][r]!='X'){
                    game.uppdateTurn(c, r);
                }
            }
        }
        assertTrue(game.isCompleted(), "Игра должна быть завершена победой");
    }
    @Test
    void testLoseCondition() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (game.getBoard()[r][c] == 'X') {
                    game.uppdateTurn(c, r);
                    assertTrue(game.isCompleted(), "Игра должна быть завершена проигрышем");
                    return;
                }
            }
        }
    }
}