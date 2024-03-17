package minecraft.morningmc.mcli.minecraft.launch;

import minecraft.morningmc.mcli.minecraft.client.directory.TargetMinecraftDirectory;
import minecraft.morningmc.mcli.minecraft.java.JavaRuntime;
import minecraft.morningmc.mcli.utils.*;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.*;

public class LaunchOptions {
	private static final Logger LOGGER = LogManager.getLogger();
	
	/** NbtLoader for loading and saving {@code LaunchOptions} objects from/to NBT data. */
	public static final NbtLoader<LaunchOptions, CompoundTag> LOADER = new NbtLoader<>() {
		
		@Override
		public LaunchOptions loadFromNbt(CompoundTag tag) throws IllegalNbtException {
			Switchable<JavaRuntime> javaRuntime;
			try {
				javaRuntime = NbtLoader.switchableLoader(JavaRuntime.LOADER).loadFromNbt(tag.getCompound("javaRuntime"));
			} catch (Exception e) {
				LOGGER.warn("javaRuntime load failed: " + e.getMessage());
				javaRuntime = DEFAULT.javaRuntime;
			}
			
			Switchable<MemoryRange> memoryRange;
			try {
				memoryRange = NbtLoader.switchableLoader(MemoryRange.LOADER).loadFromNbt(tag.getCompound("memoryRange"));
			} catch (Exception e) {
				LOGGER.warn("memoryRange load failed: " + e.getMessage());
				memoryRange = DEFAULT.memoryRange;
			}
			
			Switchable<List<String>> customJavaArguments;
			try {
				customJavaArguments = NbtLoader.switchableLoader(NbtLoader.STRING_LIST_LOADER).loadFromNbt(tag.getCompound("customJavaArguments"));
			} catch (Exception e) {
				LOGGER.warn("customJavaArguments load failed: " + e.getMessage());
				customJavaArguments = DEFAULT.javaArguments;
			}
			
			boolean useWaterMark;
			try {
				useWaterMark = tag.getByte("useWaterMark").getValue() != 0;
			} catch (Exception e) {
				LOGGER.warn("useWaterMark load failed: " + e.getMessage());
				useWaterMark = DEFAULT.useWaterMark;
			}
			
			TargetMinecraftDirectory.Policy gameDirPolicy;
			try {
				gameDirPolicy = TargetMinecraftDirectory.Policy.valueOf(tag.getString("gameDirPolicy").getValue());
			} catch (Exception e) {
				LOGGER.warn("gameDirPolicy load failed: " + e.getMessage());
				gameDirPolicy = DEFAULT.gameDirPolicy;
			}
			
			TargetMinecraftDirectory gameDir;
			try {
				gameDir = new TargetMinecraftDirectory(new File(tag.getString("gameDir").getValue()));
			} catch (Exception e) {
				LOGGER.warn("gameDir load failed: " + e.getMessage());
				gameDir = DEFAULT.gameDir;
			}
			
			WindowSize windowSize;
			try {
				windowSize = WindowSize.LOADER.loadFromNbt(tag.getCompound("windowSize"));
			} catch (Exception e) {
				LOGGER.warn("windowSize load failed: " + e.getMessage());
				windowSize = DEFAULT.windowSize;
			}
			
			Switchable<ServerInfo> serverInfo;
			try {
				serverInfo = NbtLoader.switchableLoader(ServerInfo.LOADER).loadFromNbt(tag.getCompound("serverInfo"));
			} catch (Exception e) {
				LOGGER.warn("serverInfo load failed: " + e.getMessage());
				serverInfo = DEFAULT.serverInfo;
			}
			
			return new LaunchOptions(javaRuntime, memoryRange, customJavaArguments, useWaterMark, gameDirPolicy, gameDir, windowSize, serverInfo);
		}
		
		@Override
		public CompoundTag saveToNbt(LaunchOptions object) {
			CompoundTag tag = new CompoundTag();
			
			try {
				tag.put("javaRuntime", NbtLoader.switchableLoader(JavaRuntime.LOADER).saveToNbt(object.javaRuntime));
			} catch (Exception e) {
				LOGGER.warn("javaRuntime save failed: " + e.getMessage());
			}
			
			try {
				tag.put("maxMemory", NbtLoader.switchableLoader(MemoryRange.LOADER).saveToNbt(object.memoryRange));
			} catch (Exception e) {
				LOGGER.warn("maxMemory save failed: " + e.getMessage());
			}
			
			try {
				tag.put("customJavaArguments", NbtLoader.switchableLoader(NbtLoader.STRING_LIST_LOADER).saveToNbt(object.javaArguments));
			} catch (Exception e) {
				LOGGER.warn("customJavaArguments save failed: " + e.getMessage());
			}
			
			try {
				tag.putByte("useWaterMark", (byte) (object.useWaterMark ? 1 : 0));
			} catch (Exception e) {
				LOGGER.warn("useWaterMark save failed: " + e.getMessage());
			}
			
			try {
				tag.putString("gameDirPolicy", object.gameDirPolicy.name());
			} catch (Exception e) {
				LOGGER.warn("gameDirPolicy save failed: " + e.getMessage());
			}
			
			try {
				tag.putString("gameDir", object.gameDir.getRoot().getAbsolutePath());
			} catch (Exception e) {
				LOGGER.warn("gameDir save failed: " + e.getMessage());
			}
			
			try {
				tag.put("windowSize", WindowSize.LOADER.saveToNbt(object.windowSize));
			} catch (Exception e) {
				LOGGER.warn("windowSize save failed: " + e.getMessage());
			}
			
			try {
				tag.put("serverInfo", NbtLoader.switchableLoader(ServerInfo.LOADER).saveToNbt(object.serverInfo));
			} catch (Exception e) {
				LOGGER.warn("serverInfo save failed: " + e.getMessage());
			}
			
			return tag;
		}
	};
	public static final LaunchOptions DEFAULT = new LaunchOptions(
			Switchable.ofDisabled(null),
			Switchable.ofDisabled(MemoryRange.of(2048)),
			Switchable.ofDisabled(List.of("-XX:+UnlockExperimentalVMOptions", "-XX:+UseG1GC", "-XX:G1NewSizePercent=20", "-XX:G1ReservePercent=20", "-XX:MaxGCPauseMillis=50", "-XX:G1HeapRegionSize=32M")),
			false,
			TargetMinecraftDirectory.Policy.SOURCE,
			TargetMinecraftDirectory.STANDARD,
			WindowSize.window(1024, 768),
			null
	);
	
	private Switchable<JavaRuntime> javaRuntime;
	private Switchable<MemoryRange> memoryRange;
	private Switchable<List<String>> javaArguments;
	private boolean useWaterMark;
	private TargetMinecraftDirectory.Policy gameDirPolicy;
	private TargetMinecraftDirectory gameDir;
	private WindowSize windowSize;
	private Switchable<ServerInfo> serverInfo;
	
	public LaunchOptions(Switchable<JavaRuntime> javaRuntime,
	                     Switchable<MemoryRange> memoryRange,
	                     Switchable<List<String>> javaArguments,
						 boolean useWaterMark,
						 TargetMinecraftDirectory.Policy gameDirPolicy,
	                     TargetMinecraftDirectory gameDir,
	                     WindowSize windowSize,
	                     Switchable<ServerInfo> serverInfo) {
		
		this.javaRuntime = javaRuntime;
		this.memoryRange = memoryRange;
		this.javaArguments = javaArguments;
		this.useWaterMark = useWaterMark;
		this.gameDirPolicy = gameDirPolicy;
		this.gameDir = gameDir;
		this.windowSize = windowSize;
		this.serverInfo = serverInfo;
	}
	
	// Getters
	public Switchable<JavaRuntime> getJavaRuntime() {
		return javaRuntime;
	}
	
	public Switchable<MemoryRange> getMemoryRange() {
		return memoryRange;
	}
	
	public Switchable<List<String>> getJavaArguments() {
		return javaArguments;
	}
	
	public boolean isUseWaterMark() {
		return useWaterMark;
	}
	
	public TargetMinecraftDirectory.Policy getGameDirPolicy() {
		return gameDirPolicy;
	}
	
	public TargetMinecraftDirectory getGameDir() {
		return gameDir;
	}
	
	public WindowSize getWindowSize() {
		return windowSize;
	}
	
	public Switchable<ServerInfo> getServerInfo() {
		return serverInfo;
	}
	
	// Setters
	public void setJavaRuntime(Switchable<JavaRuntime> javaRuntime) {
		this.javaRuntime = javaRuntime;
	}
	
	public void setMemoryRange(Switchable<MemoryRange> memoryRange) {
		this.memoryRange = memoryRange;
	}
	
	public void setJavaArguments(Switchable<List<String>> javaArguments) {
		this.javaArguments = javaArguments;
	}
	
	public void setUseWaterMark(boolean useWaterMark) {
		this.useWaterMark = useWaterMark;
	}
	
	public void setGameDirPolicy(TargetMinecraftDirectory.Policy gameDirPolicy) {
		this.gameDirPolicy = gameDirPolicy;
	}
	
	public void setGameDir(TargetMinecraftDirectory gameDir) {
		this.gameDir = gameDir;
	}
	
	public void setWindowSize(WindowSize windowSize) {
		this.windowSize = windowSize;
	}
	
	public void setServerInfo(Switchable<ServerInfo> serverInfo) {
		this.serverInfo = serverInfo;
	}
}
