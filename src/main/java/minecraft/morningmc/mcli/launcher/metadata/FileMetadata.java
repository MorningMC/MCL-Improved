package minecraft.morningmc.mcli.launcher.metadata;

import minecraft.morningmc.mcli.minecraft.client.directory.TargetMinecraftDirectory;
import minecraft.morningmc.mcli.utils.Platform;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for managing file and directory metadata in the MCLI launcher.
 */
public class FileMetadata {
	/** The root directory for application data. */
	public static final File APPDATA = resolveAppData();
	
	/** The working root directory for MCLI. */
	public static final File WORKING_ROOT = new File(APPDATA, ".mcli");
	
	/** The root directory for the MCLI service. */
	public static final File SERVICE_ROOT = new File(WORKING_ROOT, "service");
	
	/** The root directory for caching MCLI-related data. */
	public static final File CACHE_ROOT = new File(WORKING_ROOT, "cache");

	/** The configuration file for MCLI. */
	public static final File CONFIG = new File(WORKING_ROOT, "config.nbt");

	/**
	 * Resolves the root directory for application data.
	 *
	 * @return The root directory for application data.
	 */
	private static File resolveAppData() {
		String appData = System.getenv("AppData");
		if (appData != null) {
			return new File(appData);
		}

		try {
			switch (Platform.CURRENT) {
				case WINDOWS -> {
					String userProfile = System.getenv("UserProfile");
					if (userProfile == null) {
						userProfile = System.getProperty("user.home");
					}

					return new File(userProfile, "AppData/Roaming");
				}

				case MACOS -> {
					return new File(System.getProperty("user.home"), "Library/Application Support");
				}

				case LINUX -> {
					return new File(System.getProperty("user.home"), ".config");
				}
				
				default -> {
					return File.listRoots()[0];
				}
			}
		} catch (Exception e) {
			try {
				return File.listRoots()[0];
			} catch (Exception ex) {
				return null;
			}
		}
	}
	
	/**
	 * Completes the required files and directories for the MCLI launcher.
	 *
	 * @return The number of files and directories created during the process.
	 * @throws IOException If an I/O error occurs while creating files or directories.
	 */
	public static int completeFiles() throws IOException {
		File[] directories = {
				WORKING_ROOT,
				CACHE_ROOT,
				TargetMinecraftDirectory.STANDARD.getRoot(),
				TargetMinecraftDirectory.ISOLATE_ROOT
		};
		File[] files = {
				CONFIG
		};
		
		int created = 0;
		
		for (File directory : directories) {
			created = directory.mkdirs() ? 1 : 0;
		}
		for (File file : files) {
			created += file.createNewFile() ? 1 : 0;
		}
		
		return created;
	}
	
	/**
	 * Retrieves an input stream for the specified resource path.
	 *
	 * @param path The path of the resource.
	 * @return An InputStream for the specified resource.
	 */
	public static InputStream getResource(String path) {
		return FileMetadata.class.getClassLoader().getResourceAsStream(path);
	}
}