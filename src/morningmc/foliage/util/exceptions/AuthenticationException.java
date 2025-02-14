package morningmc.foliage.util.exceptions;

public class AuthenticationException extends LauncherException {
    public AuthenticationException() {
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}
