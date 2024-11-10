package minecraft.morningmc.mcli.minecraft.launch;

import minecraft.morningmc.mcli.minecraft.client.version.Version;
import minecraft.morningmc.mcli.utils.annotations.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * A utility class for listening to the output streams (stdout, stderr) of a Minecraft process.
 * It provides separate threads for listening to stdout, stderr, and checking for process exit.
 */
@LauncherProcess("launch")
public class ProcessListener {
	private static final Logger logger = LogManager.getLogger();
	
	public final Process minecraftInstance;
	public final LaunchArguments arguments;
	public final long processID;
	
	public volatile boolean running = true;
	
	public final Thread stdOutListener;
	public final Thread stdErrListener;
	public final Thread exitChecker;
	
	public final List<Version.Logging.Log> logs;
	
	/**
	 * Constructs a {@link ProcessListener} for the given Minecraft process.
	 *
	 * @param minecraftInstance The Minecraft process to monitor.
	 * @param arguments         The launch arguments for the Minecraft process.
	 */
	public ProcessListener(Process minecraftInstance, LaunchArguments arguments) {
		this.minecraftInstance = minecraftInstance;
		this.arguments = arguments;
		processID = minecraftInstance.pid();
		
		stdOutListener = new Thread(() -> readerListener(minecraftInstance.inputReader()), "stdOutListener#" + processID);
		stdErrListener = new Thread(() -> readerListener(minecraftInstance.errorReader()), "stdErrListener#" + processID);
		exitChecker = new Thread(this::exitChecker, "exitChecker#" + processID);
		logs = new ArrayList<>();
		
		stdOutListener.start();
		stdErrListener.start();
		exitChecker.start();
		logger.info("Started listening for Minecraft instance {}", processID);
	}
	
	/**
	 * Stops the process listener and its associated threads.
	 */
	public void stop() {
		running = false;
		exitChecker.interrupt();
		stdOutListener.interrupt();
		stdErrListener.interrupt();
		
		minecraftInstance.destroy();
		logger.info("Stopped Minecraft instance {}", processID);
	}
	
	// Thread Operations
	/**
	 * Listens to the provided {@link BufferedReader} and logs the lines to the console.
	 *
	 * @param reader The {@link BufferedReader} to listen to.
	 */
	private void readerListener(BufferedReader reader) {
		try {
			String line;
			while (running && (line = reader.readLine()) != null) {
				logger.info("[Minecraft Log #{}] {}", processID, line);
				logs.add(arguments.profile.version.logging().parse(line));
			}
			
		} catch (IOException e) {
			logger.error("Error while reading Minecraft stream {}: ", Thread.currentThread().getName(), e);
		}
	}
	
	/**
	 * Checks for the exit status of the Minecraft process and logs the exit code.
	 */
	private void exitChecker() {
		try {
			int exitCode = minecraftInstance.waitFor();
			logger.info("Minecraft process exited with code: {}", exitCode);
			running = false;
		} catch (InterruptedException ignored) {} // this will return automatically
	}
	
	/**
	 * Represents a collection of process listeners.
	 */
	@ObjectCollection
	@StaticClass
	public static class Collection {
		public static Map<Long, ProcessListener> listeners = new ConcurrentHashMap<>();
		
		/**
		 * Gets the set of process listeners in the collection.
		 *
		 * @return The set of process listeners.
		 */
		public static java.util.Collection<ProcessListener> get() {
			return listeners.values();
		}
		
		/**
		 * Add a process listener to the collection.
		 *
		 * @param listener The process listener to be added.
		 */
		public static void add(ProcessListener listener) {
			listeners.put(listener.processID, listener);
		}
		
		/**
		 * Remove a process listener from the collection.
		 *
		 * @param listener The process listener to be removed.
		 */
		public static void remove(ProcessListener listener) {
			listeners.remove(listener.processID);
		}
		
		/**
		 * Resolves a process listener by process ID from the collection.
		 *
		 * @param pid The process ID of the process listener to be resolved.
		 * @return The resolved process listener, or {@code null} if not found.
		 */
		public static ProcessListener resolve(long pid) {
			return listeners.get(pid);
		}
	}
}
