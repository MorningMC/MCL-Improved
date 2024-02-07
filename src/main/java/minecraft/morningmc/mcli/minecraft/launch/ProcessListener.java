package minecraft.morningmc.mcli.minecraft.launch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.List;

/**
 * A utility class for listening to the output streams (stdout, stderr) of a Minecraft process.
 * It provides separate threads for listening to stdout, stderr, and checking for process exit.
 */
public class ProcessListener {
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final Process minecraftInstance;
	
	private volatile boolean running = true;
	
	private final Thread stdOutListener;
	private final Thread stdErrListener;
	private final Thread exitChecker;
	
	private final List<String> minecraftLogs;
	
	/**
	 * Constructs a ProcessListener for the given Minecraft process.
	 *
	 * @param minecraftInstance The Minecraft process to monitor.
	 */
	public ProcessListener(Process minecraftInstance) {
		this.minecraftInstance = minecraftInstance;
		long pid = minecraftInstance.pid();
		
		stdOutListener = new Thread(() -> streamListener(minecraftInstance.getInputStream()), "stdOutListener#" + pid);
		stdErrListener = new Thread(() -> streamListener(minecraftInstance.getErrorStream()), "stdErrListener#" + pid);
		exitChecker = new Thread(this::exitChecker, "exitChecker#" + pid);
		minecraftLogs = List.of();
		
		stdOutListener.start();
		stdErrListener.start();
		exitChecker.start();
		
		LOGGER.info("Started listening for Minecraft instance " + pid);
	}
	
	/**
	 * Stops the process listener and its associated threads.
	 */
	public void stop() {
		running = false;
		exitChecker.interrupt();
	}
	
	// Thread Operations
	
	/**
	 * Listens to the provided input stream and logs the lines to the console.
	 *
	 * @param stream The input stream to listen to.
	 */
	private void streamListener(InputStream stream) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
			String line;
			
			while (running && (line = reader.readLine()) != null) {
				LOGGER.info("[Minecraft Log] " + line);
				minecraftLogs.add(line);
			}
			
		} catch (IOException e) {
			LOGGER.error("Error while reading Minecraft stream " + Thread.currentThread().getName() + ": ", e);
		}
	}
	
	/**
	 * Checks for the exit status of the Minecraft process and logs the exit code.
	 */
	private void exitChecker() {
		try {
			int exitCode = minecraftInstance.waitFor();
			LOGGER.info("Minecraft process exited with code: " + exitCode);
			
			running = false;
			
		} catch (InterruptedException e) {
			LOGGER.error("exitChecker interrupted: ", e);
		}
	}
	
	// Getters
	
	/**
	 * Gets the Minecraft process being monitored.
	 *
	 * @return The Minecraft process.
	 */
	public Process getMinecraftInstance() {
		return minecraftInstance;
	}
	
	/**
	 * Checks if the ProcessListener is still running.
	 *
	 * @return {@code true} if running, {@code false} otherwise.
	 */
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * Gets the thread responsible for listening to stdout.
	 *
	 * @return The stdout listener thread.
	 */
	public Thread getStdOutListener() {
		return stdOutListener;
	}
	
	/**
	 * Gets the thread responsible for listening to stderr.
	 *
	 * @return The stderr listener thread.
	 */
	public Thread getStdErrListener() {
		return stdErrListener;
	}
	
	/**
	 * Gets the thread responsible for checking the process exit.
	 *
	 * @return The exit checker thread.
	 */
	public Thread getExitChecker() {
		return exitChecker;
	}
	
	/**
	 * Gets the Minecraft logs collected during the process.
	 *
	 * @return The list of Minecraft logs.
	 */
	public List<String> getMinecraftLogs() {
		return minecraftLogs;
	}
}
