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
public class NetworkSettings {
	/** {@link NbtLoader} for loading and saving {@link NetworkSettings} objects from/to NBT data. */
	public static final NbtLoader<Void, CompoundTag> loader = new NbtLoader<>() {
		@Override
		public Void load(CompoundTag tag) throws IllegalNbtException {
			proxy = NbtLoader.proxyLoader.load(tag.getCompound("proxy"));
			connectTimeout = tag.getInt("connectTimeout").getValue();
			readTimeout = tag.getInt("readTimeout").getValue();
			maxThreads = tag.getShort("maxThreads").getValue();
			minSizePerThread = tag.getInt("minSizePerThread").getValue();
			bufferSize = tag.getInt("bufferSize").getValue();
			maxRetries = tag.getByte("maxRetries").getValue();
			
			return null;
		}
		
		@Override
		public CompoundTag save(Void object) {
			CompoundTag tag = new CompoundTag();
			
			tag.put("proxy", NbtLoader.proxyLoader.save(proxy));
			tag.putInt("connectTimeout", connectTimeout);
			tag.putInt("readTimeout", readTimeout);
			tag.putShort("maxThreads", maxThreads);
			tag.putInt("minSizePerThread", minSizePerThread);
			tag.putInt("bufferSize", bufferSize);
			tag.putByte("maxRetries", maxRetries);
			
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
	 * Initializes the {@link NetworkSettings} class in default.
	 */
	public static void initDefault() {
		proxy = Proxy.NO_PROXY;
		connectTimeout = 16000;
		readTimeout = 16000;
		maxThreads = 64;
		minSizePerThread = 64;
		bufferSize = 1024;
		maxRetries = 8;
	}
}