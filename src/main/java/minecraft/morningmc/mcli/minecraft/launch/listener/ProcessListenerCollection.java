package minecraft.morningmc.mcli.minecraft.launch.listener;

import minecraft.morningmc.mcli.utils.annotations.ObjectCollection;

import java.util.*;

/**
 * A collection of process listeners.
 */
@ObjectCollection
public class ProcessListenerCollection {
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
		instance.listeners.put(listener.pid, listener);
	}
	
	/**
	 * Remove a process listener from the collection.
	 *
	 * @param listener The process listener to be removed.
	 */
	public static void remove(ProcessListener listener) {
		instance.listeners.remove(listener.pid);
	}
	
	/**
	 * Resolves a process listener by process ID from the collection.
	 *
	 * @param pid The process ID of the process listener to be resolved.
	 * @return The resolved process listener, or {@code null} if not found.
	 */
	public static ProcessListener resolve(long pid) {
		return instance.listeners.get(pid);
	}
}
