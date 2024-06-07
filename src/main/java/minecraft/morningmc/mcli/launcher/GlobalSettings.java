package minecraft.morningmc.mcli.launcher;

import minecraft.morningmc.mcli.utils.containers.Modifiable;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

/**
 * Represents the global settings.
 *
 * @param maxThreads The maximum number of download threads to use.
 * @param minSizePerThread The minimum size per download thread in KB.
 * @param bufferSize The size of the buffer in Byte.
 * @param timeout The connection timeout in milliseconds.
 * @param maxRetries The maximum number of retries.
 * @param maxRecommendMemory The maximum recommended memory in MB.
 */
public record GlobalSettings(Modifiable<Integer> maxThreads,
                             Modifiable<Integer> minSizePerThread,
                             Modifiable<Integer> bufferSize,
                             Modifiable<Integer> timeout,
                             Modifiable<Integer> maxRetries,
                             Modifiable<Long> maxRecommendMemory) {
	
	/** NbtLoader for loading and saving {@code GlobalSettings} objects from/to NBT data. */
	public static final NbtLoader<GlobalSettings, CompoundTag> loader = new NbtLoader<>() {
		@Override
		public GlobalSettings load(CompoundTag tag) throws IllegalNbtException {
			init(new GlobalSettings(
					Modifiable.of(tag.getInt("maxThreads").getValue()),
					Modifiable.of(tag.getInt("minSizePerThread").getValue()),
					Modifiable.of(tag.getInt("bufferSize").getValue()),
					Modifiable.of(tag.getInt("timeout").getValue()),
					Modifiable.of(tag.getInt("maxRetries").getValue()),
					Modifiable.of(tag.getLong("maxRecommendMemory").getValue())
			));
			return instance;
		}
		
		@Override
		public CompoundTag save(GlobalSettings object) {
			CompoundTag tag = new CompoundTag();
			
			tag.putInt("maxThreads", object.maxThreads.get());
			tag.putInt("minSizePerThread", object.minSizePerThread.get());
			tag.putInt("bufferSize", object.bufferSize.get());
			tag.putInt("timeout", object.timeout.get());
			tag.putInt("maxRetries", object.maxRetries.get());
			tag.putLong("maxRecommendMemory", object.maxRecommendMemory.get());
			
			return tag;
		}
	};
	
	/** The default global settings. */
	public static final GlobalSettings DEFAULT = new GlobalSettings(
			Modifiable.of(64),
			Modifiable.of(64),
			Modifiable.of(1024),
			Modifiable.of(16000),
			Modifiable.of(8),
			Modifiable.of(8192L)
	);
	
	public static GlobalSettings instance = null;
	
	/**
	 * Initializes the global settings.
	 *
	 * @param settings The global settings.
	 */
	public static void init(GlobalSettings settings) {
		if (instance != null) {
			throw new IllegalStateException("GlobalSettings already initialized");
		}
		instance = settings;
	}
}
