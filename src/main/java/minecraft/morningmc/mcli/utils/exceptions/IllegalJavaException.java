package minecraft.morningmc.mcli.utils.exceptions;

import java.io.File;

/**
 * {@code IllegalJavaException} is an exception class indicating that an illegal or invalid Java path
 * has been encountered during the launcher's operation.
 */
public class IllegalJavaException extends LauncherException {
	private File javaPath;
	
	/**
	 * Constructs a new {@code IllegalJavaException} with no detail message.
	 */
	public IllegalJavaException() {
		super();
	}
	
	/**
	 * Constructs a new {@code IllegalJavaException} with the specified detail message.
	 *
	 * @param message The detail message.
	 */
	public IllegalJavaException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new {@code IllegalJavaException} with the specified cause.
	 *
	 * @param cause The cause of the exception.
	 */
	public IllegalJavaException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs a new {@code IllegalJavaException} with the specified detail message and cause.
	 *
	 * @param message The detail message.
	 * @param cause   The cause of the exception.
	 */
	public IllegalJavaException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructs a new {@code IllegalJavaException} with the specified illegal Java path.
	 *
	 * @param javaPath The illegal Java path.
	 */
	public IllegalJavaException(File javaPath) {
		super("Illegal Java Path: " + javaPath.getAbsolutePath());
		
		this.javaPath = javaPath;
	}
	
	/**
	 * Constructs a new {@code IllegalJavaException} with the specified illegal Java path and cause.
	 *
	 * @param javaPath The illegal Java path.
	 * @param cause    The cause of the exception.
	 */
	public IllegalJavaException(File javaPath, Throwable cause) {
		super("Illegal Java Path: " + javaPath.getAbsolutePath(), cause);
		
		this.javaPath = javaPath;
	}
	
	/**
	 * Gets the illegal Java path associated with this exception.
	 *
	 * @return The illegal Java path.
	 */
	public File getJavaPath() {
		return javaPath;
	}
}