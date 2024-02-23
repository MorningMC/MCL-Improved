package minecraft.morningmc.mcli.utils;

import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

/**
 * Represents the size and state (full-screen or windowed) of a window.
 */
public record WindowSize(boolean fullScreen,
                         int width,
                         int height) {
	
	/** NbtLoader for loading and saving {@code WindowSize} objects from/to NBT data. */
	public static final NbtLoader<WindowSize, CompoundTag> LOADER = new NbtLoader<>() {
		
		/**
		 * Loads a WindowSize object from an NBT CompoundTag.
		 *
		 * @param tag The CompoundTag to load from.
		 * @return The loaded WindowSize object.
		 * @throws IllegalNbtException If there is an issue with the NBT data.
		 */
		@Override
		public WindowSize loadFromNbt(CompoundTag tag) throws IllegalNbtException {
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
		 * Saves a WindowSize object to an NBT CompoundTag.
		 *
		 * @param object The WindowSize object to save.
		 * @return The resulting CompoundTag.
		 */
		@Override
		public CompoundTag saveToNbt(WindowSize object) {
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
	 * Creates a WindowSize object representing a full-screen window.
	 *
	 * @return The WindowSize object for a full-screen window.
	 */
	public static WindowSize fullScreenWindow() {
		return new WindowSize(true, 0, 0);
	}
	
	/**
	 * Creates a WindowSize object representing a window with the specified width and height.
	 *
	 * @param width  The width of the window.
	 * @param height The height of the window.
	 * @return The WindowSize object for a window with the specified width and height.
	 * @throws IndexOutOfBoundsException If width or height is negative.
	 */
	public static WindowSize window(int width, int height) {
		if (width < 0 || height < 0) {
			throw new IndexOutOfBoundsException("width or height can't be negative");
		}
		
		return new WindowSize(false, width, height);
	}
}