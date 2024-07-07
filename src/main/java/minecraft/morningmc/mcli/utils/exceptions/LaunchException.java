package minecraft.morningmc.mcli.utils.exceptions;

/**
 * An exception class specifically for errors related to the launcher's launch process.
 */
public class LaunchException extends LauncherException {
    
    /**
     * Constructs a new {@link LaunchException} with no detail message.
     */
    public LaunchException() {
        super();
    }
    
    /**
     * Constructs a new {@link LaunchException} with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval by the getMessage() method).
     */
    public LaunchException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new {@link LaunchException} with the specified cause.
     *
     * @param cause The cause of the exception (which is saved for later retrieval by the getCause() method).
     */
    public LaunchException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Constructs a new {@link LaunchException} with the specified detail message and cause.
     *
     * @param message The detail message (which is saved for later retrieval by the getMessage() method).
     * @param cause   The cause of the exception (which is saved for later retrieval by the getCause() method).
     */
    public LaunchException(String message, Throwable cause) {
        super(message, cause);
    }
}