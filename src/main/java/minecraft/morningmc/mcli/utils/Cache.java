package minecraft.morningmc.mcli.utils;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Cache implements AbstractFile {
	private static final Logger LOGGER = LoggerFactory.getLogger(Cache.class);

	private final File cache;

	public Cache(File cache) throws IOException {
		this.cache = cache;

		if (!this.cache.exists()) {
			this.cache.createNewFile();
		}

		this.cache.deleteOnExit();

		LOGGER.debug("Created cache at " + this.cache.getAbsolutePath());
	}

	public Cache(String cache) throws IOException {
		this(new File(FileMetadata.CACHE_ROOT, cache));
	}

	@Override
	public File toFile() {
		return cache;
	}
}
