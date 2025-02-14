package minecraft.morningmc.mcli.utils.exceptions;

import java.io.File;

public class MissingResourceException extends LauncherException {
	private File resource;

	public MissingResourceException() {
		super();
	}

	public MissingResourceException(String message) {
        super(message);
    }

	public MissingResourceException(Throwable cause) {
		super(cause);
	}

    public MissingResourceException(String message, Throwable cause) {
        super(message, cause);
    }

	public MissingResourceException(File resource) {
		super("Missing Resource: " + resource.getAbsolutePath());

		this.resource = resource;
	}

	public MissingResourceException(File resource, Throwable cause) {
		super("Missing Resource: " + resource.getAbsolutePath(), cause);

		this.resource = resource;
	}

	public File getResource() {
		return resource;
	}
}
