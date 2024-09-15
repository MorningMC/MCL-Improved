package minecraft.morningmc.mcli.launcher.main;

import minecraft.morningmc.mcli.minecraft.java.JavaRuntime;
import minecraft.morningmc.mcli.ui.UIManager;
import minecraft.morningmc.mcli.utils.FileManager;

import javafx.application.Application;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;

import java.io.*;

/**
 * The Main class represents the main entry point for the Minecraft launcher application.
 * It extends the JavaFX Application class and initializes the application, creates the main stage,
 * and handles the lifecycle of the application.
 *
 * @author MorningMC
 */
public class Main extends Application {
	private static final Logger logger = LogManager.getLogger();
	
	/**
	 * Initializes the application. Completes files, loads configuration.
	 */
	@Override
	public void init() {
		logger.info("Initializing launcher...");
		
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
		ConfigHelper.startAutoSave(); // start auto-save thread
	}
	
	/**
	 * Starts the application. Sets up the main stage.
	 *
	 * @param mainStage The main stage of the application.
	 */
	@Override
	public void start(Stage mainStage) {
		// stub
		
		try {
			logger.info("Starting launcher lifecycle...");
			
			UIManager manager = new UIManager(mainStage);
			
			mainStage.show();
		} catch (Throwable t) {
			logger.fatal("Failed in launcher lifecycle: ", t);
		}
	}
	
	/**
	 * Stops the application. Saves the configuration when the application is closed.
	 */
	@Override
	public void stop() {
		logger.info("Stopping launcher...");
		
		// wait other threads to finish
		while (JavaRuntime.Collection.isSearching()) {
			Thread.onSpinWait();
		}
		
		ConfigHelper.stopAutoSave(); // stop auto-save thread
		ConfigHelper.saveAll(); // save config
	}
}