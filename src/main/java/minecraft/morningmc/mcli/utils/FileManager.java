package minecraft.morningmc.mcli.utils;

import minecraft.morningmc.mcli.utils.annotations.StaticClass;

import java.io.*;

/**
 * Utility class for managing file and directory metadata in the MCLI launcher.
 */
@StaticClass
public class FileManager {
	/** The root directory for application data. */
	public static final File appdata = resolveAppData();
	
	/** The working root directory for MCLI. */
	public static final File workingRoot = new File(appdata, ".mcli");

	/** The configuration file for MCLI. */
	public static File config = new File(workingRoot, "config.nbt");
	
	/** The backup configuration file for MCLI. */
	public static File configBackup = new File(workingRoot, "config.backup.nbt");

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
	 * Retrieves an input stream for the specified resource path.
	 *
	 * @param path The path of the resource.
	 * @return An {@link InputStream} for the specified resource.
	 */
	public static InputStream getResource(String path) {
		return ClassLoader.getSystemResourceAsStream(path);
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
	
	/**
	 * Copies a file to a new location.
	 *
	 * @param source      The source file.
	 * @param destination The destination file.
	 * @throws IOException If an I/O error occurs.
	 */
	public static void copyFile(File source, File destination) throws IOException {
		try (InputStream in = new FileInputStream(source); OutputStream out = new FileOutputStream(destination)) {
			in.transferTo(out);
		}
	}
}