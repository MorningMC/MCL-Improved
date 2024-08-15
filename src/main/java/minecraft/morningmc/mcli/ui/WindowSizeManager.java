package minecraft.morningmc.mcli.ui;

import minecraft.morningmc.mcli.launcher.settings.UISettings;
import minecraft.morningmc.mcli.utils.WindowSize;
import minecraft.morningmc.mcli.utils.annotations.StaticClass;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;
import java.util.stream.Collectors;

@StaticClass
public class WindowSizeManager {
	private static final Logger logger = LogManager.getLogger();
	
	/** {@link NbtLoader} for loading and saving {@link WindowSizeManager} objects from/to NBT data. */
	public static final NbtLoader<Void, CompoundTag> loader = new NbtLoader<>() {
		@Override
		public Void load(CompoundTag tag) {
			windowSizes = new ConcurrentHashMap<>(tag.getValue().entrySet().stream()
					                                      .flatMap(entry -> {
															  try {
																  return Stream.of(Map.entry(
																		  Handle.valueOf(entry.getKey()),
																		  WindowSize.loader.load((CompoundTag) entry.getValue())
																  ));
															  } catch (IllegalNbtException e) {
																  logger.warn("Illegal window size: {}", e.getMessage());
																  return Stream.empty();
															  }
					                                      })
					                                      .collect(Collectors.toMap(
																  Map.Entry::getKey,
							                                      Map.Entry::getValue
					                                      ))
			);
			return null;
		}
		
		@Override
		public CompoundTag save(Void object) {
			CompoundTag tag = new CompoundTag();
			
			for (Map.Entry<Handle, WindowSize> entry : windowSizes.entrySet()) {
				tag.put(entry.getKey().name(), WindowSize.loader.save(entry.getValue()));
			}
			
			return tag;
		}
	};
	
	/** The managed {@link WindowSize} instances. */
	private static Map<Handle, WindowSize> windowSizes = new ConcurrentHashMap<>();
	
	/**
	 * Gets the {@link WindowSize} for a given handle.
	 *
	 * @param handle The handle of the window size.
	 * @return The {@link WindowSize} for the given handle.
	 */
	public static WindowSize get(Handle handle) {
		return windowSizes.getOrDefault(handle, UISettings.defaultWindowSize);
	}
	
	/**
	 * Sets the {@link WindowSize} for a given handle.
	 *
	 * @param handle     The handle of the window size.
	 * @param windowSize The {@link WindowSize} to be set.
	 */
	public static void set(Handle handle, WindowSize windowSize) {
		windowSizes.put(handle, windowSize);
	}
	
	/**
	 * The handles for the windows.
	 */
	public enum Handle {
		/** The main window. */
		MAIN
	}
}
