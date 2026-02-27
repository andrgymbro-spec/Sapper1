package danila.www.sapper;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<SapperDto.ErrorResponse> handleBusinessException(RuntimeException ex) {
        return ResponseEntity
                .badRequest()
                .body(new SapperDto.ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SapperDto.ErrorResponse> handleGeneralException(Exception ex) {
        return ResponseEntity
                .internalServerError()
                .body(new SapperDto.ErrorResponse("Произошла внутренняя ошибка сервера"));
    }
}
