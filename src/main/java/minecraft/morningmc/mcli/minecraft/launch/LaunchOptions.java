package minecraft.morningmc.mcli.minecraft.launch;

import minecraft.morningmc.mcli.minecraft.client.directory.TargetMinecraftDirectory;
import minecraft.morningmc.mcli.minecraft.java.JavaRuntime;
import minecraft.morningmc.mcli.utils.ServerInfo;
import minecraft.morningmc.mcli.utils.WindowSize;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.*;

public class LaunchOptions {
	private static final Logger LOGGER = LogManager.getLogger();
	
	/** NbtLoader for loading and saving LaunchOptions objects from/to NBT data. */
	public static final NbtLoader<LaunchOptions, CompoundTag> LOADER = new NbtLoader<>() {
		
		@Override
		public LaunchOptions loadFromNbt(CompoundTag tag) throws IllegalNbtException {
			boolean autoSelectJavaRuntimes;
			try {
				autoSelectJavaRuntimes = tag.getByte("autoSelectJavaRuntimes").getValue() != 0;
			} catch (Exception e) {
				LOGGER.warn("autoSelectJavaRuntimes load failed: " + e.getMessage());
				autoSelectJavaRuntimes = DEFAULT.autoSelectJavaRuntimes;
			}
			
			JavaRuntime javaRuntime;
			try {
				javaRuntime = JavaRuntime.fromPath(new File(tag.getString("javaRuntime").getValue()));
			} catch (Exception e) {
				LOGGER.warn("javaRuntime load failed: " + e.getMessage());
				javaRuntime = DEFAULT.javaRuntime;
			}
			
			int maxMemory;
			try {
				maxMemory = tag.getInt("maxMemory").getValue();
			} catch (Exception e) {
				LOGGER.warn("maxMemory load failed: " + e.getMessage());
				maxMemory = DEFAULT.maxMemory;
			}
			
			int minMemory;
			try {
				minMemory = tag.getInt("minMemory").getValue();
			} catch (Exception e) {
				LOGGER.warn("minMemory load failed: " + e.getMessage());
				minMemory = DEFAULT.minMemory;
			}
			
			boolean useCustomJavaArguments;
			try {
				useCustomJavaArguments = tag.getByte("useCustomJavaArguments").getValue() != 0;
			} catch (Exception e) {
				LOGGER.warn("useCustomJavaArguments load failed: " + e.getMessage());
				useCustomJavaArguments = DEFAULT.useCustomJavaArguments;
			}
			
			List<String> customJavaArguments;
			try {
				customJavaArguments = NbtLoader.STRING_LIST_LOADER.loadFromNbt(tag.getList("customJavaArguments"));
			} catch (Exception e) {
				LOGGER.warn("customJavaArguments load failed: " + e.getMessage());
				customJavaArguments = DEFAULT.customJavaArguments;
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
			
			ServerInfo serverInfo;
			try {
				serverInfo = ServerInfo.LOADER.loadFromNbt(tag.getCompound("serverInfo"));
			} catch (Exception e) {
				LOGGER.warn("serverInfo load failed: " + e.getMessage());
				serverInfo = DEFAULT.serverInfo;
			}
			
			return new LaunchOptions(autoSelectJavaRuntimes, javaRuntime, maxMemory, minMemory, useCustomJavaArguments, customJavaArguments, useWaterMark, gameDirPolicy, gameDir, windowSize, serverInfo);
		}
		
		@Override
		public CompoundTag saveToNbt(LaunchOptions object) {
			CompoundTag tag = new CompoundTag();
			
			try {
				tag.putByte("autoSelectJavaRuntimes", (byte) (object.autoSelectJavaRuntimes ? 1 : 0));
			} catch (Exception e) {
				LOGGER.warn("autoSelectJavaRuntimes save failed: " + e.getMessage());
			}
			
			try {
				tag.putString("javaRuntime", object.javaRuntime.executable().getAbsolutePath());
			} catch (Exception e) {
				LOGGER.warn("javaRuntime save failed: " + e.getMessage());
			}
			
			try {
				tag.putInt("maxMemory", object.maxMemory);
			} catch (Exception e) {
				LOGGER.warn("maxMemory save failed: " + e.getMessage());
			}
			
			try {
				tag.putInt("minMemory", object.minMemory);
			} catch (Exception e) {
				LOGGER.warn("minMemory save failed: " + e.getMessage());
			}
			
			try {
				tag.putByte("useCustomJavaArguments", (byte) (object.useCustomJavaArguments ? 1 : 0));
			} catch (Exception e) {
				LOGGER.warn("useCustomJavaArguments save failed: " + e.getMessage());
			}
			
			try {
				tag.put("customJavaArguments", NbtLoader.STRING_LIST_LOADER.saveToNbt(object.customJavaArguments));
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
				tag.put("serverInfo", ServerInfo.LOADER.saveToNbt(object.serverInfo));
			} catch (Exception e) {
				LOGGER.warn("serverInfo save failed: " + e.getMessage());
			}
			
			return tag;
		}
	};
	public static final LaunchOptions DEFAULT = new LaunchOptions(
			true,
			null,
			2048,
			0,
			false,
			List.of("-XX:+UnlockExperimentalVMOptions", "-XX:+UseG1GC", "-XX:G1NewSizePercent=20", "-XX:G1ReservePercent=20", "-XX:MaxGCPauseMillis=50", "-XX:G1HeapRegionSize=32M"),
			false,
			TargetMinecraftDirectory.Policy.SOURCE,
			TargetMinecraftDirectory.STANDARD,
			WindowSize.window(1024, 768),
			null
	);
	
	private boolean autoSelectJavaRuntimes;
	private JavaRuntime javaRuntime;
	private int maxMemory;
	private int minMemory;
	private boolean useCustomJavaArguments;
	private List<String> customJavaArguments;
	private boolean useWaterMark;
	private TargetMinecraftDirectory.Policy gameDirPolicy;
	private TargetMinecraftDirectory gameDir;
	private WindowSize windowSize;
	private ServerInfo serverInfo;
	
	public LaunchOptions(boolean autoSelectJavaRuntimes,
	                     JavaRuntime javaRuntime,
						 int maxMemory,
						 int minMemory,
						 boolean useCustomJavaArguments,
						 List<String> customJavaArguments,
						 boolean useWaterMark,
						 TargetMinecraftDirectory.Policy gameDirPolicy,
	                     TargetMinecraftDirectory gameDir,
	                     WindowSize windowSize,
	                     ServerInfo serverInfo) {

		this.autoSelectJavaRuntimes = autoSelectJavaRuntimes;
		this.javaRuntime = javaRuntime;
		this.maxMemory = maxMemory;
		this.minMemory = minMemory;
		this.useCustomJavaArguments = useCustomJavaArguments;
		this.customJavaArguments = customJavaArguments;
		this.useWaterMark = useWaterMark;
		this.gameDirPolicy = gameDirPolicy;
		this.gameDir = gameDir;
		this.windowSize = windowSize;
		this.serverInfo = serverInfo;
	}
	
	// Getters
	public boolean isAutoSelectJavaRuntimes() {
		return autoSelectJavaRuntimes;
	}
	
	public JavaRuntime getJavaRuntime() {
		return javaRuntime;
	}
	
	public int getMaxMemory() {
		return maxMemory;
	}
	
	public int getMinMemory() {
		return minMemory;
	}
	
	public boolean isUseCustomJavaArguments() {
		return useCustomJavaArguments;
	}
	
	public List<String> getCustomJavaArguments() {
		return customJavaArguments;
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
	
	public ServerInfo getServerInfo() {
		return serverInfo;
	}
	
	// Setters
	public void setAutoSelectJavaRuntimes(boolean autoSelectJavaRuntimes) {
		this.autoSelectJavaRuntimes = autoSelectJavaRuntimes;
	}
	
	public void setJavaRuntime(JavaRuntime javaRuntime) {
		this.javaRuntime = javaRuntime;
	}
	
	public void setMaxMemory(int maxMemory) {
		this.maxMemory = maxMemory;
	}
	
	public void setMinMemory(int minMemory) {
		this.minMemory = minMemory;
	}
	
	public void setUseCustomJavaArguments(boolean useCustomJavaArguments) {
		this.useCustomJavaArguments = useCustomJavaArguments;
	}
	
	public void setCustomJavaArguments(List<String> customJavaArguments) {
		this.customJavaArguments = customJavaArguments;
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
	
	public void setServerInfo(ServerInfo serverInfo) {
		this.serverInfo = serverInfo;
	}
}
