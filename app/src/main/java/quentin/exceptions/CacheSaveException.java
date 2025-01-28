package quentin.exceptions;

public class CacheSaveException extends RuntimeException {
    private static final long serialVersionUID = 3311314235858597788L;

    public CacheSaveException(String message) {
        super(message);
    }
}
