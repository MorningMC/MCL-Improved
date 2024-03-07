package minecraft.morningmc.mcli.minecraft.launch.listener;

import minecraft.morningmc.mcli.utils.annotations.LauncherProcess;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

/**
 * A utility class for listening to the output streams (stdout, stderr) of a Minecraft process.
 * It provides separate threads for listening to stdout, stderr, and checking for process exit.
 */
@LauncherProcess("launch")
public class ProcessListener {
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final Process minecraftInstance;
	private final long pid;
	
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
		pid = minecraftInstance.pid();
		
		stdOutListener = new Thread(() -> readerListener(minecraftInstance.inputReader()), "stdOutListener#" + pid);
		stdErrListener = new Thread(() -> readerListener(minecraftInstance.errorReader()), "stdErrListener#" + pid);
		exitChecker = new Thread(this::exitChecker, "exitChecker#" + pid);
		minecraftLogs = List.of();
		
		stdOutListener.start();
		stdErrListener.start();
		exitChecker.start();
		
		LOGGER.info("Started listening for Minecraft instance " + pid);
		ProcessListenerCollection.add(this);
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
		LOGGER.info("Stopped Minecraft instance " + minecraftInstance.pid());
	}
	
	// Thread Operations
	/**
	 * Listens to the provided {@code BufferedReader} and logs the lines to the console.
	 *
	 * @param reader The {@code BufferedReader} to listen to.
	 */
	private void readerListener(BufferedReader reader) {
		try {
			String line;
			
			while (running && (line = reader.readLine()) != null) {
				LOGGER.info("[Minecraft Log #" + pid + "] " + line);
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
			ProcessListenerCollection.remove(this);
			
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
	 * Gets the process ID of the Minecraft process.
	 *
	 * @return the process ID of the Minecraft process
	 */
	public long getPid() {
		return pid;
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
	
	// Overrides
	@Override
	public String toString() {
		return "Minecraft Instance " + minecraftInstance.pid();
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		ProcessListener that = (ProcessListener) o;
		return Objects.equals(minecraftInstance, that.minecraftInstance);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(minecraftInstance);
	}
}
