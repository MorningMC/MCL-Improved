package minecraft.morningmc.mcli.utils;

import minecraft.morningmc.mcli.utils.exceptions.MissingResourceException;

import java.io.File;
import java.util.Objects;

public class Resource implements AbstractFile {
	private final File resource;

	public Resource(File resource) throws MissingResourceException {
		this.resource = resource;

		if (!resource.exists()) {
			throw new MissingResourceException(resource);
		}
	}

	public Resource(String resource) throws MissingResourceException {
		this(new File(Objects.requireNonNull(Resource.class.getResource(resource)).getFile()));
	}

	@Override
	public File toFile() {
		return resource;
	}
}
