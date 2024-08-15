package minecraft.morningmc.mcli.utils;

import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

/**
 * Represents the size and state (full-screen or windowed) of a window.
 *
 * @param fullScreen Whether the window is full-screen.
 * @param width      The width of the window in pixel.
 * @param height     The height of the window in pixel.
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
				return new WindowSize(
						tag.getByte("full_screen").getValue() != 0,
						tag.getInt("width").getValue(),
						tag.getInt("height").getValue()
				);
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
			
			if (object != null) {
				tag.putByte("full_screen", (byte) (object.fullScreen ? 1 : 0));
				tag.putInt("width", object.width);
				tag.putInt("height", object.height);
			}
			
			return tag;
		}
	};
	
	/**
	 * Creates a {@link WindowSize} object representing a full-screen window.
	 *
	 * @return The {@link WindowSize} object for a full-screen window.
	 */
	public static WindowSize window() {
		return new WindowSize(true, 0, 0);
	}
	
	/**
	 * Creates a {@link WindowSize} object representing a window with the specified width and height.
	 *
	 * @param width  The width of the window in pixel.
	 * @param height The height of the window in pixel.
	 * @return The {@link WindowSize} object for a window with the specified width and height.
	 * @throws IndexOutOfBoundsException If width or height is negative.
	 */
	public static WindowSize window(int width, int height) {
		if (width < 0) {
			throw new IndexOutOfBoundsException("width can't be negative");
		}
		if (height < 0) {
			throw new IndexOutOfBoundsException("height can't be negative");
		}
		
		return new WindowSize(false, width, height);
	}
	
	/**
	 * Constructs a new {@link WindowSize} object with the specified full-screen state.
	 *
	 * @param fullScreen Whether the window should be full-screen or not.
	 * @return A new {@link WindowSize}.
	 */
	public WindowSize fullScreen(boolean fullScreen) {
		return new WindowSize(fullScreen, width, height);
	}
	
	/**
	 * Constructs a new {@link WindowSize} object with the specified width.
	 *
	 * @param width The width of the window in pixel.
	 * @return A new {@link WindowSize}.
	 * @throws IndexOutOfBoundsException If width is negative.
	 */
	public WindowSize width(int width) {
		if (width < 0) {
			throw new IndexOutOfBoundsException("width can't be negative");
		}
		
		return new WindowSize(fullScreen, width, height);
	}
	
	/**
	 * Constructs a new {@link WindowSize} object with the specified height.
	 *
	 * @param height The height of the window in pixel.
	 * @return A new {@link WindowSize}.
	 * @throws IndexOutOfBoundsException If height is negative.
	 */
	public WindowSize height(int height) {
		if (height < 0) {
			throw new IndexOutOfBoundsException("height can't be negative");
		}

		return new WindowSize(fullScreen, width, height);
	}
}