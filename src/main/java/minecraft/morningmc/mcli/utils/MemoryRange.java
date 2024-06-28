package minecraft.morningmc.mcli.utils;

import minecraft.morningmc.mcli.launcher.GlobalSettings;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
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
	/** NbtLoader for loading and saving {@code MemoryRange} objects from/to NBT data. */
	public static final NbtLoader<MemoryRange, CompoundTag> loader = new NbtLoader<>() {
		
		/**
		 * Loads a {@code MemoryRange} object from an NBT compound tag.
		 *
		 * @param tag The NBT compound tag representing the {@code MemoryRange} object.
		 * @return The loaded {@code MemoryRange} object, or null if an error occurs.
		 */
		@Override
		public MemoryRange load(CompoundTag tag) throws IllegalNbtException {
			long minimum = tag.getLong("minimum").getValue();
			long maximum = tag.getLong("maximum").getValue();
			
			return of(minimum, maximum);
		}
		
		/**
		 * Saves a {@code MemoryRange} object to an NBT compound tag.
		 *
		 * @param object The {@code MemoryRange} object to be saved.
		 * @return The NBT compound tag representing the {@code MemoryRange} object.
		 */
		@Override
		public CompoundTag save(MemoryRange object) {
			CompoundTag tag = new CompoundTag();
			
			tag.putLong("minimum", object.minimum);
			tag.putLong("maximum", object.maximum);
			
			return tag;
		}
	};
	
	/**
	 * Constructs a new {@code MemoryRange} object with a maximum of {@code maximum} MB.
	 *
	 * @param maximum The maximum memory in MB.
	 * @return A new {@code MemoryRange} object.
	 * @throws IllegalArgumentException if {@code maximum} is less than zero.
	 */
	public static MemoryRange of(long maximum) {
		return of(0, maximum);
	}
	
	/**
	 * Constructs a new {@code MemoryRange} object with a maximum of {@code maximum} MB and a minimum of {@code minimum} MB.
	 *
	 * @param minimum The minimum memory in MB.
	 * @param maximum The maximum memory in MB.
	 * @return A new {@code MemoryRange} object.
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
		long totalMemory = -1;
		if (Platform.system.operatingSystem() == Platform.OperatingSystem.LINUX) {
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
					totalMemory = sunBean.getTotalMemorySize() / 1024 / 1024;
				} else {
					totalMemory = 1024;
				}
			} catch (NoClassDefFoundError e) {
				totalMemory = 1024;
			}
		}
			
		long recommendMemory = Math.round(totalMemory * 1. / 4. / 128.) * 128;
		return of(Math.min(recommendMemory, GlobalSettings.maxRecommendMemory));
	}
}
