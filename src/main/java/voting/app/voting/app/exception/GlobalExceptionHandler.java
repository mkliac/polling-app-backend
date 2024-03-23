package voting.app.voting.app.exception;

import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> ReshandleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex, WebRequest request) {
        String message =
                Objects.requireNonNull(ex.getBindingResult().getAllErrors())
                        .get(0)
                        .getDefaultMessage();
        ApiError apiError =
                ApiError.builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .message(message)
                        .path(request.getDescription(false).substring(4))
                        .build();
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
