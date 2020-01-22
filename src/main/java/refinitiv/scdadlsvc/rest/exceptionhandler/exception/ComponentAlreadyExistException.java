package refinitiv.scdadlsvc.rest.exceptionhandler.exception;

public class ComponentAlreadyExistException extends RuntimeException {
    public ComponentAlreadyExistException(String message) {
        super(message);
    }
}
