package minecraft.morningmc.mcli.launcher.main;

import minecraft.morningmc.mcli.launcher.settings.GlobalSettings;
import minecraft.morningmc.mcli.minecraft.client.Profile;
import minecraft.morningmc.mcli.minecraft.java.JavaRuntime;
import minecraft.morningmc.mcli.ui.Window;
import minecraft.morningmc.mcli.utils.Translation;
import minecraft.morningmc.mcli.launcher.settings.Settings;
import minecraft.morningmc.mcli.minecraft.launch.Launcher;
import minecraft.morningmc.mcli.utils.FileManager;
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
			Settings.loader.load(config.getCompound("settings"));
		} catch (Exception e) {
			logger.warn("Failed to load settings: {}", e.getMessage());
			Settings.initDefault();
		}
		
		try {
			Profile.Collection.loader.load(config.getList("profile_collection"));
		} catch (Exception e) {
			logger.warn("Failed to load profile_collection: {}", e.getMessage());
			Profile.Collection.init(Set.of());
		}
		
		try {
			JavaRuntime.Collection.loader.load(config.getList("java_runtime_collection"));
		} catch (Exception e) {
			logger.warn("Failed to load java_runtime_collection: {}", e.getMessage());
			JavaRuntime.Collection.init(Set.of());
		}
		JavaRuntime.Collection.search();
		
		try {
			Window.WindowSizeManager.loader.load(config.getCompound("window_size_manager"));
		} catch (Exception e) {
			logger.warn("Failed to load window_size_manager: {}", e.getMessage());
		}
		
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
		config.put("settings", Settings.loader.save(null));
		config.put("profile_collection", Profile.Collection.loader.save(null));
		config.put("java_runtime_collection", JavaRuntime.Collection.loader.save(null));
		config.put("window_size_manager", Window.WindowSizeManager.loader.save(null));
		config.put("translation", Translation.loader.save(Translation.instance));
	}
	
	/**
	 * Saves all configurations.
	 */
	public static void saveAll() {
		logger.info("Saving configurations...");
		
		// save config to file
		byte retries = 0;
		while (retries < GlobalSettings.saveConfigMaxRetries) {
			CompoundTag config = new CompoundTag();
			
			ConfigHelper.saveConfigs(config);
			config.put("launcher", Launcher.loader.save(Main.instance.launcher));
			
			try {
				new Nbt().toFile(config, FileManager.config);
				logger.info("Configuration successfully saved to file: {}", FileManager.config);
				break;
			} catch (Exception e) {
				logger.warn("Failed to save config, tried {}: {}", ++retries, e.getMessage());
			}
		}
		if (retries >= GlobalSettings.saveConfigMaxRetries) {
			logger.error("Failed to save config after {} retries.", retries);
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
			if (GlobalSettings.autoSaveInterval > 0) {
				try {
					Thread.sleep(GlobalSettings.autoSaveInterval);
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