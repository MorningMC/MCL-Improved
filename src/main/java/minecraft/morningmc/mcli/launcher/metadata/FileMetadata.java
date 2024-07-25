package minecraft.morningmc.mcli.launcher.metadata;

import minecraft.morningmc.mcli.minecraft.client.MinecraftDirectory;
import minecraft.morningmc.mcli.utils.Platform;
import minecraft.morningmc.mcli.utils.annotations.StaticClass;

import java.io.*;

/**
 * Utility class for managing file and directory metadata in the MCLI launcher.
 *
 * @see LauncherMetadata
 */
@StaticClass
public class FileMetadata {
	/** The root directory for application data. */
	public static final File appdata = resolveAppData();
	
	/** The working root directory for MCLI. */
	public static final File workingRoot = new File(appdata, ".mcli");
	
	/** The root directory for caching MCLI-related data. */
	public static final File cacheRoot = new File(workingRoot, "cache");

	/** The configuration file for MCLI. */
	public static final File config = new File(workingRoot, "config.nbt");

	/**
	 * Resolves the root directory for application data.
	 *
	 * @return The root directory for application data.
	 */
	private static File resolveAppData() {
		try {
			return switch (Platform.current.operatingSystem()) {
				case WINDOWS -> new File(System.getenv("AppData"));
				case MACOS -> new File(System.getProperty("user.home"), "Library/Application Support");
				case LINUX -> new File(System.getProperty("user.home"), ".config");
				default -> new File(".");
			};
		} catch (Exception e) {
			return new File(".");
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
				workingRoot,
				cacheRoot,
				
				MinecraftDirectory.standard.root,
				MinecraftDirectory.isolateRoot
		};
		File[] files = {
				config
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
	 * @return An {@link InputStream} for the specified resource.
	 */
	public static InputStream getResource(String path) {
		return FileMetadata.class.getClassLoader().getResourceAsStream(path);
	}
	
	/**
	 * Retrieves a buffered reader for the specified input stream.
	 *
	 * @param stream The specified input stream.
	 * @return A {@link BufferedReader} for the specified input stream.
	 */
	public static BufferedReader getReader(InputStream stream) {
		return new BufferedReader(new InputStreamReader(stream));
	}
	
	/**
	 * Renames a file to a new name.
	 *
	 * @param file The file to be renamed.
	 * @param name The new name for the file.
	 * @return {@code true} if and only if the renaming succeeded, {@code false} otherwise.
	 */
	public static boolean renameFile(File file, String name) {
		return file.renameTo(new File(file.getParentFile(), name));
	}
}