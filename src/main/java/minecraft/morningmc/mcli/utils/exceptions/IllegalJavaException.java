package minecraft.morningmc.mcli.utils.exceptions;

import minecraft.morningmc.mcli.minecraft.java.JavaRuntime;

import java.io.File;

/**
 * An exception class indicating that an illegal or invalid Java path
 * has been encountered during the launcher's operation.
 *
 * @see JavaRuntime
 */
public class IllegalJavaException extends LauncherException {
	public File javaPath;
	
	/**
	 * Constructs a new {@link IllegalJavaException} with no detail message.
	 */
	public IllegalJavaException() {
		super();
	}
	
	/**
	 * Constructs a new {@link IllegalJavaException} with the specified detail message.
	 *
	 * @param message The detail message (which is saved for later retrieval by the getMessage() method).
	 */
	public IllegalJavaException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new {@link IllegalJavaException} with the specified cause.
	 *
	 * @param cause The cause of the exception (which is saved for later retrieval by the getCause() method).
	 */
	public IllegalJavaException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructs a new {@link IllegalJavaException} with the specified detail message and cause.
	 *
	 * @param message The detail message (which is saved for later retrieval by the getMessage() method).
	 * @param cause   The cause of the exception (which is saved for later retrieval by the getCause() method).
	 */
	public IllegalJavaException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Constructs a new {@link IllegalJavaException} with the specified illegal Java path.
	 *
	 * @param javaPath The illegal Java path.
	 */
	public IllegalJavaException(File javaPath) {
		this("Illegal Java Path: " + javaPath.getAbsolutePath());
		
		this.javaPath = javaPath;
	}
	
	/**
	 * Constructs a new {@link IllegalJavaException} with the specified illegal Java path and cause.
	 *
	 * @param javaPath The illegal Java path.
	 * @param cause    The cause of the exception (which is saved for later retrieval by the getCause() method).
	 */
	public IllegalJavaException(File javaPath, Throwable cause) {
		this("Illegal Java Path: " + javaPath.getAbsolutePath(), cause);
		
		this.javaPath = javaPath;
	}
}