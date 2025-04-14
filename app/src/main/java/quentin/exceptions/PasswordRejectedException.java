package quentin.exceptions;

import java.io.Serial;

public class PasswordRejectedException extends RuntimeException {

    @Serial private static final long serialVersionUID = -1120747489107356171L;

    @Override
    public String getMessage() {
        return "Too many attempts";
    }
}
