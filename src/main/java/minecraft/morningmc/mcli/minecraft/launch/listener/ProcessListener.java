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
	private static final Logger logger = LogManager.getLogger();
	
	public final Process minecraftInstance;
	public final long processID;
	
	public volatile boolean running = true;
	
	public final Thread stdOutListener;
	public final Thread stdErrListener;
	public final Thread exitChecker;
	
	public final List<String> logs;
	
	/**
	 * Constructs a {@link ProcessListener} for the given Minecraft process.
	 *
	 * @param minecraftInstance The Minecraft process to monitor.
	 */
	public ProcessListener(Process minecraftInstance) {
		this.minecraftInstance = minecraftInstance;
		processID = minecraftInstance.pid();
		
		stdOutListener = new Thread(() -> readerListener(minecraftInstance.inputReader()), "stdOutListener#" + processID);
		stdErrListener = new Thread(() -> readerListener(minecraftInstance.errorReader()), "stdErrListener#" + processID);
		exitChecker = new Thread(this::exitChecker, "exitChecker#" + processID);
		logs = new ArrayList<>();
		
		stdOutListener.start();
		stdErrListener.start();
		exitChecker.start();
		logger.info("Started listening for Minecraft instance {}", processID);
		
		// Add this listener to the collection of active listeners
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
		logger.info("Stopped Minecraft instance {}", minecraftInstance.pid());
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
				logs.add(line);
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
}
