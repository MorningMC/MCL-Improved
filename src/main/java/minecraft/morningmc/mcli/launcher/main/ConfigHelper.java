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

import java.io.IOException;
import java.util.*;

/**
 * Utility class for managing configurations.
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
			Window.SizeManager.loader.load(config.getCompound("window_size_manager"));
		} catch (Exception e) {
			logger.warn("Failed to load window_size_manager: {}", e.getMessage());
		}
		
		try {
			Translation.loader.load(config.getString("translation"));
		} catch (Exception e) {
			logger.warn("Failed to load translation: {}", e.getMessage());
			Translation.init("en");
		}
		
		try {
			Launcher.loader.load(config.getCompound("launcher"));
		} catch (Exception e) {
			logger.warn("Failed to load launcher: {}", e.getMessage());
			Launcher.initDefault();
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
		config.put("window_size_manager", Window.SizeManager.loader.save(null));
		config.put("translation", Translation.loader.save(Translation.instance));
		config.put("launcher", Launcher.loader.save(null));
	}
	
	/**
	 * Loads all configurations.
	 */
	public static void loadAll() {
		logger.info("Loading configurations...");
		
		FileManager.workingRoot.mkdirs(); // create working directory
		if (FileManager.config.exists()) {
			// backup config
			logger.info("Config found! Backing up config...");
			
			try {
				FileManager.configBackup.createNewFile();
				FileManager.copyFile(FileManager.config, FileManager.configBackup);
			} catch (Exception e) {
				logger.warn("Failed to backup config: {}", e.getMessage());
			}
			
		} else {
			// try complete config
			try {
				FileManager.config.createNewFile();
				
				// try reverse config
				if (FileManager.configBackup.exists()) {
					FileManager.copyFile(FileManager.configBackup, FileManager.config);
				}
			} catch (Exception e) {
				logger.error("Failed to complete config file: ", e);
			}
		}
		
		// load config
		logger.info("Loading configurations...");
		CompoundTag config;
		try {
			config = new Nbt().fromFile(FileManager.config);
		} catch (IOException e) {
			logger.warn("Failed to load config: {}", e.getMessage());
			config = new CompoundTag();
		}
		
		ConfigHelper.loadConfigs(config);
	}
	
	/**
	 * Saves all configurations.
	 */
	public static void saveAll() {
		logger.info("Saving configurations...");
		
		// save config to file
		// this process sometimes throws exceptions, so some retries are required to be taken
		for (byte retries = 0;;) {
			CompoundTag config = new CompoundTag();
			ConfigHelper.saveConfigs(config);
			
			try {
				new Nbt().toFile(config, FileManager.config); // exception may throw here
				
				// code below will be executed if config successfully saved to file
				// otherwise an exception will be thrown and jump to the catch branch
				logger.info("Configuration successfully saved to file: {}", FileManager.config);
				break; // break out the while loop to stop retries
				
			} catch (Exception e) {
				logger.warn("Failed to save config, tried {}: {}", ++retries /* variable retries updated here */, e.getMessage());
				
				if (retries >= GlobalSettings.saveConfigMaxRetries) { // if exceptions still throw
					logger.error("Failed to save config after {} retries.", retries);
					
					// try reverse config
					// this prevents file format corruption
					if (FileManager.configBackup.exists()) {
						logger.info("Backup file found! reverting...");
						
						try {
							FileManager.copyFile(FileManager.configBackup, FileManager.config);
						} catch (Exception ex) {
							logger.error("Failed to revert to backup: ", ex);
						}
					}
					
					break; // break out the while loop
				}
			}
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
					saveAll();
				} catch (InterruptedException e) {
					break;
				}
			} else {
				Thread.onSpinWait();
			}
		}
	}
}