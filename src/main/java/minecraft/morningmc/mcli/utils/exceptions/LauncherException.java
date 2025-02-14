package minecraft.morningmc.mcli.utils.exceptions;

public class LauncherException extends Exception {

	public LauncherException() {
		super();
	}

	public LauncherException(String message) {
		super(message);
	}

	public LauncherException(Throwable cause) {
		super(cause);
	}

	public LauncherException(String message, Throwable cause) {
		super(message, cause);
	}
}
