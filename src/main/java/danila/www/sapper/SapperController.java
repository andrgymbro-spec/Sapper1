package danila.www.sapper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class SapperController {
    private final SapperService service;

    @PostMapping("/new")
    public ResponseEntity<?> create(@RequestBody SapperDto.NewGameRequest req) {
        try {
            return ResponseEntity.ok(service.createGame(req));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/turn")
    public ResponseEntity<?> turn(@RequestBody SapperDto.TurnRequest req) {
        try {
            return ResponseEntity.ok(service.turn(req));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
