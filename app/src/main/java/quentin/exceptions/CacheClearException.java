package quentin.exceptions;

public class CacheClearException extends RuntimeException {
    private static final long serialVersionUID = 7922687837294525961L;

    public CacheClearException(String message, Throwable cause) {
        super(message, cause);
    }
}
