package minecraft.morningmc.mcli.utils.exceptions;

/**
 * {@code LauncherException} is the base exception class for exceptions related to the launcher.
 * It extends the standard Java {@code Exception} class.
 */
public class LauncherException extends Exception {
	
	/**
	 * Constructs a new {@code LauncherException} with no detail message.
	 */
	public LauncherException() {
		super();
	}
	
	/**
	 * Constructs a new {@code LauncherException} with the specified detail message.
	 *
	 * @param message The detail message.
	 */
	public LauncherException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new {@code LauncherException} with the specified cause.
	 *
	 * @param cause The cause of the exception.
	 */
	public LauncherException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs a new {@code LauncherException} with the specified detail message and cause.
	 *
	 * @param message The detail message.
	 * @param cause The cause of the exception.
	 */
	public LauncherException(String message, Throwable cause) {
		super(message, cause);
	}
}