package refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.componentstack;

import refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.UpdateScdadlObjectException;

public class UpdateComponentStackWithWrongIdException extends UpdateScdadlObjectException {
    public UpdateComponentStackWithWrongIdException(String message) {
        super(message);
    }
}
