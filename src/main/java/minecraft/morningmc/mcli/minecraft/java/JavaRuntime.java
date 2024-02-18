package minecraft.morningmc.mcli.minecraft.java;

import minecraft.morningmc.mcli.utils.Platform;
import minecraft.morningmc.mcli.utils.exceptions.IllegalJavaException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a Java Runtime, providing methods for retrieving Java version and executable information.
 */
public record JavaRuntime(File executable, int version) implements Comparable<JavaRuntime> {
	private static final Logger LOGGER = LogManager.getLogger();
	
	/** The default executable name for Java on Windows and non-Windows platforms. */
	public static final String JAVA = Platform.CURRENT == Platform.WINDOWS ? "java.exe" : "java";
	
	/** The current Java runtime based on the system properties. */
	public static final JavaRuntime CURRENT = resolveCurrent();
	
	/**
	 * Creates a JavaRuntime instance from the given executable path.
	 *
	 * @param path The path to the Java executable.
	 * @return The JavaRuntime instance.
	 * @throws IllegalJavaException If an error occurs during Java version retrieval or the executable is illegal.
	 */
	public static JavaRuntime fromPath(File path) throws IllegalJavaException {
		ProcessBuilder builder = new ProcessBuilder(path.getAbsolutePath(), "-XshowSettings:properties", "-version");
		builder.redirectErrorStream(true);
		
		String version = "";
		try {
			Process process = builder.start();
			BufferedReader reader = process.inputReader();
			
			StringBuilder contentBuilder = new StringBuilder();
			for (String line; (line = reader.readLine()) != null; ) {
				contentBuilder.append(line).append("\n");
			}
			String content = contentBuilder.toString();
			
			if (!content.contains("java") && !content.contains("sun")) {
				throw new IllegalJavaException(path);
			}
			
			Matcher matcher = Pattern.compile("java\\.version = (?<version>.*)").matcher(content);
			if (matcher.find()) {
				version = matcher.group(1);
			} else {
				LOGGER.warn("Failed to retrieve Java version from: " + path);
			}
			
		} catch (IOException e) {
			throw new IllegalJavaException(path, e);
		}
		
		return new JavaRuntime(path, parseVersion(version));
	}
	
	/**
	 * Creates a JavaRuntime instance from the Java home directory.
	 *
	 * @param home The Java home directory.
	 * @return The JavaRuntime instance.
	 * @throws IllegalJavaException If an error occurs during Java version retrieval.
	 */
	public static JavaRuntime fromHome(File home) throws IllegalJavaException {
		return fromPath(new File(home, "bin/" + JAVA));
	}
	
	/**
	 * Refreshes the JavaRuntime instance to check for changes.
	 *
	 * @throws IllegalJavaException If an error occurs during Java version retrieval or the executable is illegal.
	 */
	public void refresh() throws IllegalJavaException {
		JavaRuntime newRuntime = fromPath(executable);
		
		if (this.compareTo(newRuntime) != 0) {
			throw new IllegalJavaException(executable);
		}
	}
	
	private static JavaRuntime resolveCurrent() {
		try {
			return fromHome(new File(System.getProperty("java.home")));
			
		} catch (IllegalJavaException e) {
			LOGGER.warn("Failed to get current Java runtime: ", e);
			return null;
		}
	}
	
	private static int parseVersion(String version) {
		// parse the version string into int
		
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
	
	// Overrides
	@Override
	public String toString() {
		return "Java " + (version >= 0 ? version : "<unknown>") + " (" + executable.getAbsolutePath() + ")";
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