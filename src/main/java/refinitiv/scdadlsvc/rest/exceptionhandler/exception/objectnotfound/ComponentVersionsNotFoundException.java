package refinitiv.scdadlsvc.rest.exceptionhandler.exception.objectnotfound;

import refinitiv.scdadlsvc.rest.exceptionhandler.exception.ScdadlObjectNotFoundException;

public class ComponentVersionsNotFoundException extends ScdadlObjectNotFoundException {
    public ComponentVersionsNotFoundException(String message) {
        super(message);
    }
}
