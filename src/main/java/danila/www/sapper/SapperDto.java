package danila.www.sapper;

import com.fasterxml.jackson.annotation.JsonProperty;


public class SapperDto {
    public record NewGameRequest(
            int width,
            int height,
            @JsonProperty("mines_count") int minesCount
    ) {}
    public record TurnRequest(
            @JsonProperty("game_id") long gameId, // Изменили String на long
            int col,
            int row
    ) {}
    public record GameResponse(
            @JsonProperty("game_id") long gameId, // Изменили String на long
            int width,
            int height,
            @JsonProperty("mines_count") int minesCount,
            boolean completed,
            String[][] field
    ) {}

    public record ErrorResponse(String error) {}
}