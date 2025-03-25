package quentin.exceptions;

public class PasswordRejectedException extends RuntimeException {

    private static final long serialVersionUID = -1120747489107356171L;

    @Override
    public String getMessage() {
        return "Too many attempts";
    }
}
