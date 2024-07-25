package minecraft.morningmc.mcli.launcher.settings;

import minecraft.morningmc.mcli.utils.annotations.StaticClass;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

/**
 * Manages all the settings of the launcher.
 */
@StaticClass
public class SettingsManager {
	/** {@link NbtLoader} for loading and saving {@link SettingsManager} objects from/to NBT data. */
	public static final NbtLoader<SettingsManager, CompoundTag> loader = new NbtLoader<>() {
		
		@Override
		public SettingsManager load(CompoundTag tag) throws IllegalNbtException {
			GlobalSettings.loader.load(tag.getCompound("globalSettings"));
			NetworkSettings.loader.load(tag.getCompound("networkSettings"));
			UISettings.loader.load(tag.getCompound("uiSettings"));
			
			return new SettingsManager();
		}
		
		@Override
		public CompoundTag save(SettingsManager object) {
			CompoundTag tag = new CompoundTag();
			
			tag.put("globalSettings", GlobalSettings.loader.save(new GlobalSettings()));
			tag.put("networkSettings", NetworkSettings.loader.save(new NetworkSettings()));
			tag.put("uiSettings", UISettings.loader.save(new UISettings()));
			
			return tag;
		}
	};
	
	/**
	 * Initializes all the settings in default.
	 */
	public static void initDefault() {
		GlobalSettings.initDefault();
		NetworkSettings.initDefault();
		UISettings.initDefault();
	}
}
