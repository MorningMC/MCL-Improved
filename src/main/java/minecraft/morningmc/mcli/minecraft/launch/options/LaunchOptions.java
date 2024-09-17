package minecraft.morningmc.mcli.minecraft.launch.options;

import minecraft.morningmc.mcli.launcher.settings.FileSettings;
import minecraft.morningmc.mcli.minecraft.client.MinecraftDirectory;
import minecraft.morningmc.mcli.minecraft.java.JavaRuntime;
import minecraft.morningmc.mcli.utils.containers.*;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.primitive.StringTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents the options used for launching Minecraft.
 */
public final class LaunchOptions {
	private static final Logger logger = LogManager.getLogger();
	
	/** {@link NbtLoader} for loading and saving {@link LaunchOptions} objects from/to NBT data. */
	public static final NbtLoader<LaunchOptions, CompoundTag> loader = new NbtLoader<>() {
		
		@Override
		public LaunchOptions load(CompoundTag tag) {
			Switchable<JavaRuntime> javaRuntime;
			try {
				javaRuntime = Switchable.generateLoader(JavaRuntime.loader).load(tag.getCompound("java_runtime"));
			} catch (Exception e) {
				logger.warn("java_runtime load failed: {}", e.getMessage());
				javaRuntime = defaultOptions.javaRuntime;
			}
			
			Switchable<MemoryRange> memoryRange;
			try {
				memoryRange = Switchable.generateLoader(MemoryRange.loader).load(tag.getCompound("memory_range"));
			} catch (Exception e) {
				logger.warn("memory_range load failed: {}", e.getMessage());
				memoryRange = defaultOptions.memoryRange;
			}
			
			Switchable<List<String>> javaArguments;
			try {
				javaArguments = Switchable.generateLoader(NbtLoader.stringListLoader).load(tag.getCompound("java_arguments"));
			} catch (Exception e) {
				logger.warn("java_arguments load failed: {}", e.getMessage());
				javaArguments = defaultOptions.javaArguments;
			}
			
			Map<String, String> customArgumentParameters;
			try {
				customArgumentParameters = tag.getCompound("custom_argument_parameters").getValue().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> ((StringTag) entry.getValue()).getValue()));
			} catch (Exception e) {
				logger.warn("custom_argument_parameters load failed: {}", e.getMessage());
				customArgumentParameters = defaultOptions.customArgumentParameters;
			}
			
			Switchable<String> watermark;
			try {
				watermark = Switchable.generateLoader(NbtLoader.stringLoader).load(tag.getCompound("watermark"));
			} catch (Exception e) {
				logger.warn("watermark load failed: {}", e.getMessage());
				watermark = defaultOptions.watermark;
			}
			
			Switchable<MinecraftDirectory> gameDir;
			try {
				gameDir = Switchable.generateLoader(MinecraftDirectory.loader).load(tag.getCompound("game_dir"));
			} catch (Exception e) {
				logger.warn("game_dir load failed: {}", e.getMessage());
				gameDir = defaultOptions.gameDir;
			}
			
			WindowSize windowSize;
			try {
				windowSize = WindowSize.loader.load(tag.getCompound("window_size"));
			} catch (Exception e) {
				logger.warn("window_size load failed: {}", e.getMessage());
				windowSize = defaultOptions.windowSize;
			}
			
			QuickPlay quickPlay;
			try {
				quickPlay = QuickPlay.loader.load(tag.getCompound("quick_play"));
			} catch (Exception e) {
				logger.warn("quick_play load failed: {}", e.getMessage());
				quickPlay = defaultOptions.quickPlay;
			}
			
			boolean demo;
			try {
				demo = tag.getByte("demo").getValue() != 0;
			} catch (Exception e) {
				logger.warn("demo load failed: {}", e.getMessage());
				demo = defaultOptions.demo;
			}
			
			return new LaunchOptions(javaRuntime, memoryRange, javaArguments, customArgumentParameters, watermark, gameDir, windowSize, quickPlay, demo);
		}
		
		@Override
		public CompoundTag save(LaunchOptions object) {
			CompoundTag tag = new CompoundTag();
			
			try {
				tag.put("java_runtime", Switchable.generateLoader(JavaRuntime.loader).save(object.javaRuntime));
			} catch (Exception e) {
				logger.warn("java_runtime save failed: {}", e.getMessage());
			}
			
			try {
				tag.put("memory_range", Switchable.generateLoader(MemoryRange.loader).save(object.memoryRange));
			} catch (Exception e) {
				logger.warn("memory_range save failed: {}", e.getMessage());
			}
			
			try {
				tag.put("java_arguments", Switchable.generateLoader(NbtLoader.stringListLoader).save(object.javaArguments));
			} catch (Exception e) {
				logger.warn("java_arguments save failed: {}", e.getMessage());
			}
			
			try {
				tag.put("custom_argument_parameters", new CompoundTag(object.customArgumentParameters.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> new StringTag(entry.getValue())))));
			} catch (Exception e) {
				logger.warn("custom_argument_parameters save failed: {}", e.getMessage());
			}
			
			try {
				tag.put("watermark", Switchable.generateLoader(NbtLoader.stringLoader).save(object.watermark));
			} catch (Exception e) {
				logger.warn("watermark save failed: {}", e.getMessage());
			}
			
			try {
				
				tag.put("game_dir", Switchable.generateLoader(MinecraftDirectory.loader).save(object.gameDir));
			} catch (Exception e) {
				logger.warn("game_dir save failed: {}", e.getMessage());
			}
			
			try {
				tag.put("window_size", WindowSize.loader.save(object.windowSize));
			} catch (Exception e) {
				logger.warn("window_size save failed: {}", e.getMessage());
			}
			
			try {
				tag.put("quick_play", QuickPlay.loader.save(object.quickPlay));
			} catch (Exception e) {
				logger.warn("quick_play save failed: {}", e.getMessage());
			}
			
			try {
				tag.putByte("demo", object.demo ? (byte) 1 : (byte) 0);
			} catch (Exception e) {
				logger.warn("demo save failed: {}", e.getMessage());
			}
			
			return tag;
		}
	};
	
	/** The default launch options. */
	public static final LaunchOptions defaultOptions = new LaunchOptions(
			Switchable.ofDisabled(JavaRuntime.Collection.getOne()),
			Switchable.ofDisabled(MemoryRange.of(2048)),
			Switchable.ofDisabled(List.of("-XX:+UnlockExperimentalVMOptions", "-XX:+UseG1GC", "-XX:G1NewSizePercent=20", "-XX:G1ReservePercent=20", "-XX:MaxGCPauseMillis=50", "-XX:G1HeapRegionSize=32M")),
			Map.of(),
			Switchable.ofDisabled("MCLI"),
			Switchable.ofDisabled(new MinecraftDirectory(FileSettings.standardRoot)),
			WindowSize.window(1024, 768),
			QuickPlay.none(),
			false
	);
	
	public Switchable<JavaRuntime> javaRuntime;
	public Switchable<MemoryRange> memoryRange;
	public Switchable<List<String>> javaArguments;
	public Map<String, String> customArgumentParameters;
	public Switchable<String> watermark;
	public Switchable<MinecraftDirectory> gameDir;
	public WindowSize windowSize;
	public QuickPlay quickPlay;
	public boolean demo;
	
	/**
	 * Constructs a new {@link LaunchOptions} object with the specified options.
	 *
	 * @param javaRuntime              The {@link Switchable} object for Java runtime.
	 * @param memoryRange              The {@link Switchable} object for memory range.
	 * @param javaArguments            The {@link Switchable} object for Java arguments.
	 * @param customArgumentParameters The custom argument parameters.
	 * @param watermark                The {@link Switchable} object for watermark.
	 * @param gameDir                  The {@link Switchable} object for the game directory.
	 * @param windowSize               The window size.
	 * @param quickPlay                The Quick Play info.
	 * @param demo                     Whether to run in demo mode.
	 */
	public LaunchOptions(Switchable<JavaRuntime> javaRuntime,
	                     Switchable<MemoryRange> memoryRange,
	                     Switchable<List<String>> javaArguments,
						 Map<String, String> customArgumentParameters,
	                     Switchable<String> watermark,
	                     Switchable<MinecraftDirectory> gameDir,
	                     WindowSize windowSize,
	                     QuickPlay quickPlay,
	                     boolean demo) {
		this.javaRuntime = javaRuntime;
		this.memoryRange = memoryRange;
		this.javaArguments = javaArguments;
		this.customArgumentParameters = customArgumentParameters;
		this.watermark = watermark;
		this.gameDir = gameDir;
		this.windowSize = windowSize;
		this.quickPlay = quickPlay;
		this.demo = demo;
	}
}
