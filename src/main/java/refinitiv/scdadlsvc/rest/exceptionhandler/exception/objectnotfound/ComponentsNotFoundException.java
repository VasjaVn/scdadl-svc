package refinitiv.scdadlsvc.rest.exceptionhandler.exception.objectnotfound;

import refinitiv.scdadlsvc.rest.exceptionhandler.exception.ScdadlObjectNotFoundException;

public class ComponentsNotFoundException extends ScdadlObjectNotFoundException {
    public ComponentsNotFoundException(String message) {
        super(message);
    }
}
