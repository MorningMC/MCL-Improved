package minecraft.morningmc.mcli.utils.exceptions;

/**
 * The base exception class for exceptions related to the launcher.
 * It extends the standard Java {@link Exception} class.
 */
public class LauncherException extends Exception {
	
	/**
	 * Constructs a new {@link LauncherException} with no detail message.
	 */
	public LauncherException() {
		super();
	}
	
	/**
	 * Constructs a new {@link LauncherException} with the specified detail message.
	 *
	 * @param message The detail message (which is saved for later retrieval by the getMessage() method).
	 */
	public LauncherException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new {@link LauncherException} with the specified cause.
	 *
	 * @param cause The cause of the exception (which is saved for later retrieval by the getCause() method).
	 */
	public LauncherException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs a new {@link LauncherException} with the specified detail message and cause.
	 *
	 * @param message The detail message (which is saved for later retrieval by the getMessage() method).
	 * @param cause   The cause of the exception (which is saved for later retrieval by the getCause() method).
	 */
	public LauncherException(String message, Throwable cause) {
		super(message, cause);
	}
}