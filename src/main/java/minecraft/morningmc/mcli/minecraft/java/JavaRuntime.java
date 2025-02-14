package minecraft.morningmc.mcli.minecraft.java;

import minecraft.morningmc.mcli.utils.Platform;
import minecraft.morningmc.mcli.utils.exceptions.IllegalJavaException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record JavaRuntime(File executable, int version) {
	private static final Logger LOGGER = LoggerFactory.getLogger(JavaRuntime.class);

	public static final String JAVA = Platform.CURRENT == Platform.WINDOWS ? "java.exe" : "java";
	public static final JavaRuntime CURRENT = getCurrent();

	public static JavaRuntime fromPath(File path) throws IllegalJavaException {
		ProcessBuilder builder = new ProcessBuilder(path.getAbsolutePath(), "-XshowSettings:properties", "-version");
		builder.redirectErrorStream(true);

		String version = null;
		try {
			Process process = builder.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

			StringBuilder content = new StringBuilder();
			for (String line; (line = reader.readLine()) != null; ) {
				content.append(line).append("\n");
			}
			String c = content.toString();

			if (!c.contains("java") && !c.contains("sun")) {
				throw new IllegalJavaException(path);
			}

			Matcher matcher = Pattern.compile("java.version=([0-9.]+)").matcher(c);
			if (matcher.find()) {
				version = matcher.group(1);
			}

		} catch (IOException e) {
			throw new IllegalJavaException(path, e);
		}

		return new JavaRuntime(path, parseVersion(version));
	}

	public static JavaRuntime fromHome(File home) throws IllegalJavaException {
		return fromPath(new File(home, "bin/" + JAVA));
	}

	private static JavaRuntime getCurrent() {
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
				head = Integer.parseInt(version);
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
			return -1;
		}
	}

	// Installed Java Runtimes
	private static Set<JavaRuntime> installedJavaRuntimes = null;
	private static volatile boolean searchingInstalledJavaRuntimes = false;

	public static Set<JavaRuntime> getInstalledJavaRuntimes() {
		if (installedJavaRuntimes == null) {
			searchInstalledJavaRuntimes();
		}

		while (searchingInstalledJavaRuntimes) {
			// wait until the search is done
			Thread.onSpinWait();
		}

		return installedJavaRuntimes;
	}

	public static synchronized void searchInstalledJavaRuntimes() {
		searchingInstalledJavaRuntimes = true; // set flag

		Set<JavaRuntime> runtimes = new HashSet<>();
		long startTime = System.currentTimeMillis();

		// Add order:
		// 1. System-defined locations
		// 2. Minecraft-installed locations
		// 3. PATH

		// System-defined locations
		switch (Platform.CURRENT) {
			case WINDOWS -> {

			}
			case LINUX -> {

			}
			case MACOS -> {

			}
		}

		// Minecraft-installed locations

		// PATH

        runtimes.add(CURRENT);

		long stopTime = System.currentTimeMillis();

		LOGGER.info("Finish searching Java runtimes. Found " + runtimes.size());
		LOGGER.debug("Used " + (stopTime - startTime) + " ms");

		installedJavaRuntimes = runtimes;
		searchingInstalledJavaRuntimes = false; // clear flag
	}

	// Windows Registry Support
	private static Set<File> queryJavaHomesInRegistryKey(String location) throws IOException {
		Set<File> homes = new HashSet<>();
		for (String java : querySubFolders(location)) {
			if (!querySubFolders(java).contains(java + "\\MSI")) {
				continue;
			}
			String home = queryRegisterValue(java, "JavaHome");
			if (home != null) {
				try {
					homes.add(new File(home));

				} catch (InvalidPathException e) {
					LOGGER.warn("Invalid Java path in system registry: " + home);
				}
			}
		}
		return homes;
	}

	private static Set<String> querySubFolders(String location) throws IOException {
		Set<String> res = new HashSet<>();

		Process process = Runtime.getRuntime().exec(new String[] { "cmd", "/c", "reg", "query", location });
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			for (String line; (line = reader.readLine()) != null;) {
				if (line.startsWith(location) && !line.equals(location)) {
					res.add(line);
				}
			}
		}
		return res;
	}

	private static String queryRegisterValue(String location, String name) throws IOException {
		boolean last = false;
		Process process = Runtime.getRuntime().exec(new String[] { "cmd", "/c", "reg", "query", location, "/v", name });

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
			for (String line; (line = reader.readLine()) != null;) {
				if (!line.trim().isEmpty()) {
					if (last && line.trim().startsWith(name)) {
						int begins = line.indexOf(name);

						if (begins > 0) {
							String s2 = line.substring(begins + name.length());
							begins = s2.indexOf("REG_SZ");

							if (begins > 0) {
								return s2.substring(begins + "REG_SZ".length()).trim();
							}
						}
					}
					if (location.equals(line.trim())) {
						last = true;
					}
				}
			}
		}
		return null;
	}

	// Overrides
	@Override
	public String toString() {
		return "Java " + version + " (" + executable.getAbsolutePath() + ")";
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		JavaRuntime that = (JavaRuntime) o;
		return Objects.equals(executable, that.executable);
	}

	@Override
	public int hashCode() {
		return Objects.hash(executable);
	}
}
