package minecraft.morningmc.mcli.launcher.settings;

import minecraft.morningmc.mcli.utils.WindowSize;
import minecraft.morningmc.mcli.utils.annotations.StaticClass;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

@StaticClass
public class UISettings {
	/** {@link NbtLoader} for loading and saving {@link UISettings} objects from/to NBT data. */
	public static final NbtLoader<UISettings, CompoundTag> loader = new NbtLoader<>() {
		
		@Override
		public UISettings load(CompoundTag tag) throws IllegalNbtException {
			windowSize = WindowSize.loader.load(tag.getCompound("windowSize"));
			
			return new UISettings();
		}
		
		@Override
		public CompoundTag save(UISettings object) {
			CompoundTag tag = new CompoundTag();
			
			tag.put("windowSize", WindowSize.loader.save(windowSize));
			
			return tag;
		}
	};
	
	/** The size of the window in pixel */
	public static WindowSize windowSize;
	
	/**
	 * Initializes the {@link UISettings} in default.
	 */
	public static void initDefault() {
		windowSize = WindowSize.windowed(1024, 632);
	}
}