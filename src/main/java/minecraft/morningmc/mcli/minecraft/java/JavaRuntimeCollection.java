package minecraft.morningmc.mcli.minecraft.java;

import minecraft.morningmc.mcli.utils.Platform;
import minecraft.morningmc.mcli.utils.annotations.ObjectCollection;
import minecraft.morningmc.mcli.utils.exceptions.IllegalJavaException;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.primitive.StringTag;
import dev.dewy.nbt.tags.collection.ListTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.InvalidPathException;
import java.util.*;
import java.util.stream.*;

/**
 * A collection of Java runtimes managed by the launcher.
 */
@ObjectCollection
public class JavaRuntimeCollection implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger();
	
	/** NbtLoader for loading and saving {@code JavaRuntimeCollection} objects from/to NBT data. */
	public static final NbtLoader<JavaRuntimeCollection, ListTag<StringTag>> LOADER = new NbtLoader<>() {
		
		/**
		 * Load {@code JavaRuntimeCollection} from an NBT list tag.
		 *
		 * @param tag The NBT list tag containing Java runtime paths.
		 * @return The loaded {@code JavaRuntimeCollection}.
		 * @throws IllegalNbtException If there is an issue with the NBT data.
		 */
		@Override
		public JavaRuntimeCollection loadFromNbt(ListTag<StringTag> tag) throws IllegalNbtException {
			init(tag.getValue().stream()
					     .map(StringTag::getValue)
					     .map(File::new)
					     .flatMap(path -> {
						     try {
							     return Stream.of(JavaRuntime.fromPath(path));
						     } catch (IllegalJavaException e) {
							     LOGGER.warn("Failed to load Java runtime from NBT: " + e.getMessage());
							     return Stream.empty();
						     }
					     })
					     .collect(Collectors.toSet()));
			return instance;
		}
		
		/**
		 * Save {@code JavaRuntimeCollection} to an NBT list tag.
		 *
		 * @param object The {@code JavaRuntimeCollection} to be saved.
		 * @return The NBT list tag containing Java runtime paths.
		 */
		@Override
		public ListTag<StringTag> saveToNbt(JavaRuntimeCollection object) {
			ListTag<StringTag> tag = new ListTag<>();
			
			object.runtimes.stream()
					.map(JavaRuntime::executable)
					.map(File::getAbsolutePath)
					.map(StringTag::new)
					.forEach(tag::add);
			
			return tag;
		}
	};
	@SuppressWarnings("unchecked")
	private static final Comparator<JavaRuntime> COMPARATOR = ((Comparator<JavaRuntime>) Comparator.naturalOrder())
			                                                              .thenComparingInt(runtime -> runtime.platform().architecture().bits()).reversed()
			                                                              .thenComparingInt(JavaRuntime::hashCode);
	
	public static JavaRuntimeCollection instance = null;
	
	private final Set<JavaRuntime> runtimes = new TreeSet<>(COMPARATOR);
	private Thread thread = null;
	
	/**
	 * Initialize the JavaRuntimeCollection with a collection of Java runtimes.
	 *
	 * @param runtimes The collection of Java runtimes.
	 */
	public static void init(Collection<JavaRuntime> runtimes) {
		if (instance != null) {
			throw new IllegalStateException("JavaRuntimeCollection already initialized");
		}
		
		instance = new JavaRuntimeCollection();
		instance.runtimes.addAll(runtimes);
	}
	
	/**
	 * Get the set of Java runtimes.
	 *
	 * @return The set of Java runtimes.
	 */
	public static Set<JavaRuntime> get() {
		return instance.runtimes;
	}
	
	/**
	 * Add a Java runtime to the collection.
	 *
	 * @param runtime The Java runtime to be added.
	 */
	public static void add(JavaRuntime runtime) {
		instance.runtimes.add(runtime);
	}
	
	/**
	 * Start searching for potential Java runtimes in a separate thread.
	 */
	public static void search() {
		if (!isSearching()) {
			instance.thread = new Thread(instance, "searchJava");
			instance.thread.start();
		}
	}
	
	/**
	 * Check if the search for potential Java runtimes is in progress.
	 *
	 * @return {@code true} if the search is ongoing, {@code false} otherwise.
	 */
	public static boolean isSearching() {
		if (instance.thread == null) {
			return false;
		}
		
		return instance.thread.isAlive();
	}
	
	/**
	 * Runnable implementation for searching and updating Java runtimes.
	 */
	@Override
	public void run() {
		synchronized (runtimes) {
			// refresh old runtimes
			runtimes.removeIf(runtime -> {
				try {
					runtime.refresh();
				} catch (IllegalJavaException e) {
					LOGGER.warn("Expired Java runtime: " + runtime, e);
					return true;
				}
				
				return false;
			});
			
			// search potential runtimes
			LOGGER.info("Start searching for potential Java runtimes...");
			
			try {
				long startTime = System.currentTimeMillis();
				Set<JavaRuntime> potentialRuntimes = new TreeSet<>(COMPARATOR);
				
				// Add order:
				// 1. System-defined locations
				// 2. Minecraft-installed locations
				// 3. PATH
				
				// System-defined locations
				switch (Platform.CURRENT.operatingSystem()) {
					case WINDOWS -> {
						potentialRuntimes.addAll(queryJavaHomesInRegistryKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Runtime Environment\\"));
						potentialRuntimes.addAll(queryJavaHomesInRegistryKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Development Kit\\"));
						potentialRuntimes.addAll(queryJavaHomesInRegistryKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\JRE\\"));
						potentialRuntimes.addAll(queryJavaHomesInRegistryKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\JDK\\"));
						
						// program files
						Stream.of("ProgramFiles", "ProgramFiles(x86)", "ProgramFiles(ARM)")
								.map(System::getenv)
								.filter(Objects::nonNull)
								.map(File::new)
								.flatMap(programFile -> Stream.of("Java", "BellSoft", "AdoptOpenJDK", "Zulu", "Microsoft", "Eclipse Foundation", "Semeru")
										                         .map(vendor -> new File(programFile, vendor)))
								.flatMap(JavaRuntimeCollection::listDirectories)
								.flatMap(JavaRuntimeCollection::parseHome)
								.forEach(potentialRuntimes::add);
					}
					
					case LINUX -> Stream.of("/usr/java", "/usr/lib/jvm", "/usr/lib32/jvm")
										.map(File::new)
										.flatMap(JavaRuntimeCollection::listDirectories)
										.flatMap(JavaRuntimeCollection::parseHome)
										.forEach(potentialRuntimes::add);
					
					case MACOS -> {
						try {
							for (File file : Objects.requireNonNull(new File("/Library/Java/JavaVirtualMachines").listFiles())) {
								if (file.isDirectory()) {
									File home = new File(file, "Contents/Home");
									if (home.exists()) {
										LOGGER.trace("Query home: " + home.getAbsolutePath());
										potentialRuntimes.add(JavaRuntime.fromHome(home));
									}
									
									home = new File(home, "jre");
									if (home.exists()) {
										LOGGER.trace("Query home: " + home.getAbsolutePath());
										potentialRuntimes.add(JavaRuntime.fromHome(home));
									}
								}
							}
						} catch (Exception ignored) {}
						
						try {
							for (File file : Objects.requireNonNull(new File("/System/Library/Java/JavaVirtualMachines").listFiles())) {
								if (file.isDirectory()) {
									File home = new File(file, "Contents/Home");
									if (home.exists()) {
										LOGGER.trace("Query home: " + home.getAbsolutePath());
										potentialRuntimes.add(JavaRuntime.fromHome(home));
									}
								}
							}
						} catch (Exception ignored) {}
						
						try {
							potentialRuntimes.add(JavaRuntime.fromPath(new File("/Library/Internet Plug-Ins/JavaAppletPlugin.plugin/Contents/Home/bin/java")));
						} catch (IllegalJavaException ignored) {}
						
						try {
							potentialRuntimes.add(JavaRuntime.fromPath(new File("/Applications/Xcode.app/Contents/Applications/Application Loader.app/Contents/MacOS/itms/java/bin/java")));
						} catch (IllegalJavaException ignored) {}
					}
				}
				
				// Minecraft-installed locations
				Set<File> minecraftLocations = new HashSet<>();
				switch (Platform.CURRENT.operatingSystem()) {
					case WINDOWS -> {
						File file = new File(System.getenv("LocalAppData"), "Packages\\Microsoft.4297127D64EC6_8wekyb3d8bbwe\\LocalCache\\Local\\runtime");
						if (file.exists()) {
							minecraftLocations.add(file);
						}
						
						File programFile;
						try {
							programFile = new File(System.getenv("ProgramFiles(x86)"));
						} catch (Exception e) {
							programFile = new File("C:\\Program Files (x86)");
						}
						if (programFile.exists()) {
							minecraftLocations.add(new File(programFile, "Minecraft Launcher\\runtime"));
						}
					}
					
					case LINUX -> {
						File file = new File(System.getProperty("user.home", ".minecraft/runtime"));
						if (file.exists()) {
							minecraftLocations.add(file);
						}
					}
					
					case MACOS -> {
						String userHome = System.getProperty("user.home");
						if (userHome != null) {
							File file = new File(userHome, "Library/Application Support/minecraft/runtime");
							if (file.exists()) {
								minecraftLocations.add(file);
							}
						}
					}
				}
				
				for (File location : minecraftLocations) {
					try {
						for (File dir : Objects.requireNonNull(location.listFiles())) {
							if (dir.isDirectory()) {
								String component = dir.getName();
								
								try {
									for (File file : Objects.requireNonNull(dir.listFiles())) {
										File home = new File(file, component);
										
										LOGGER.trace("Query home: " + home.getAbsolutePath());
										potentialRuntimes.add(JavaRuntime.fromHome(home));
									}
								} catch (Exception ignored) {}
							}
						}
					} catch (Exception ignored) {}
				}
				
				// PATH
				try {
					Arrays.stream(System.getenv("PATH").split(Platform.CURRENT.pathSeparator()))
							.map(File::new)
							.filter(bin -> bin.getName().equals("bin"))
							.map(bin -> new File(bin, JavaRuntime.JAVA))
							.flatMap(executable -> {
								LOGGER.trace("Query executable in PATH: " + executable);
								
								try {
									return Stream.of(JavaRuntime.fromPath(executable));
								} catch (IllegalJavaException e) {
									return Stream.empty();
								}
							})
							.forEach(potentialRuntimes::add);
					
				} catch (Exception e) {
					LOGGER.warn("Failed to parse PATH: " + e.getMessage());
				}
				
				if (JavaRuntime.CURRENT != null) {
					potentialRuntimes.add(JavaRuntime.CURRENT);
				}
				
				long stopTime = System.currentTimeMillis();
				
				LOGGER.debug("Finish searching potential Java runtimes. Found " + potentialRuntimes.size());
				LOGGER.debug("Used " + (stopTime - startTime) + " ms");
				
				runtimes.addAll(potentialRuntimes);
				
			} catch (Exception e) {
				LOGGER.error("Failed to search potential Java runtimes: ", e);
			}
			
			// list found runtimes
			LOGGER.debug("Found " + get().size() + " Java runtimes in total:");
			for (JavaRuntime runtime : runtimes) {
				LOGGER.debug(runtime.toString());
			}
		}
	}
	
	/**
	 * List subdirectories of the given directory.
	 *
	 * @param directory The directory to be rooted.
	 * @return A stream that contains all subdirectories of the given directory,
	 *         or empty if an error occurs.
	 */
	private static Stream<File> listDirectories(File directory) {
		File[] files = directory.listFiles();
		if (files != null) {
			return Arrays.stream(files).filter(File::isDirectory);
		}
		
		return Stream.empty();
	}
	
	private static Stream<JavaRuntime> parseHome(File home) {
		LOGGER.trace("Query home: " + home.getAbsolutePath());
		
		try {
			return Stream.of(JavaRuntime.fromHome(home));
		} catch (IllegalJavaException e) {
			return Stream.empty();
		}
	}
	
	// Windows Registry Support
	/**
	 * Query Java home locations in the Windows Registry key.
	 *
	 * @param location The Windows Registry key location.
	 * @return The set of Java runtimes found in the registry key.
	 * @throws IOException If there is an issue with querying the registry.
	 */
	private static Set<JavaRuntime> queryJavaHomesInRegistryKey(String location) throws IOException {
		Set<JavaRuntime> homes = new HashSet<>();
		for (String java : querySubFolders(location)) {
			if (querySubFolders(java).contains(java + "\\MSI")) {
				String home = queryRegisterValue(java, "JavaHome");
				if (home != null) {
					try {
						homes.add(JavaRuntime.fromHome(new File(home)));
					} catch (InvalidPathException | IllegalJavaException e) {
						LOGGER.warn("Invalid Java path in system registry: " + home);
					}
				}
			}
		}
		
		return homes;
	}
	
	/**
	 * Query sub-folders under a Windows Registry location.
	 *
	 * @param location The Windows Registry key location.
	 * @return The set of sub-folder names.
	 * @throws IOException If there is an issue with querying the registry.
	 */
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
	
	/**
	 * Query a value in the Windows Registry.
	 *
	 * @param location The Windows Registry key location.
	 * @param name The name of the registry value to query.
	 * @return The value associated with the specified registry value name.
	 * @throws IOException If there is an issue with querying the registry.
	 */
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
}