package minecraft.morningmc.mcli.minecraft.launch;

import minecraft.morningmc.mcli.minecraft.client.MinecraftDirectory;
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
 *
 * @param javaRuntime The {@code Switchable} object for Java runtime.
 * @param memoryRange The {@code Switchable} object for memory range.
 * @param javaArguments The {@code Switchable} object for Java arguments.
 * @param useWaterMark The {@code Modifiable} object for whether to use the watermark.
 * @param gameDir The {@code EnumSwitchable} object for the game directory.
 * @param windowSize The {@code Modifiable} object for the window size.
 * @param serverInfo The {@code Switchable} object for the server info.
 */
public record LaunchOptions (Switchable<JavaRuntime> javaRuntime,
                             Switchable<MemoryRange> memoryRange,
                             Switchable<List<String>> javaArguments,
                             Modifiable<Boolean> useWaterMark,
                             EnumSwitchable<MinecraftDirectory, MinecraftDirectory.Policy> gameDir,
                             Modifiable<WindowSize> windowSize,
                             Switchable<ServerInfo> serverInfo) {
	private static final Logger logger = LogManager.getLogger();
	
	/** NbtLoader for loading and saving {@code LaunchOptions} objects from/to NBT data. */
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
			
			Modifiable<Boolean> useWaterMark;
			try {
				useWaterMark = Modifiable.of(tag.getByte("useWaterMark").getValue() != 0);
			} catch (Exception e) {
				logger.warn("useWaterMark load failed: {}", e.getMessage());
				useWaterMark = DEFAULT.useWaterMark;
			}
			
			EnumSwitchable<MinecraftDirectory, MinecraftDirectory.Policy> gameDir;
			try {
				gameDir = EnumSwitchable.generateLoader(MinecraftDirectory.loader, MinecraftDirectory.Policy.class).load(tag.getCompound("gameDir"));
			} catch (Exception e) {
				logger.warn("gameDir load failed: {}", e.getMessage());
				gameDir = DEFAULT.gameDir;
			}
			
			Modifiable<WindowSize> windowSize;
			try {
				windowSize = Modifiable.of(WindowSize.loader.load(tag.getCompound("windowSize")));
			} catch (Exception e) {
				logger.warn("windowSize load failed: {}", e.getMessage());
				windowSize = DEFAULT.windowSize;
			}
			
			Switchable<ServerInfo> serverInfo;
			try {
				serverInfo = Switchable.generateLoader(ServerInfo.loader).load(tag.getCompound("serverInfo"));
			} catch (Exception e) {
				logger.warn("serverInfo load failed: {}", e.getMessage());
				serverInfo = DEFAULT.serverInfo;
			}
			
			return new LaunchOptions(javaRuntime, memoryRange, javaArguments, useWaterMark, gameDir, windowSize, serverInfo);
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
				tag.putByte("useWaterMark", (byte) (object.useWaterMark.get() ? 1 : 0));
			} catch (Exception e) {
				logger.warn("useWaterMark save failed: {}", e.getMessage());
			}
			
			try {
				
				tag.put("gameDir", EnumSwitchable.generateLoader(MinecraftDirectory.loader, MinecraftDirectory.Policy.class).save(object.gameDir));
			} catch (Exception e) {
				logger.warn("gameDir save failed: {}", e.getMessage());
			}
			
			try {
				tag.put("windowSize", WindowSize.loader.save(object.windowSize.get()));
			} catch (Exception e) {
				logger.warn("windowSize save failed: {}", e.getMessage());
			}
			
			try {
				tag.put("serverInfo", Switchable.generateLoader(ServerInfo.loader).save(object.serverInfo));
			} catch (Exception e) {
				logger.warn("serverInfo save failed: {}", e.getMessage());
			}
			
			return tag;
		}
	};
	
	/** The default launch options. */
	public static final LaunchOptions DEFAULT = new LaunchOptions(
			Switchable.ofDisabled(JavaRuntime.current),
			Switchable.ofDisabled(MemoryRange.of(2048)),
			Switchable.ofDisabled(List.of("-XX:+UnlockExperimentalVMOptions", "-XX:+UseG1GC", "-XX:G1NewSizePercent=20", "-XX:G1ReservePercent=20", "-XX:MaxGCPauseMillis=50", "-XX:G1HeapRegionSize=32M")),
			Modifiable.of(false),
			EnumSwitchable.of(MinecraftDirectory.standard, MinecraftDirectory.Policy.STANDARD),
			Modifiable.of(WindowSize.window(1024, 768)),
			Switchable.ofDisabled(null)
	);
}
