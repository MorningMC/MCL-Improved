package minecraft.morningmc.mcli.utils.exceptions;

/**
 * {@code LaunchException} is an exception class specifically for errors related to the launcher's launch process.
 * It extends the {@code LauncherException} class.
 */
public class LaunchException extends LauncherException {
    
    /**
     * Constructs a new {@code LaunchException} with no detail message.
     */
    public LaunchException() {
    }
    
    /**
     * Constructs a new {@code LaunchException} with the specified detail message and cause.
     *
     * @param message The detail message.
     * @param cause The cause of the exception.
     */
    public LaunchException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructs a new {@code LaunchException} with the specified detail message.
     *
     * @param message The detail message.
     */
    public LaunchException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new {@code LaunchException} with the specified cause.
     *
     * @param cause The cause of the exception.
     */
    public LaunchException(Throwable cause) {
        super(cause);
    }
}