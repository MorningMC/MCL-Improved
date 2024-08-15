package minecraft.morningmc.mcli.launcher.settings;

import minecraft.morningmc.mcli.utils.annotations.StaticClass;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

/**
 * Represents the global settings.
 */
@StaticClass
public final class GlobalSettings extends Settings {
	/** {@link NbtLoader} for loading and saving {@link GlobalSettings} objects from/to NBT data. */
	public static final NbtLoader<Void, CompoundTag> loader = new NbtLoader<>() {
		
		@Override
		public Void load(CompoundTag tag) {
			timestampFormat = tag.getString("timestamp_format").getValue();
			maxRecommendMemory = tag.getLong("max_recommend_memory").getValue();
			autoSaveInterval = tag.getInt("auto_save_interval").getValue();
			easterEggs = tag.getByte("easter_eggs").getValue() == 1;
			
			init();
			return null;
		}
		
		@Override
		public CompoundTag save(Void object) {
			CompoundTag tag = new CompoundTag();
			
			tag.putString("timestamp_format", timestampFormat);
			tag.putLong("max_recommend_memory", maxRecommendMemory);
			tag.putInt("auto_save_interval", autoSaveInterval);
			tag.putByte("easter_eggs", easterEggs ? (byte) 1 : (byte) 0);
			
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
	public static int autoSaveInterval;
	
	/** Whether to enable the Easter eggs. */
	public static boolean easterEggs;
	
	/**
	 * Initializes the {@link GlobalSettings}.
	 */
	public static void init() {
	}
	
	/**
	 * Initializes the {@link GlobalSettings} in default.
	 */
	public static void initDefault() {
		timestampFormat = "yyyy-MM-dd'T'HH:mm:ss:SSSZZ";
		maxRecommendMemory = 8192;
		autoSaveInterval = 300000;
		easterEggs = true;
		
		init();
	}
}