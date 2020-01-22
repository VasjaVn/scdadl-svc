package refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.component;

import refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.UpdateScdadlObjectException;

public class UpdateComponentWithWrongIdException extends UpdateScdadlObjectException {
    public UpdateComponentWithWrongIdException(String message) {
        super(message);
    }
}
