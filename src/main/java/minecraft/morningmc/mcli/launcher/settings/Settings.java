package minecraft.morningmc.mcli.launcher.settings;

import minecraft.morningmc.mcli.utils.annotations.StaticClass;
import minecraft.morningmc.mcli.utils.functions.ExceptionUtils;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Manages all the settings of the launcher.
 */
@StaticClass
public class Settings {
	private static final Logger logger = LogManager.getLogger();
	/** {@link NbtLoader} for loading and saving {@link Settings} objects from/to NBT data. */
	public static final NbtLoader<Void, CompoundTag> loader = new NbtLoader<>() {
		
		@Override
		public Void load(CompoundTag tag) {
			try {
				GlobalSettings.loader.load(tag.getCompound("global_settings"));
			} catch (Exception e) {
				logger.warn("Failed to load global settings: {}", ExceptionUtils.getMessages(e));
				GlobalSettings.initDefault();
			}
			
			try {
				FileSettings.loader.load(tag.getCompound("file_settings"));
			} catch (Exception e) {
				logger.warn("Failed to load file settings: {}", ExceptionUtils.getMessages(e));
				FileSettings.initDefault();
			}
			
			try {
				NetworkSettings.loader.load(tag.getCompound("network_settings"));
			} catch (Exception e) {
				logger.warn("Failed to load network settings: {}", ExceptionUtils.getMessages(e));
				NetworkSettings.initDefault();
			}
			
			try {
				UISettings.loader.load(tag.getCompound("ui_settings"));
			} catch (Exception e) {
				logger.warn("Failed to load ui settings: {}", ExceptionUtils.getMessages(e));
				UISettings.initDefault();
			}
			
			init();
			return null;
		}
		
		@Override
		public CompoundTag save(Void object) {
			CompoundTag tag = new CompoundTag();
			
			tag.put("global_settings", GlobalSettings.loader.save(null));
			tag.put("file_settings", FileSettings.loader.save(null));
			tag.put("network_settings", NetworkSettings.loader.save(null));
			tag.put("ui_settings", UISettings.loader.save(null));
			
			return tag;
		}
	};
	
	/**
	 * Initializes the {@link Settings}.
	 */
	public static void init() {
	}
	
	/**
	 * Initializes the {@link Settings} in default.
	 */
	public static void initDefault() {
		GlobalSettings.initDefault();
		FileSettings.initDefault();
		NetworkSettings.initDefault();
		UISettings.initDefault();
		
		init();
	}
}
