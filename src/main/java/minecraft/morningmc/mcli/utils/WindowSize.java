package minecraft.morningmc.mcli.utils;

import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

/**
 * Represents the size and state (full-screen or windowed) of a window.
 */
public record WindowSize(boolean fullScreen, int width, int height) {
	
	/** {@link NbtLoader} for loading and saving {@link WindowSize} objects from/to NBT data. */
	public static final NbtLoader<WindowSize, CompoundTag> loader = new NbtLoader<>() {
		
		/**
		 * Loads a {@link WindowSize} object from an NBT CompoundTag.
		 *
		 * @param tag The compound tag to load from.
		 * @return The loaded {@link WindowSize} object.
		 */
		@Override
		public WindowSize load(CompoundTag tag) {
			try {
				boolean fullScreen = tag.getByte("fullScreen").getValue() != 0;
				int width = tag.getInt("width").getValue();
				int height = tag.getInt("height").getValue();
				
				return new WindowSize(fullScreen, width, height);
			} catch (Exception e) {
				return null;
			}
		}
		
		/**
		 * Saves a {@link WindowSize} object to an NBT CompoundTag.
		 *
		 * @param object The {@link WindowSize} object to be saved.
		 * @return The resulting compound tag.
		 */
		@Override
		public CompoundTag save(WindowSize object) {
			CompoundTag tag = new CompoundTag();
			
			if (object == null) {
				return tag;
			}
			
			tag.putByte("fullScreen", (byte) (object.fullScreen ? 1 : 0));
			tag.putInt("width", object.width);
			tag.putInt("height", object.height);
			
			return tag;
		}
	};
	
	/**
	 * Creates a {@link WindowSize} object representing a full-screen window.
	 *
	 * @return The {@link WindowSize} object for a full-screen window.
	 */
	public static WindowSize fullScreened() {
		return new WindowSize(true, 0, 0);
	}
	
	/**
	 * Creates a {@link WindowSize} object representing a window with the specified width and height.
	 *
	 * @param width  The width of the window.
	 * @param height The height of the window.
	 * @return The {@link WindowSize} object for a window with the specified width and height.
	 * @throws IndexOutOfBoundsException If width or height is negative.
	 */
	public static WindowSize windowed(int width, int height) {
		if (width < 0 || height < 0) {
			throw new IndexOutOfBoundsException("width or height can't be negative");
		}
		
		return new WindowSize(false, width, height);
	}
}