package minecraft.morningmc.mcli.launcher.networking.download;

import minecraft.morningmc.mcli.utils.containers.Modifiable;

public class DownloadManager {
	/** The maximum number of threads to use. */
	public static final Modifiable<Integer> MAX_THREADS = Modifiable.of(64);
	
	/** The minimum size per thread in KB. */
	public static final Modifiable<Integer> MIN_SIZE_PER_THREAD = Modifiable.of(1024);
	
	/** The size of the buffer in Byte. */
	public static final Modifiable<Integer> BUFFER_SIZE = Modifiable.of(1024);
	
	/** The timeout in milliseconds. */
	public static final Modifiable<Integer> TIMEOUT = Modifiable.of(16000);
	
	/** The maximum number of retries. */
	public static final Modifiable<Integer> MAX_RETRIES = Modifiable.of(8);
}
