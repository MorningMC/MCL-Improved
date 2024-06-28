package minecraft.morningmc.mcli.launcher;

import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

/**
 * Represents the global settings.
 */
public final class GlobalSettings {
	
	/**
	 * {@code NbtLoader} for loading and saving {@code GlobalSettings} objects from/to NBT data.
	 */
	public static final NbtLoader<GlobalSettings, CompoundTag> loader = new NbtLoader<>() {
		@Override
		public GlobalSettings load(CompoundTag tag) {
			maxThreads = tag.getInt("maxThreads").getValue();
			minSizePerThread = tag.getInt("minSizePerThread").getValue();
			bufferSize = tag.getInt("bufferSize").getValue();
			timeout = tag.getInt("timeout").getValue();
			maxRetries = tag.getInt("maxRetries").getValue();
			maxRecommendMemory = tag.getLong("maxRecommendMemory").getValue();
			
			return new GlobalSettings();
		}
		
		@Override
		public CompoundTag save(GlobalSettings object) {
			CompoundTag tag = new CompoundTag();
			
			tag.putInt("maxThreads", maxThreads);
			tag.putInt("minSizePerThread", minSizePerThread);
			tag.putInt("bufferSize", bufferSize);
			tag.putInt("timeout", timeout);
			tag.putInt("maxRetries", maxRetries);
			tag.putLong("maxRecommendMemory", maxRecommendMemory);
			
			return tag;
		}
	};
	
	/** The maximum number of download threads to use. */
	public static int maxThreads;
	
	/** The minimum size per download thread in KB. */
	public static int minSizePerThread;
	
	/** The size of the buffer in Byte. */
	public static int bufferSize;
	
	/** The connection timeout in milliseconds. */
	public static int timeout;
	
	/** The maximum number of retries. */
	public static int maxRetries;
	
	/** The maximum recommended memory in MB. */
	public static long maxRecommendMemory;
	
	/**
	 * Initializes the global settings in default.
	 */
	public static void initDefault() {
		maxThreads = 64;
		minSizePerThread = 64;
		bufferSize = 1024;
		timeout = 16000;
		maxRetries = 8;
		maxRecommendMemory = 8192L;
	}
}