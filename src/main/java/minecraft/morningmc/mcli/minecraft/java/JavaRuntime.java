package minecraft.morningmc.mcli.minecraft.java;

import minecraft.morningmc.mcli.utils.Platform;
import minecraft.morningmc.mcli.utils.exceptions.IllegalJavaException;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.primitive.StringTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a Java runtime, providing methods for retrieving Java version and executable information.
 */
public record JavaRuntime(File executable, int version, Platform platform) implements Comparable<JavaRuntime> {
	private static final Logger LOGGER = LogManager.getLogger();
	
	/** NbtLoader for loading and saving {@code JavaRuntime} objects from/to NBT data. */
	public static final NbtLoader<JavaRuntime, StringTag> LOADER = new NbtLoader<>() {
		
		@Override
		public JavaRuntime loadFromNbt(StringTag tag) throws IllegalNbtException {
			try {
				return JavaRuntime.fromPath(new File(tag.getValue()));
			} catch (IllegalJavaException e) {
				LOGGER.warn("Failed to load Java runtime from NBT: " + e.getMessage());
				return null;
			}
		}

		@Override
		public StringTag saveToNbt(JavaRuntime object) {
			return new StringTag(object.executable.getAbsolutePath());
		}
	};
	
	/** The default executable name for Java. */
	public static final String JAVA = Platform.SYSTEM.operatingSystem() == Platform.OperatingSystem.WINDOWS ? "java.exe" : "java";
	
	/** The current Java runtime based on the system properties. */
	public static final JavaRuntime CURRENT = resolveCurrent();
	
	/**
	 * Creates a {@code JavaRuntime} instance from the given executable path.
	 *
	 * @param path The path to the Java executable.
	 * @return The {@code JavaRuntime} instance.
	 * @throws IllegalJavaException If an error occurs during Java version retrieval or the executable is illegal.
	 */
	public static JavaRuntime fromPath(File path) throws IllegalJavaException {
		ProcessBuilder builder = new ProcessBuilder(path.getAbsolutePath(), "-XshowSettings:properties", "-version");
		builder.redirectErrorStream(true);
		
		String content;
		
		try {
			Process process = builder.start();
			BufferedReader reader = process.inputReader();
			StringBuilder contentBuilder = new StringBuilder();
			
			for (String line; (line = reader.readLine()) != null; ) {
				contentBuilder.append(line).append("\n");
			}
			content = contentBuilder.toString();
			
		} catch (Exception e) {
			throw new IllegalJavaException(path, e);
		}
		
		if (!content.contains("java") && !content.contains("sun")) {
			throw new IllegalJavaException(path);
		}
		
		return new JavaRuntime(
				path, parseVersion(getProperty(content, "java.version")),
				new Platform(
						Platform.OperatingSystem.infer(getProperty(content, "os.name")),
						Platform.Architecture.infer(getProperty(content, "sun.arch.data.model"), getProperty(content, "os.arch")),
						getProperty(content, "file.separator"),
						getProperty(content, "path.separator"),
						getProperty(content, "line.separator"),
						Platform.inferEncoding(getProperty(content, "sun.jnu.encoding"))
				)
		);
	}
	
	/**
	 * Creates a {@code JavaRuntime} instance from the Java home directory.
	 *
	 * @param home The Java home directory.
	 * @return The {@code JavaRuntime} instance.
	 * @throws IllegalJavaException If an error occurs during Java version retrieval.
	 */
	public static JavaRuntime fromHome(File home) throws IllegalJavaException {
		return fromPath(new File(home, "bin/" + JAVA));
	}
	
	/**
	 * Refreshes the {@code JavaRuntime} instance to check for changes.
	 *
	 * @throws IllegalJavaException If an error occurs during Java version retrieval or the executable is illegal.
	 */
	public void refresh() throws IllegalJavaException {
		JavaRuntime newRuntime = fromPath(executable);
		
		if (this.compareTo(newRuntime) != 0) {
			throw new IllegalJavaException(executable);
		}
	}
	
	/**
	 * Resolves the current Java runtime based on the system properties.
	 *
	 * @return The current Java runtime, or {@code null} if an error occurs.
	 */
	private static JavaRuntime resolveCurrent() {
		try {
			return fromHome(new File(System.getProperty("java.home")));
			
		} catch (IllegalJavaException e) {
			LOGGER.warn("Failed to get current Java runtime: ", e);
			return null;
		}
	}
	
	/**
	 * Parses the Java version string into an integer.
	 *
	 * @param version The version string to be parsed.
	 * @return The parsed Java version, or -1 if an error occurs.
	 */
	private static int parseVersion(String version) {
		if (version != null) {
			Matcher matcher = Pattern.compile("^(?<version>[0-9]+)").matcher(version);
			
			if (matcher.find()) {
				int head;
				try {
					head = Integer.parseInt(matcher.group());
				} catch (NumberFormatException e) {
					head = -1;
				}
				
				if (head > 1) {
					return head;
				}
			}
			
			// using 1.x format
			if (version.contains("1.8")) {
				return 8;
			} else if (version.contains("1.7")) {
				return 7;
			} else if (version.contains("1.6")) {
				return 6;
			} else {
				LOGGER.warn("Failed to parse Java version: " + version);
				return -1;
			}
		}
		
		return -1;
	}
	
	/**
	 * Retrieves a system property from the given content.
	 *
	 * @param content The given content.
	 * @param key The key of the system property to be retrieved.
	 * @return The value of the system property, or {@code null} if not found.
	 */
	private static String getProperty(String content, String key) {
		key = key.replace(".", "\\.");
		Matcher matcher = Pattern.compile(key + " = (?<value>.*)").matcher(content);

		if (matcher.find()) {
			return matcher.group(1);
		}

		return null;
	}
	
	// Overrides
	@Override
	public String toString() {
		return "Java " + (version >= 0 ? version : "?") + " (" + executable.getAbsolutePath() + ", " + platform + ")";
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		JavaRuntime that = (JavaRuntime) o;
		return Objects.equals(executable, that.executable);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(executable);
	}
	
	@Override
	public int compareTo(JavaRuntime o) {
		return version - o.version;
	}
}