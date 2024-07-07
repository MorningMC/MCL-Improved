package minecraft.morningmc.mcli.utils.exceptions;

/**
 * An exception class indicating issues related to NBT (Named Binary Tag) data.
 */
public class IllegalNbtException extends LauncherException {
	
	/**
	 * Constructs a new {@link IllegalNbtException} with no detail message.
	 */
	public IllegalNbtException() {
		super();
	}
	
	/**
	 * Constructs a new {@link IllegalNbtException} with the specified detail message.
	 *
	 * @param message The detail message (which is saved for later retrieval by the getMessage() method).
	 */
	public IllegalNbtException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new {@link IllegalNbtException} with the specified cause.
	 *
	 * @param cause The cause of the exception (which is saved for later retrieval by the getCause() method).
	 */
	public IllegalNbtException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs a new {@link IllegalNbtException} with the specified detail message and cause.
	 *
	 * @param message The detail message (which is saved for later retrieval by the getMessage() method).
	 * @param cause   The cause of the exception (which is saved for later retrieval by the getCause() method).
	 */
	public IllegalNbtException(String message, Throwable cause) {
		super(message, cause);
	}
}