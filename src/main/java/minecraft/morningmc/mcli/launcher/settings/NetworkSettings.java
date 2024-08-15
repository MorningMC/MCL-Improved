package minecraft.morningmc.mcli.launcher.settings;

import minecraft.morningmc.mcli.utils.annotations.StaticClass;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

import java.net.Proxy;

/**
 * Represents the network settings.
 */
@StaticClass
public class NetworkSettings extends Settings {
	/** {@link NbtLoader} for loading and saving {@link NetworkSettings} objects from/to NBT data. */
	public static final NbtLoader<Void, CompoundTag> loader = new NbtLoader<>() {
		
		@Override
		public Void load(CompoundTag tag) throws IllegalNbtException {
			proxy = NbtLoader.proxyLoader.load(tag.getCompound("proxy"));
			connectTimeout = tag.getInt("connect_timeout").getValue();
			readTimeout = tag.getInt("read_timeout").getValue();
			maxThreads = tag.getShort("max_threads").getValue();
			minSizePerThread = tag.getInt("min_size_per_thread").getValue();
			bufferSize = tag.getInt("buffer_size").getValue();
			maxRetries = tag.getByte("max_retries").getValue();
			
			init();
			return null;
		}
		
		@Override
		public CompoundTag save(Void object) {
			CompoundTag tag = new CompoundTag();
			
			tag.put("proxy", NbtLoader.proxyLoader.save(proxy));
			tag.putInt("connect_timeout", connectTimeout);
			tag.putInt("read_timeout", readTimeout);
			tag.putShort("max_threads", maxThreads);
			tag.putInt("min_size_per_thread", minSizePerThread);
			tag.putInt("buffer_size", bufferSize);
			tag.putByte("max_retries", maxRetries);
			
			return tag;
		}
	};
	
	/** The proxy to be used in connection. */
	public static Proxy proxy;
	
	/** The connection timeout in milliseconds. */
	public static int connectTimeout;
	
	/** The read timeout in milliseconds. */
	public static int readTimeout;
	
	// Download
	/** The maximum number of download threads to use. */
	public static short maxThreads;
	
	/** The minimum size per download thread in KB. */
	public static int minSizePerThread;
	
	/** The size of the buffer in Byte. */
	public static int bufferSize;
	
	/** The maximum number of retries. */
	public static byte maxRetries;
	
	/**
	 * Initializes the {@link NetworkSettings}.
	 */
	public static void init() {
	}
	
	/**
	 * Initializes the {@link NetworkSettings} in default.
	 */
	public static void initDefault() {
		proxy = Proxy.NO_PROXY;
		connectTimeout = 16000;
		readTimeout = 16000;
		maxThreads = 64;
		minSizePerThread = 64;
		bufferSize = 1024;
		maxRetries = 8;
		
		init();
	}
}