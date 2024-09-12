package minecraft.morningmc.mcli.minecraft.launch.options;

import minecraft.morningmc.mcli.launcher.settings.GlobalSettings;
import minecraft.morningmc.mcli.utils.Platform;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

import java.io.*;
import java.util.regex.*;

/**
 * A record that manages a range of memory.
 *
 * @param minimum The minimum memory in MB.
 * @param maximum The maximum memory in MB.
 */
public record MemoryRange(long minimum, long maximum) {
	/** {@link NbtLoader} for loading and saving {@link MemoryRange} objects from/to NBT data. */
	public static final NbtLoader<MemoryRange, CompoundTag> loader = new NbtLoader<>() {
		
		/**
		 * Loads a {@link MemoryRange} object from an NBT compound tag.
		 *
		 * @param tag The NBT compound tag representing the {@link MemoryRange} object.
		 * @return The loaded {@link MemoryRange} object, or null if an error occurs.
		 */
		@Override
		public MemoryRange load(CompoundTag tag) {
			long minimum = tag.getLong("minimum").getValue();
			long maximum = tag.getLong("maximum").getValue();
			
			return of(minimum, maximum);
		}
		
		/**
		 * Saves a {@link MemoryRange} object to an NBT compound tag.
		 *
		 * @param object The {@link MemoryRange} object to be saved.
		 * @return The NBT compound tag representing the {@link MemoryRange} object.
		 */
		@Override
		public CompoundTag save(MemoryRange object) {
			CompoundTag tag = new CompoundTag();
			
			tag.putLong("minimum", object.minimum);
			tag.putLong("maximum", object.maximum);
			
			return tag;
		}
	};
	
	/** The total memory of the system in MB. */
	public static final long totalMemory = resolveTotalMemory();
	
	/**
	 * Resolves the total memory of the system in MB.
	 *
	 * @return The total memory of the system in MB.
	 */
	private static long resolveTotalMemory() {
		long totalMemory = -1;
		if (Platform.system.operatingSystem == Platform.OperatingSystem.LINUX) {
			try (BufferedReader reader = new BufferedReader(new FileReader("/proc/meminfo"))) {
				for (String line; ( line = reader.readLine() ) != null; ) {
					Matcher matcher = Pattern.compile("^(?<key>.*?):\\s+(?<value>\\d+) kB?$").matcher(line);
					
					if (matcher.matches()) {
						if (matcher.group("key").equals("MemTotal")) {
							totalMemory = Long.parseLong(matcher.group("value")) / 1024;
							break;
						}
					}
				}
			} catch (Exception ignored) {}
		}
		
		if (totalMemory == -1) {
			try {
				java.lang.management.OperatingSystemMXBean bean = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
				if (bean instanceof com.sun.management.OperatingSystemMXBean) {
					com.sun.management.OperatingSystemMXBean sunBean = (com.sun.management.OperatingSystemMXBean) java.lang.management.ManagementFactory.getOperatingSystemMXBean();
					totalMemory = sunBean.getTotalMemorySize() >> 20 /* convert Bytes to MB */;
				} else {
					totalMemory = 1024;
				}
			} catch (NoClassDefFoundError e) {
				totalMemory = 1024;
			}
		}
		
		return totalMemory;
	}
	
	/**
	 * Constructs a new {@link MemoryRange} object with a maximum of {@code maximum} MB.
	 *
	 * @param maximum The maximum memory in MB.
	 * @return A new {@link MemoryRange} object.
	 * @throws IllegalArgumentException if {@code maximum} is less than zero.
	 */
	public static MemoryRange of(long maximum) {
		return of(0, maximum);
	}
	
	/**
	 * Constructs a new {@link MemoryRange} object with
	 * a maximum of {@code maximum} MB and a minimum of {@code minimum} MB.
	 *
	 * @param minimum The minimum memory in MB.
	 * @param maximum The maximum memory in MB.
	 * @return A new {@link MemoryRange} object.
	 * @throws IllegalArgumentException if {@code minimum} or {@code maximum} is less than zero,
	 *                                  or if {@code maximum} is less than {@code minimum}.
	 */
	public static MemoryRange of(long minimum, long maximum) {
		if (minimum < 0) {
			throw new IllegalArgumentException("Minimum memory cannot be less than zero.");
		}
		if (maximum < 0) {
			throw new IllegalArgumentException("Maximum memory cannot be less than zero.");
		}
		if (minimum > maximum) {
			throw new IllegalArgumentException("Minimum memory cannot be greater than maximum memory.");
		}

		return new MemoryRange(minimum, maximum);
	}
	
	/**
	 * Recommends a memory range based on the current free memory.
	 *
	 * @return A recommended memory range.
	 */
	public static MemoryRange recommendMemoryRange() {
		long recommendMemory = Math.round(totalMemory * 1. / 4. / 128.) * 128;
		return of(Math.min(recommendMemory, GlobalSettings.maxRecommendMemory));
	}
}
