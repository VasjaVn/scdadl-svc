package refinitiv.scdadlsvc.rest.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.ComponentAlreadyExistException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.ReqParamIdAndDtoIdNotEqualsException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.createobject.CreateScdadlObjectException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.objectnotfound.ScdadlObjectNotFoundException;
import refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.UpdateScdadlObjectException;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@Slf4j
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(ComponentAlreadyExistException.class)
    public ResponseEntity handleConflict(ComponentAlreadyExistException ex) {
        log.error("CONFLICT: {}", ex.getMessage());
        return ResponseEntity.status(CONFLICT).build();
    }

    @ExceptionHandler(ScdadlObjectNotFoundException.class)
    public ResponseEntity handleNotFound(Exception ex) {
        log.error("NOT_FOUND: {}", ex.getMessage());
        return ResponseEntity.status(NOT_FOUND).build();
    }

    @ExceptionHandler({ CreateScdadlObjectException.class,
            UpdateScdadlObjectException.class,
            ReqParamIdAndDtoIdNotEqualsException.class,
            MethodArgumentTypeMismatchException.class })
    public ResponseEntity handleBadRequest(Exception ex) {
        log.error("BAD_REQUEST: {}", ex.getMessage());
        return ResponseEntity.status(BAD_REQUEST).build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleBadRequestForMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.error("BAD_REQUEST: {}", message);
        return ResponseEntity.status(BAD_REQUEST).build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {
        log.error("METHOD_NOT_ALLOWED: {}", ex.getMessage());
        return ResponseEntity.status(METHOD_NOT_ALLOWED).build();
    }
}
