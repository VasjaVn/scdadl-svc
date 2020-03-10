package refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.component;

import refinitiv.scdadlsvc.rest.exceptionhandler.exception.updateobject.UpdateScdadlObjectException;

public class UpdateComponentWithWrongPlatformNameException extends UpdateScdadlObjectException {
    public UpdateComponentWithWrongPlatformNameException(String message) {
        super(message);
    }
}
