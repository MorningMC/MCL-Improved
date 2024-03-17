package minecraft.morningmc.mcli.utils;

import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

/**
 * A record that manages a range of memory.
 *
 * @param minimum The minimum memory in MB.
 * @param maximum The maximum memory in MB.
 */
public record MemoryRange(int minimum, int maximum) {
	/** NbtLoader for loading and saving {@code MemoryRange} objects from/to NBT data. */
	public static final NbtLoader<MemoryRange, CompoundTag> LOADER = new NbtLoader<>() {
		
		/**
		 * Loads a {@code MemoryRange} object from an NBT compound tag.
		 *
		 * @param tag The NBT compound tag representing the {@code MemoryRange} object.
		 * @return The loaded {@code MemoryRange} object, or null if an error occurs.
		 */
		@Override
		public MemoryRange loadFromNbt(CompoundTag tag) throws IllegalNbtException {
			int minimum = tag.getInt("minimum").getValue();
			int maximum = tag.getInt("maximum").getValue();
			
			return of(minimum, maximum);
		}
		
		/**
		 * Saves a {@code MemoryRange} object to an NBT compound tag.
		 *
		 * @param object The {@code MemoryRange} object to be saved.
		 * @return The NBT compound tag representing the {@code MemoryRange} object.
		 */
		@Override
		public CompoundTag saveToNbt(MemoryRange object) {
			CompoundTag tag = new CompoundTag();
			
			tag.putInt("minimum", object.minimum);
			tag.putInt("maximum", object.maximum);
			
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
	public static MemoryRange of(int maximum) {
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
	public static MemoryRange of(int minimum, int maximum) {
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
}
