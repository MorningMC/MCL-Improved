package minecraft.morningmc.mcli.utils.exceptions;

/**
 * An exception thrown when authentication fails.
 * This exception typically occurs when there is an issue with the user's credentials or authentication token.
 */
public class AuthenticationException extends LauncherException {
	
	/**
	 * Constructs a new {@link AuthenticationException} with no detail message.
	 */
	public AuthenticationException() {
		super();
	}
	
	/**
	 * Constructs a new {@link AuthenticationException} with the specified detail message.
	 *
	 * @param message The detail message (which is saved for later retrieval by the getMessage() method).
	 */
	public AuthenticationException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new {@link AuthenticationException} with the specified cause.
	 *
	 * @param cause The cause of the exception (which is saved for later retrieval by the getCause() method).
	 */
	public AuthenticationException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs a new {@link AuthenticationException} with the specified detail message and cause.
	 *
	 * @param message The detail message (which is saved for later retrieval by the getMessage() method).
	 * @param cause   The cause of the exception (which is saved for later retrieval by the getCause() method).
	 */
	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}
}