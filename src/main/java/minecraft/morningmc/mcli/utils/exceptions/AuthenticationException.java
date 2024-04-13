package minecraft.morningmc.mcli.utils.exceptions;

/**
 * An exception thrown when authentication fails.
 * This exception typically occurs when there is an issue with the user's credentials or authentication token.
 */
public class AuthenticationException extends LauncherException {
	
	/**
	 * Constructs a new {@code AuthenticationException} with no detail message.
	 */
	public AuthenticationException() {
	}
	
	/**
	 * Constructs a new {@code AuthenticationException} with the specified detail message and cause.
	 *
	 * @param message the detail message (which is saved for later retrieval by the getMessage() method).
	 * @param cause   the cause (which is saved for later retrieval by the getCause() method).
	 */
	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructs a new {@code AuthenticationException} with the specified detail message.
	 *
	 * @param message the detail message (which is saved for later retrieval by the getMessage() method).
	 */
	public AuthenticationException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new {@code AuthenticationException} with the specified cause.
	 *
	 * @param cause the cause (which is saved for later retrieval by the getCause() method).
	 */
	public AuthenticationException(Throwable cause) {
		super(cause);
	}
}