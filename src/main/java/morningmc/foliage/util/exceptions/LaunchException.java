package morningmc.foliage.util.exceptions;

public class LaunchException extends LauncherException {
    public LaunchException() {}

    public LaunchException(String message, Throwable cause) {
        super(message, cause);
    }

    public LaunchException(String message) {
        super(message);
    }

    public LaunchException(Throwable cause) {
        super(cause);
    }
}
