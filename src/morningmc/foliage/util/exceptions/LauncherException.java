package morningmc.foliage.util.exceptions;

public class LauncherException extends Exception {
    public LauncherException() {}

    public LauncherException(String message, Throwable cause) {
        super(message, cause);
    }

    public LauncherException(String message) {
        super(message);
    }

    public LauncherException(Throwable cause) {
        super(cause);
    }
}
