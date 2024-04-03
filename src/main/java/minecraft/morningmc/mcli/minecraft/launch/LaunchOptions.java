package minecraft.morningmc.mcli.minecraft.launch;

import minecraft.morningmc.mcli.minecraft.client.directory.TargetMinecraftDirectory;
import minecraft.morningmc.mcli.minecraft.java.JavaRuntime;
import minecraft.morningmc.mcli.utils.*;
import minecraft.morningmc.mcli.utils.containers.Modifiable;
import minecraft.morningmc.mcli.utils.containers.Switchable;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.*;

public record LaunchOptions (Switchable<JavaRuntime> javaRuntime,
                             Switchable<MemoryRange> memoryRange,
                             Switchable<List<String>> javaArguments,
                             Modifiable<Boolean> useWaterMark,
                             Modifiable<TargetMinecraftDirectory.Policy> gameDirPolicy,
                             Modifiable<TargetMinecraftDirectory> gameDir,
                             Modifiable<WindowSize> windowSize,
                             Switchable<ServerInfo> serverInfo) {
	private static final Logger LOGGER = LogManager.getLogger();
	
	/** NbtLoader for loading and saving {@code LaunchOptions} objects from/to NBT data. */
	public static final NbtLoader<LaunchOptions, CompoundTag> LOADER = new NbtLoader<>() {
		
		@Override
		public LaunchOptions load(CompoundTag tag) throws IllegalNbtException {
			Switchable<JavaRuntime> javaRuntime;
			try {
				javaRuntime = NbtLoader.switchableLoader(JavaRuntime.LOADER).load(tag.getCompound("javaRuntime"));
			} catch (Exception e) {
				LOGGER.warn("javaRuntime load failed: " + e.getMessage());
				javaRuntime = DEFAULT.javaRuntime;
			}
			
			Switchable<MemoryRange> memoryRange;
			try {
				memoryRange = NbtLoader.switchableLoader(MemoryRange.LOADER).load(tag.getCompound("memoryRange"));
			} catch (Exception e) {
				LOGGER.warn("memoryRange load failed: " + e.getMessage());
				memoryRange = DEFAULT.memoryRange;
			}
			
			Switchable<List<String>> customJavaArguments;
			try {
				customJavaArguments = NbtLoader.switchableLoader(NbtLoader.STRING_LIST_LOADER).load(tag.getCompound("customJavaArguments"));
			} catch (Exception e) {
				LOGGER.warn("customJavaArguments load failed: " + e.getMessage());
				customJavaArguments = DEFAULT.javaArguments;
			}
			
			Modifiable<Boolean> useWaterMark;
			try {
				useWaterMark = Modifiable.of(tag.getByte("useWaterMark").getValue() != 0);
			} catch (Exception e) {
				LOGGER.warn("useWaterMark load failed: " + e.getMessage());
				useWaterMark = DEFAULT.useWaterMark;
			}
			
			Modifiable<TargetMinecraftDirectory.Policy> gameDirPolicy;
			try {
				gameDirPolicy = Modifiable.of(TargetMinecraftDirectory.Policy.valueOf(tag.getString("gameDirPolicy").getValue()));
			} catch (Exception e) {
				LOGGER.warn("gameDirPolicy load failed: " + e.getMessage());
				gameDirPolicy = DEFAULT.gameDirPolicy;
			}
			
			Modifiable<TargetMinecraftDirectory> gameDir;
			try {
				gameDir = Modifiable.of(new TargetMinecraftDirectory(new File(tag.getString("gameDir").getValue())));
			} catch (Exception e) {
				LOGGER.warn("gameDir load failed: " + e.getMessage());
				gameDir = DEFAULT.gameDir;
			}
			
			Modifiable<WindowSize> windowSize;
			try {
				windowSize = Modifiable.of(WindowSize.LOADER.load(tag.getCompound("windowSize")));
			} catch (Exception e) {
				LOGGER.warn("windowSize load failed: " + e.getMessage());
				windowSize = DEFAULT.windowSize;
			}
			
			Switchable<ServerInfo> serverInfo;
			try {
				serverInfo = NbtLoader.switchableLoader(ServerInfo.LOADER).load(tag.getCompound("serverInfo"));
			} catch (Exception e) {
				LOGGER.warn("serverInfo load failed: " + e.getMessage());
				serverInfo = DEFAULT.serverInfo;
			}
			
			return new LaunchOptions(javaRuntime, memoryRange, customJavaArguments, useWaterMark, gameDirPolicy, gameDir, windowSize, serverInfo);
		}
		
		@Override
		public CompoundTag save(LaunchOptions object) {
			CompoundTag tag = new CompoundTag();
			
			try {
				tag.put("javaRuntime", NbtLoader.switchableLoader(JavaRuntime.LOADER).save(object.javaRuntime));
			} catch (Exception e) {
				LOGGER.warn("javaRuntime save failed: " + e.getMessage());
			}
			
			try {
				tag.put("maxMemory", NbtLoader.switchableLoader(MemoryRange.LOADER).save(object.memoryRange));
			} catch (Exception e) {
				LOGGER.warn("maxMemory save failed: " + e.getMessage());
			}
			
			try {
				tag.put("customJavaArguments", NbtLoader.switchableLoader(NbtLoader.STRING_LIST_LOADER).save(object.javaArguments));
			} catch (Exception e) {
				LOGGER.warn("customJavaArguments save failed: " + e.getMessage());
			}
			
			try {
				tag.putByte("useWaterMark", (byte) (object.useWaterMark.get() ? 1 : 0));
			} catch (Exception e) {
				LOGGER.warn("useWaterMark save failed: " + e.getMessage());
			}
			
			try {
				tag.putString("gameDirPolicy", object.gameDirPolicy.get().name());
			} catch (Exception e) {
				LOGGER.warn("gameDirPolicy save failed: " + e.getMessage());
			}
			
			try {
				tag.putString("gameDir", object.gameDir.get().getRoot().getAbsolutePath());
			} catch (Exception e) {
				LOGGER.warn("gameDir save failed: " + e.getMessage());
			}
			
			try {
				tag.put("windowSize", WindowSize.LOADER.save(object.windowSize.get()));
			} catch (Exception e) {
				LOGGER.warn("windowSize save failed: " + e.getMessage());
			}
			
			try {
				tag.put("serverInfo", NbtLoader.switchableLoader(ServerInfo.LOADER).save(object.serverInfo));
			} catch (Exception e) {
				LOGGER.warn("serverInfo save failed: " + e.getMessage());
			}
			
			return tag;
		}
	};
	public static final LaunchOptions DEFAULT = new LaunchOptions(
			Switchable.ofDisabled(JavaRuntime.CURRENT),
			Switchable.ofDisabled(MemoryRange.of(2048)),
			Switchable.ofDisabled(List.of("-XX:+UnlockExperimentalVMOptions", "-XX:+UseG1GC", "-XX:G1NewSizePercent=20", "-XX:G1ReservePercent=20", "-XX:MaxGCPauseMillis=50", "-XX:G1HeapRegionSize=32M")),
			Modifiable.of(false),
			Modifiable.of(TargetMinecraftDirectory.Policy.SOURCE),
			Modifiable.of(TargetMinecraftDirectory.STANDARD),
			Modifiable.of(WindowSize.window(1024, 768)),
			Switchable.ofDisabled(null)
	);
}
