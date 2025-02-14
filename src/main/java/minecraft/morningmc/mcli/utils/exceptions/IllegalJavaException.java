package minecraft.morningmc.mcli.utils.exceptions;

import java.io.File;

public class IllegalJavaException extends LauncherException {
	private File javaPath;

	public IllegalJavaException() {
		super();
	}

	public IllegalJavaException(String message) {
		super(message);
	}

	public IllegalJavaException(Throwable cause) {
		super(cause);
	}

	public IllegalJavaException(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalJavaException(File javaPath) {
		super("Illegal Java Path: " + javaPath.getAbsolutePath());

		this.javaPath = javaPath;
	}

	public IllegalJavaException(File javaPath, Throwable cause) {
		super("Illegal Java Path: " + javaPath.getAbsolutePath(), cause);

		this.javaPath = javaPath;
	}

	public File getJavaPath() {
		return javaPath;
	}
}
