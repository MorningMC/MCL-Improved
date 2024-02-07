package minecraft.morningmc.mcli.utils.exceptions;

/**
 * {@code IllegalNbtException} is an exception class indicating issues related to NBT (Named Binary Tag) data.
 */
public class IllegalNbtException extends LauncherException {
	
	/**
	 * Constructs a new {@code IllegalNbtException} with no detail message.
	 */
	public IllegalNbtException() {
		super();
	}
	
	/**
	 * Constructs a new {@code IllegalNbtException} with the specified detail message.
	 *
	 * @param message The detail message.
	 */
	public IllegalNbtException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new {@code IllegalNbtException} with the specified cause.
	 *
	 * @param cause The cause of the exception.
	 */
	public IllegalNbtException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs a new {@code IllegalNbtException} with the specified detail message and cause.
	 *
	 * @param message The detail message.
	 * @param cause The cause of the exception.
	 */
	public IllegalNbtException(String message, Throwable cause) {
		super(message, cause);
	}
}