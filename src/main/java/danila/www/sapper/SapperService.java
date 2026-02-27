package danila.www.sapper;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class SapperService {
    private final Map<Long,GameInstance> gameInstanceMap= new ConcurrentHashMap<>();
    private final AtomicLong IdGen=new AtomicLong();

    public SapperDto.GameResponse createGame(SapperDto.NewGameRequest req){
        if(req.width()>30||req.height()>30||req.minesCount()>=req.width()*req.height()){
            throw new IllegalArgumentException("Invalid parameters");
        }
        long id =IdGen.getAndIncrement();
        GameInstance game = new GameInstance(id, req.width(), req.height(), req.minesCount());
        game.init();
        gameInstanceMap.put(id,game);
        return game.toResponse();
    }
    public SapperDto.GameResponse turn(SapperDto.TurnRequest request){
        GameInstance game = gameInstanceMap.get(request.gameId());
        if(game==null) throw new IllegalArgumentException("Игра не найдена");
        if(game.isCompleted()) throw new IllegalArgumentException("Игра уже завершена");
        game.uppdateTurn(request.col(),request.row());
        return game.toResponse();
    }
    @Scheduled(fixedRate = 600000)
    public void clean (){
        gameInstanceMap.entrySet().removeIf(e->e.getValue().getActivity().isBefore(LocalDateTime.now().minusMinutes(20)));
    }

    }

