package minecraft.morningmc.mcli.launcher.settings;

import minecraft.morningmc.mcli.utils.annotations.StaticClass;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

/**
 * Represents the global settings.
 */
@StaticClass
public final class GlobalSettings {
	
	/** {@link NbtLoader} for loading and saving {@link GlobalSettings} objects from/to NBT data. */
	public static final NbtLoader<Void, CompoundTag> loader = new NbtLoader<>() {
		@Override
		public Void load(CompoundTag tag) {
			timestampFormat = tag.getString("timestampFormat").getValue();
			maxRecommendMemory = tag.getLong("maxRecommendMemory").getValue();
			autoSaveConfigInterval = tag.getInt("autoSaveConfigInterval").getValue();
			easterEggs = tag.getByte("easterEggs").getValue() == 1;
			
			return null;
		}
		
		@Override
		public CompoundTag save(Void object) {
			CompoundTag tag = new CompoundTag();
			
			tag.putString("timestampFormat", timestampFormat);
			tag.putLong("maxRecommendMemory", maxRecommendMemory);
			tag.putInt("autoSaveConfigInterval", autoSaveConfigInterval);
			tag.putByte("easterEggs", easterEggs ? (byte) 1 : (byte) 0);
			
			return tag;
		}
	};
	
	// Formats
	/** The format of the timestamp. */
	public static String timestampFormat;
	
	// Miscellaneous
	/** The maximum recommended memory in MB. */
	public static long maxRecommendMemory;
	
	/** The interval at which the auto-save is made in milliseconds, or 0 or negative to turn off. */
	public static int autoSaveConfigInterval;
	
	/** Whether to enable the Easter eggs. */
	public static boolean easterEggs;
	
	/**
	 * Initializes the {@link GlobalSettings} class in default.
	 */
	public static void initDefault() {
		timestampFormat = "yyyy-MM-dd'T'HH:mm:ss:SSSZZ";
		maxRecommendMemory = 8192;
		autoSaveConfigInterval = 300000;
		easterEggs = true;
	}
}