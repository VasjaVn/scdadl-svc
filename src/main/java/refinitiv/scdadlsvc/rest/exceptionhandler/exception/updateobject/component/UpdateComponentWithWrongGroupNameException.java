package refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.component;

import refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.UpdateScdadlObjectException;

public class UpdateComponentWithWrongGroupNameException extends UpdateScdadlObjectException {
    public UpdateComponentWithWrongGroupNameException(String message) {
        super(message);
    }
}
