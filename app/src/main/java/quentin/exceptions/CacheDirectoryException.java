package quentin.exceptions;

public class CacheDirectoryException extends RuntimeException {

    private static final long serialVersionUID = 7784377231053533086L;

    public CacheDirectoryException(String message) {
        super(message);
    }
}
