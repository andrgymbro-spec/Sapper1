package danila.www.sapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.Random;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Getter
public class GameInstance {
    private final Long id;
    private final int width, height, minesCount;
    private char [] [] board;
    private String [] [] view;
    private boolean completed=false;
    private LocalDateTime activity = LocalDateTime.now();

    public void init(){
        this.board = new char[height][width];
        this.view = new String[height][width];
        IntStream.range(0,height).forEach(i->{
            Arrays.fill(board[i],'0');
            Arrays.fill(view[i],"-");
        });
        new Random().ints(0,width*height)
                .distinct()
                .limit(minesCount)
                .forEach(ps-> board[ps/width][ps%height]='X');
        IntStream.range(0, height).parallel().forEach(r -> {
            for (int c = 0; c < width; c++) {
                if (board[r][c] != 'X') {
                    long count = countNearbyMines(r, c);
                    board[r][c] = (char) (count + '0');
                }
            }
        });
    }
    private long countNearbyMines(int r, int c) {
        int count = 0;

        for (int nr = r - 1; nr <= r + 1; nr++) {
            for (int nc = c - 1; nc <= c + 1; nc++) {
                // Проверка границ и проверка на мину
                if (isSafe(nr, nc) && board[nr][nc] == 'X') {
                    count++;
                }
            }
        }
         return count;
    }
    public void uppdateTurn(int col, int row) {
        this.activity = LocalDateTime.now();
        if (board[row][col] == 'X') {
            finish(false);
        } else {
            openBFS(col, row);
            if (checkWin()) finish(true);
        }
    }
    private void openBFS(int startCol, int startRow) {
        Deque<int[]> deque = new ArrayDeque<>();
        deque.add(new int[]{startRow, startCol});

        while (!deque.isEmpty()) {
            int[] curr = deque.poll();
            int r = curr[0], c = curr[1];
            if (!isSafe(r, c) || !view[r][c].equals("-"))
                continue;
            view[r][c] = String.valueOf(board[r][c]);
            if (board[r][c] == '0') {
                for(int i = -1;i<=1;i++){
                    for(int j = -1;j<=1;j++)
                        if(i!=0||j!=0) {
                            deque.add(new int[]{r + i, c + j});
                        }
                }
            }
        }
    }
        private boolean isSafe(int r, int c) {
            return r >= 0 && r < height && c >= 0 && c < width;
        }
        private boolean checkWin() {
            long opened = Arrays.stream(view).flatMap(Arrays::stream).filter(s -> !s.equals("-")).count();
            return opened == (long) width * height - minesCount;
        }
        private void finish(boolean won) {
            this.completed = true;
            IntStream.range(0, height).parallel().forEach(r -> {
                for (int c = 0; c < width; c++) {
                    if (board[r][c] == 'X') view[r][c] = won ? "M" : "X";
                    else view[r][c] = String.valueOf(board[r][c]);
                }
            });
        }
        public SapperDto.GameResponse toResponse() {
            return new SapperDto.GameResponse(id, width, height, minesCount, completed, view);
        }
    }
