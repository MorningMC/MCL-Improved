package minecraft.morningmc.mcli.ui.window;

import minecraft.morningmc.mcli.launcher.settings.UISettings;
import minecraft.morningmc.mcli.minecraft.launch.options.WindowSize;
import minecraft.morningmc.mcli.utils.annotations.StaticClass;
import minecraft.morningmc.mcli.utils.functions.ExceptionUtils;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

/**
 * Manages different types of window sizes.
 */
@StaticClass
public class SizeManager {
	private static final Logger logger = LogManager.getLogger();
	/** {@link NbtLoader} for loading and saving {@link SizeManager} objects from/to NBT data. */
	public static final NbtLoader<Void, CompoundTag> loader = new NbtLoader<>() {
		@Override
		public Void load(CompoundTag tag) {
			windowSizes = new ConcurrentHashMap<>(tag.getValue().entrySet().stream().flatMap(entry -> {
				try {
					return Stream.of(Map.entry(Token.valueOf(entry.getKey()), WindowSize.loader.load((CompoundTag) entry.getValue())));
				} catch (Exception e) {
					logger.warn("Illegal window size: {}", ExceptionUtils.getMessages(e));
					return Stream.empty();
				}
			}).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
			return null;
		}
		
		@Override
		public CompoundTag save(Void object) {
			CompoundTag tag = new CompoundTag();
			
			for (Map.Entry<Token, WindowSize> entry : windowSizes.entrySet()) {
				tag.put(entry.getKey().name(), WindowSize.loader.save(entry.getValue()));
			}
			
			return tag;
		}
	};
	
	/** The managed {@link WindowSize} instances. */
	private static Map<Token, WindowSize> windowSizes = new ConcurrentHashMap<>();
	
	/**
	 * Gets the {@link WindowSize} for a given handle.
	 *
	 * @param token The token of the window size.
	 * @return The {@link WindowSize} for the given handle.
	 */
	public static WindowSize get(Token token) {
		return windowSizes.getOrDefault(token, UISettings.defaultWindowSize);
	}
	
	/**
	 * Sets the {@link WindowSize} for a given handle.
	 *
	 * @param token      The token of the window size.
	 * @param windowSize The {@link WindowSize} to be set.
	 */
	public static void set(Token token, WindowSize windowSize) {
		windowSizes.put(token, windowSize);
	}
	
	/**
	 * The tokens for the windows.
	 */
	public enum Token {
		/** The main window. */
		MAIN
	}
}
