package quentin.exceptions;

public class CacheClearException extends RuntimeException {
    public CacheClearException(String message, Throwable cause) {
        super(message, cause);
    }
}
