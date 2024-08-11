package minecraft.morningmc.mcli.launcher.main;

import minecraft.morningmc.mcli.launcher.settings.GlobalSettings;
import minecraft.morningmc.mcli.ui.Translation;
import minecraft.morningmc.mcli.launcher.settings.SettingsManager;
import minecraft.morningmc.mcli.minecraft.client.profile.ProfileCollection;
import minecraft.morningmc.mcli.minecraft.java.JavaRuntimeCollection;
import minecraft.morningmc.mcli.minecraft.launch.Launcher;
import minecraft.morningmc.mcli.utils.annotations.StaticClass;

import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * A Utility class for managing configurations.
 */
@StaticClass
public final class ConfigHelper implements Runnable {
	private static final Logger logger = LogManager.getLogger();
	
	private static Thread thread = null;
	
	/**
	 * Loads the configurations from the given compound tag.
	 *
	 * @param config The compound tag containing the configurations.
	 */
	public static void loadConfigs(CompoundTag config) {
		try {
			SettingsManager.loader.load(config.getCompound("settingsManager"));
		} catch (Exception e) {
			logger.warn("Failed to load settingsManager: {}", e.getMessage());
			SettingsManager.initDefault();
		}
		
		try {
			ProfileCollection.loader.load(config.getList("profileCollection"));
		} catch (Exception e) {
			logger.warn("Failed to load profileCollection: {}", e.getMessage());
			ProfileCollection.init(Set.of());
		}
		
		try {
			JavaRuntimeCollection.loader.load(config.getList("javaRuntimeCollection"));
		} catch (Exception e) {
			logger.warn("Failed to load javaRuntimeCollection: {}", e.getMessage());
			JavaRuntimeCollection.init(Set.of());
		}
		JavaRuntimeCollection.search();
		
		try {
			Translation.loader.load(config.getString("translation"));
		} catch (Exception e) {
			logger.warn("Failed to load translation: {}", e.getMessage());
			Translation.init("en");
		}
	}
	
	/**
	 * Saves the configurations to the given compound tag.
	 *
	 * @param config The compound tag to save the configurations to.
	 */
	public static void saveConfigs(CompoundTag config) {
		config.put("settingsManager", SettingsManager.loader.save(null));
		config.put("profileCollection", ProfileCollection.loader.save(ProfileCollection.instance));
		config.put("javaRuntimeCollection", JavaRuntimeCollection.loader.save(JavaRuntimeCollection.instance));
		config.put("translation", Translation.loader.save(Translation.instance));
	}
	
	/**
	 * Saves all configurations.
	 */
	public static void saveAll() {
		logger.info("Saving configurations...");
		
		CompoundTag config = new CompoundTag();
		
		ConfigHelper.saveConfigs(config);
		config.put("launcher", Launcher.loader.save(Main.instance.launcher));
		
		try {
			new Nbt().toFile(config, FileManager.config);
		} catch (Exception e) {
			logger.error("Failed to save config: ", e);
		}
	}
	
	/**
	 * Starts the auto-save thread.
	 */
	public static void startAutoSave() {
		if (thread == null) {
			logger.info("Starting auto-save thread...");
			thread = new Thread(new ConfigHelper(), "config");
			thread.start();
		}
	}
	
	/**
	 * Stops the auto-save thread.
	 */
	public static void stopAutoSave() {
		if (thread != null) {
			logger.info("Stopping auto-save thread...");
			thread.interrupt();
		}
	}
	
	/**
	 * The main task of auto-save thread.
	 */
	@Override
	public void run() {
		while (true) {
			if (GlobalSettings.autoSaveConfigInterval > 0) {
				try {
					Thread.sleep(GlobalSettings.autoSaveConfigInterval);
				} catch (InterruptedException e) {
					break;
				}
				
				saveAll();
				
			} else {
				Thread.onSpinWait();
			}
		}
	}
}