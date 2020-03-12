package refinitiv.scdadlsvc.rest.exceptionhandler.exception;

public class ScdadlObjectNotFoundException extends RuntimeException {
    public ScdadlObjectNotFoundException(String message) {
        super(message);
    }
}
