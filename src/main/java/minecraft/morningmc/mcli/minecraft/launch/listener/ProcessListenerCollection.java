package minecraft.morningmc.mcli.minecraft.launch.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class ProcessListenerCollection {
	private static final Logger LOGGER = LogManager.getLogger();
	
	public static ProcessListenerCollection instance = new ProcessListenerCollection();
	
	public Map<Long, ProcessListener> listeners;
	
	/**
	 * Gets the set of process listeners in the collection.
	 *
	 * @return The set of process listeners.
	 */
	public static Collection<ProcessListener> get() {
		return instance.listeners.values();
	}
	
	/**
	 * Add a process listener to the collection.
	 *
	 * @param listener The process listener to be added.
	 */
	public static void add(ProcessListener listener) {
		instance.listeners.put(listener.getPid(), listener);
	}
	
	/**
	 * Remove a process listener from the collection.
	 *
	 * @param listener The process listener to be removed.
	 */
	public static void remove(ProcessListener listener) {
		instance.listeners.remove(listener.getPid());
	}
	
	/**
	 * Resolves a process listener by process ID from the collection.
	 *
	 * @param pid The process ID of the process listener to be resolved.
	 * @return The resolved {@code ProcessListener} object, or null if not found.
	 */
	public static ProcessListener resolve(long pid) {
		return instance.listeners.get(pid);
	}
}
