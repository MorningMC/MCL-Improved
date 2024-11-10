package minecraft.morningmc.mcli.launcher.main;

import minecraft.morningmc.mcli.launcher.settings.GlobalSettings;
import minecraft.morningmc.mcli.minecraft.client.Profile;
import minecraft.morningmc.mcli.minecraft.java.JavaRuntime;
import minecraft.morningmc.mcli.ui.window.SizeManager;
import minecraft.morningmc.mcli.utils.Translation;
import minecraft.morningmc.mcli.launcher.settings.Settings;
import minecraft.morningmc.mcli.minecraft.launch.Launcher;
import minecraft.morningmc.mcli.utils.FileManager;
import minecraft.morningmc.mcli.utils.annotations.StaticClass;
import static minecraft.morningmc.mcli.utils.functions.ExceptionUtils.getMessages;

import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
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
			logger.warn("Failed to load settings: {}", getMessages(e));
			Settings.initDefault();
		}
		
		try {
			Profile.Collection.loader.load(config.getList("profile_collection"));
		} catch (Exception e) {
			logger.warn("Failed to load profile_collection: {}", getMessages(e));
			Profile.Collection.init(Set.of());
		}
		
		try {
			JavaRuntime.Collection.loader.load(config.getList("java_runtime_collection"));
		} catch (Exception e) {
			logger.warn("Failed to load java_runtime_collection: {}", getMessages(e));
			JavaRuntime.Collection.init(Set.of());
		}
		JavaRuntime.Collection.search();
		
		try {
			SizeManager.loader.load(config.getCompound("size_manager"));
		} catch (Exception e) {
			logger.warn("Failed to load size_manager: {}", getMessages(e));
		}
		
		try {
			Translation.loader.load(config.getString("translation"));
		} catch (Exception e) {
			logger.warn("Failed to load translation: {}", getMessages(e));
			Translation.init("en");
		}
		
		try {
			Launcher.loader.load(config.getCompound("launcher"));
		} catch (Exception e) {
			logger.warn("Failed to load launcher: {}", getMessages(e));
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
		config.put("size_manager", SizeManager.loader.save(null));
		config.put("translation", Translation.loader.save(Translation.instance));
		config.put("launcher", Launcher.loader.save(null));
	}
	
	/**
	 * Loads all configurations.
	 */
	public static void loadAll() {
		logger.info("Loading configurations...");
		
		FileManager.workingRoot.mkdirs(); // create working directory
		
		Optional<File> configFile;
		if (FileManager.config.exists()) { // if config exists
			logger.info("Config found! Backing up config...");
			configFile = Optional.of(FileManager.config);
			
			// backup config
			try {
				FileManager.configBackup.createNewFile();
				FileManager.copyFile(FileManager.config, FileManager.configBackup);
			} catch (Exception e) {
				logger.warn("Failed to backup config: {}", getMessages(e));
			}
			
		} else if (FileManager.configBackup.exists()) { // if config not found but backup exists
			logger.info("Config not found! Loading from backup...");
			configFile = Optional.of(FileManager.configBackup);
			
		} else { // if neither config nor backup not found
			logger.info("First launch! Using default (empty) config...");
			configFile = Optional.empty();
		}
		
		// load config
		CompoundTag config = configFile.map(file -> {
			try {
				return new Nbt().fromFile(file);
			} catch (IOException e) {
				logger.warn("Failed to load config: {}", getMessages(e));
				return null; // use empty config
			}
		}).orElse(new CompoundTag());
		
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
			try {
				CompoundTag config = new CompoundTag();
				ConfigHelper.saveConfigs(config);
				new Nbt().toFile(config, FileManager.config); // exception may throw here
				
				// code below will be executed if config successfully saved to file
				// otherwise an exception will be thrown and jump to the catch branch
				logger.info("Configuration successfully saved to file: {}", FileManager.config.getAbsolutePath());
				break; // break the while loop to stop retries
				
			} catch (Exception e) {
				logger.warn("Failed to save config, tried {}: {}", ++retries /* variable retries updated here */, getMessages(e));
				
				if (retries >= GlobalSettings.saveConfigMaxRetries) { // if exceptions still throw
					logger.error("Failed to save config after {} retries.", retries);
					
					// try restore config
					// this prevents file format corruption
					if (FileManager.configBackup.exists()) {
						logger.info("Backup file found! restoring...");
						
						try {
							FileManager.copyFile(FileManager.configBackup, FileManager.config);
						} catch (Exception ex) {
							logger.error("Failed to restore config from backup: ", ex);
						}
					}
					
					break; // break the while loop
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
			thread.setDaemon(true);
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