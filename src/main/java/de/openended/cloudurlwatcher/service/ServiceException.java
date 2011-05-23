package de.openended.cloudurlwatcher.service;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -7691541998967889847L;

    public ServiceException() {
        super();
    }

    public ServiceException(String arg0) {
        super(arg0);
    }

    public ServiceException(Throwable arg0) {
        super(arg0);
    }

    public ServiceException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
