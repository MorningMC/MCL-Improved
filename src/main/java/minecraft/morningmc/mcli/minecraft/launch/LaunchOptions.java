package minecraft.morningmc.mcli.minecraft.launch;

import minecraft.morningmc.mcli.minecraft.client.MinecraftDirectory;
import minecraft.morningmc.mcli.utils.QuickPlay;
import minecraft.morningmc.mcli.minecraft.java.JavaRuntime;
import minecraft.morningmc.mcli.utils.*;
import minecraft.morningmc.mcli.utils.containers.*;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Represents the options used for launching Minecraft.
 */
public final class LaunchOptions {
	private static final Logger logger = LogManager.getLogger();
	
	/** {@link NbtLoader} for loading and saving {@link LaunchOptions} objects from/to NBT data. */
	public static final NbtLoader<LaunchOptions, CompoundTag> loader = new NbtLoader<>() {
		
		@Override
		public LaunchOptions load(CompoundTag tag) throws IllegalNbtException {
			Switchable<JavaRuntime> javaRuntime;
			try {
				javaRuntime = Switchable.generateLoader(JavaRuntime.loader).load(tag.getCompound("javaRuntime"));
			} catch (Exception e) {
				logger.warn("javaRuntime load failed: {}", e.getMessage());
				javaRuntime = DEFAULT.javaRuntime;
			}
			
			Switchable<MemoryRange> memoryRange;
			try {
				memoryRange = Switchable.generateLoader(MemoryRange.loader).load(tag.getCompound("memoryRange"));
			} catch (Exception e) {
				logger.warn("memoryRange load failed: {}", e.getMessage());
				memoryRange = DEFAULT.memoryRange;
			}
			
			Switchable<List<String>> javaArguments;
			try {
				javaArguments = Switchable.generateLoader(NbtLoader.stringListLoader).load(tag.getCompound("javaArguments"));
			} catch (Exception e) {
				logger.warn("javaArguments load failed: {}", e.getMessage());
				javaArguments = DEFAULT.javaArguments;
			}
			
			boolean useWatermark;
			try {
				useWatermark = tag.getByte("useWatermark").getValue() != 0;
			} catch (Exception e) {
				logger.warn("useWatermark load failed: {}", e.getMessage());
				useWatermark = DEFAULT.useWatermark;
			}
			
			EnumSwitchable<MinecraftDirectory, MinecraftDirectory.Policy> gameDir;
			try {
				gameDir = EnumSwitchable.generateLoader(MinecraftDirectory.loader, MinecraftDirectory.Policy.class).load(tag.getCompound("gameDir"));
			} catch (Exception e) {
				logger.warn("gameDir load failed: {}", e.getMessage());
				gameDir = DEFAULT.gameDir;
			}
			
			WindowSize windowSize;
			try {
				windowSize = WindowSize.loader.load(tag.getCompound("windowSize"));
			} catch (Exception e) {
				logger.warn("windowSize load failed: {}", e.getMessage());
				windowSize = DEFAULT.windowSize;
			}
			
			QuickPlay quickPlay;
			try {
				quickPlay = QuickPlay.loader.load(tag.getCompound("quickPlay"));
			} catch (Exception e) {
				logger.warn("quickPlay load failed: {}", e.getMessage());
				quickPlay = DEFAULT.quickPlay;
			}
			
			return new LaunchOptions(javaRuntime, memoryRange, javaArguments, useWatermark, gameDir, windowSize, quickPlay);
		}
		
		@Override
		public CompoundTag save(LaunchOptions object) {
			CompoundTag tag = new CompoundTag();
			
			try {
				tag.put("javaRuntime", Switchable.generateLoader(JavaRuntime.loader).save(object.javaRuntime));
			} catch (Exception e) {
				logger.warn("javaRuntime save failed: {}", e.getMessage());
			}
			
			try {
				tag.put("memoryRange", Switchable.generateLoader(MemoryRange.loader).save(object.memoryRange));
			} catch (Exception e) {
				logger.warn("memoryRange save failed: {}", e.getMessage());
			}
			
			try {
				tag.put("javaArguments", Switchable.generateLoader(NbtLoader.stringListLoader).save(object.javaArguments));
			} catch (Exception e) {
				logger.warn("javaArguments save failed: {}", e.getMessage());
			}
			
			try {
				tag.putByte("useWatermark", (byte) ( object.useWatermark ? 1 : 0 ));
			} catch (Exception e) {
				logger.warn("useWatermark save failed: {}", e.getMessage());
			}
			
			try {
				
				tag.put("gameDir", EnumSwitchable.generateLoader(MinecraftDirectory.loader, MinecraftDirectory.Policy.class).save(object.gameDir));
			} catch (Exception e) {
				logger.warn("gameDir save failed: {}", e.getMessage());
			}
			
			try {
				tag.put("windowSize", WindowSize.loader.save(object.windowSize));
			} catch (Exception e) {
				logger.warn("windowSize save failed: {}", e.getMessage());
			}
			
			try {
				tag.put("quickPlay", QuickPlay.loader.save(object.quickPlay));
			} catch (Exception e) {
				logger.warn("quickPlay save failed: {}", e.getMessage());
			}
			
			return tag;
		}
	};
	
	/** The default launch options. */
	public static final LaunchOptions DEFAULT = new LaunchOptions(
			Switchable.ofDisabled(JavaRuntime.current),
			Switchable.ofDisabled(MemoryRange.of(2048)),
			Switchable.ofDisabled(List.of("-XX:+UnlockExperimentalVMOptions", "-XX:+UseG1GC", "-XX:G1NewSizePercent=20", "-XX:G1ReservePercent=20", "-XX:MaxGCPauseMillis=50", "-XX:G1HeapRegionSize=32M")),
			false,
			EnumSwitchable.of(MinecraftDirectory.standard, MinecraftDirectory.Policy.STANDARD),
			WindowSize.windowed(1024, 768),
			new QuickPlay(QuickPlay.Type.NONE, null, null)
	);
	
	public Switchable<JavaRuntime> javaRuntime;
	public Switchable<MemoryRange> memoryRange;
	public Switchable<List<String>> javaArguments;
	public boolean useWatermark;
	public EnumSwitchable<MinecraftDirectory, MinecraftDirectory.Policy> gameDir;
	public WindowSize windowSize;
	public QuickPlay quickPlay;
	
	/**
	 * @param javaRuntime   The {@link Switchable} object for Java runtime.
	 * @param memoryRange   The {@link Switchable} object for memory range.
	 * @param javaArguments The {@link Switchable} object for Java arguments.
	 * @param useWatermark  Whether to use the watermark.
	 * @param gameDir       The {@link EnumSwitchable} object for the game directory.
	 * @param windowSize    The window size.
	 * @param quickPlay     The Quick Play info.
	 */
	public LaunchOptions(Switchable<JavaRuntime> javaRuntime, Switchable<MemoryRange> memoryRange, Switchable<List<String>> javaArguments, boolean useWatermark, EnumSwitchable<MinecraftDirectory, MinecraftDirectory.Policy> gameDir, WindowSize windowSize, QuickPlay quickPlay) {
		this.javaRuntime = javaRuntime;
		this.memoryRange = memoryRange;
		this.javaArguments = javaArguments;
		this.useWatermark = useWatermark;
		this.gameDir = gameDir;
		this.windowSize = windowSize;
		this.quickPlay = quickPlay;
	}
}
