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
	public static final NbtLoader<Void, CompoundTag> loader = new NbtLoader<>() {
		
		@Override
		public Void load(CompoundTag tag) throws IllegalNbtException {
			GlobalSettings.loader.load(tag.getCompound("globalSettings"));
			NetworkSettings.loader.load(tag.getCompound("networkSettings"));
			UISettings.loader.load(tag.getCompound("uiSettings"));
			
			return null;
		}
		
		@Override
		public CompoundTag save(Void object) {
			CompoundTag tag = new CompoundTag();
			
			tag.put("globalSettings", GlobalSettings.loader.save(null));
			tag.put("networkSettings", NetworkSettings.loader.save(null));
			tag.put("uiSettings", UISettings.loader.save(null));
			
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
