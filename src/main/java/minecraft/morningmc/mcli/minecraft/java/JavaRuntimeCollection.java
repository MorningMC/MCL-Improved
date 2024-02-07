package minecraft.morningmc.mcli.minecraft.java;

import minecraft.morningmc.mcli.utils.Platform;
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

/**
 * A collection of Java runtimes managed by the launcher.
 */
public class JavaRuntimeCollection implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger();
	
	/** NbtLoader for loading and saving JavaRuntimeCollection objects from/to NBT data. */
	public static final NbtLoader<JavaRuntimeCollection, ListTag<StringTag>> LOADER = new NbtLoader<>() {
		
		/**
		 * Load JavaRuntimeCollection from an NBT list tag.
		 *
		 * @param tag The NBT list tag containing Java runtime paths.
		 * @return The loaded JavaRuntimeCollection.
		 * @throws IllegalNbtException If there is an issue with the NBT data.
		 */
		@Override
		public JavaRuntimeCollection loadFromNbt(ListTag<StringTag> tag) throws IllegalNbtException {
			Set<JavaRuntime> runtimes = new TreeSet<>(Comparator.reverseOrder());
			
			for (StringTag subTag : tag) {
				try {
					runtimes.add(JavaRuntime.fromPath(new File(subTag.getValue())));
				} catch (IllegalJavaException e) {
					LOGGER.warn("Failed to load Java runtime from NBT: " + e.getMessage());
				}
			}
			
			init(runtimes);
			return instance;
		}
		
		/**
		 * Save JavaRuntimeCollection to an NBT list tag.
		 *
		 * @param object The JavaRuntimeCollection to be saved.
		 * @return The NBT list tag containing Java runtime paths.
		 */
		@Override
		public ListTag<StringTag> saveToNbt(JavaRuntimeCollection object) {
			ListTag<StringTag> tag = new ListTag<>();
			
			for (JavaRuntime runtime : object.runtimes) {
				tag.add(new StringTag(runtime.executable().getAbsolutePath()));
			}
			
			return tag;
		}
	};
	
	public static JavaRuntimeCollection instance = null;
	
	private static final Comparator<JavaRuntime> COMPARATOR = Comparator.reverseOrder();
	private final Set<JavaRuntime> runtimes = new TreeSet<>(COMPARATOR.thenComparingInt(JavaRuntime::hashCode));
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
	 * @return True if the search is ongoing, false otherwise.
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
			// Refresh old runtimes
			for (JavaRuntime runtime : runtimes) {
				try {
					runtime.refresh();
				} catch (IllegalJavaException e) {
					LOGGER.warn("Expired Java runtime: " + runtime);
					runtimes.remove(runtime);
				}
			}
			
			// search potential runtimes
			LOGGER.info("Start searching for potential Java runtimes...");
			
			try {
				long startTime = System.currentTimeMillis();
				Set<JavaRuntime> potentialRuntimes = new TreeSet<>(COMPARATOR.thenComparingInt(JavaRuntime::hashCode));
				
				// Add order:
				// 1. System-defined locations
				// 2. Minecraft-installed locations
				// 3. PATH
				
				// System-defined locations
				switch (Platform.CURRENT) {
					case WINDOWS -> {
						potentialRuntimes.addAll(queryJavaHomesInRegistryKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Runtime Environment\\"));
						potentialRuntimes.addAll(queryJavaHomesInRegistryKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\Java Development Kit\\"));
						potentialRuntimes.addAll(queryJavaHomesInRegistryKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\JRE\\"));
						potentialRuntimes.addAll(queryJavaHomesInRegistryKey("HKEY_LOCAL_MACHINE\\SOFTWARE\\JavaSoft\\JDK\\"));
						
						// Program Files
						Set<File> programFiles = new HashSet<>();
						try {
							for (String env : new String[]{ "ProgramFiles", "ProgramFiles(x86)", "ProgramFiles(ARM)" }) {
								String value = System.getenv(env);
								
								if (value != null) {
									programFiles.add(new File(value));
								}
							}
						} catch (Exception e) {
							for (String root : new String[]{ "C:\\Program Files", "C:\\Program Files (x86)", "C:\\Program Files (ARM)" }) {
								File file = new File(root);
								
								if (file.exists()) {
									programFiles.add(file);
								}
							}
						}
						
						for (File programFile : programFiles) {
							for (String vendor : new String[]{ "Java", "BellSoft", "AdoptOpenJDK", "Zulu", "Microsoft", "Eclipse Foundation", "Semeru" }) {
								File root = new File(programFile, vendor);
								
								try {
									for (File home : Objects.requireNonNull(root.listFiles())) {
										if (home.isDirectory()) {
											LOGGER.trace("Query home: " + home.getAbsolutePath());
											
											try {
												potentialRuntimes.add(JavaRuntime.fromHome(home));
											} catch (IllegalJavaException ignored) {}
										}
									}
								} catch (Exception ignored) {}
							}
						}
					}
					
					case LINUX -> {
						for (String path : new String[]{ "/usr/java", "/usr/lib/jvm", "/usr/lib32/jvm" }) {
							File root = new File(path);
							
							try {
								for (File home : Objects.requireNonNull(root.listFiles())) {
									if (home.isDirectory()) {
										LOGGER.trace("Query home: " + home.getAbsolutePath());
										
										try {
											potentialRuntimes.add(JavaRuntime.fromHome(home));
										} catch (IllegalJavaException ignored) {}
									}
								}
							} catch (Exception ignored) {}
						}
					}
					
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
				
				
				// PATH
				try {
					for (String path : System.getenv("PATH").split(Platform.getPathSeparator())) {
						try {
							File executable = new File(path, JavaRuntime.JAVA);
							LOGGER.trace("Query executable in PATH: " + executable);
							potentialRuntimes.add(JavaRuntime.fromPath(executable));
						} catch (IllegalJavaException ignored) {}
					}
				} catch (Exception ignored) {}
				
				if (JavaRuntime.CURRENT != null) {
					potentialRuntimes.add(JavaRuntime.CURRENT);
				}
				
				long stopTime = System.currentTimeMillis();
				
				LOGGER.info("Finish searching potential Java runtimes. Found " + potentialRuntimes.size());
				LOGGER.debug("Used " + (stopTime - startTime) + " ms");
				
				runtimes.addAll(potentialRuntimes);
				
			} catch (Exception e) {
				LOGGER.error("Failed to search potential Java runtimes: ", e);
			}
			
			LOGGER.trace("Found " + get().size() + " Java runtimes in total:");
			for (JavaRuntime runtime : runtimes) {
				LOGGER.trace(runtime.toString());
			}
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
			if (!querySubFolders(java).contains(java + "\\MSI")) {
				continue;
			}
			String home = queryRegisterValue(java, "JavaHome");
			if (home != null) {
				try {
					homes.add(JavaRuntime.fromHome(new File(home)));
					
				} catch (InvalidPathException | IllegalJavaException e) {
					LOGGER.warn("Invalid Java path in system registry: " + home);
				}
			}
		}
		return homes;
	}
	
	/**
	 * Query subfolders under a Windows Registry location.
	 *
	 * @param location The Windows Registry key location.
	 * @return The set of subfolder names.
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