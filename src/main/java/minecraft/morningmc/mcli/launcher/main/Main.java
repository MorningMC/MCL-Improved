package minecraft.morningmc.mcli.launcher.main;

import minecraft.morningmc.mcli.minecraft.java.JavaRuntimeCollection;
import minecraft.morningmc.mcli.minecraft.launch.LaunchOptions;
import minecraft.morningmc.mcli.minecraft.launch.Launcher;
import minecraft.morningmc.mcli.ui.UIManager;
import minecraft.morningmc.mcli.utils.FileManager;

import javafx.application.Application;
import javafx.stage.Stage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;

import java.io.IOException;

/**
 * The Main class represents the main entry point for the Minecraft launcher application.
 * It extends the JavaFX Application class and initializes the application, creates the main stage,
 * and handles the lifecycle of the application.
 *
 * @author MorningMC
 */
public class Main extends Application {
	private static final Logger logger = LogManager.getLogger();
	
	public static Main instance = null;
	
	public Launcher launcher;
	private UIManager manager;
	
	/**
	 * Initializes the application. Completes files, loads configuration.
	 */
	@Override
	public void init() {
		instance = this;
		
		logger.info("Initializing launcher...");
		
		// complete files
		try {
			int created = FileManager.completeFiles();
			logger.debug("Completed {} files.", created);
			
		} catch (IOException e) {
			logger.error("Complete files failed: ", e);
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
		
		try {
			launcher = Launcher.loader.load(config.getCompound("launcher"));
		} catch (Exception e) {
			logger.warn("Failed to load launcher: {}", e.getMessage());
			launcher = new Launcher(LaunchOptions.DEFAULT, null, null);
		}
		
		// start auto-save thread
		ConfigHelper.startAutoSave();
	}
	
	/**
	 * Starts the application. Sets up the main stage.
	 *
	 * @param mainStage The main stage of the application.
	 */
	@Override
	public void start(Stage mainStage) {
		try {
			logger.info("Starting launcher lifecycle...");
			
			manager = new UIManager(mainStage);
			
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
		while (JavaRuntimeCollection.isSearching()) {
			Thread.onSpinWait();
		}
		
		// stop auto-save thread
		ConfigHelper.stopAutoSave();
		
		// save config
		ConfigHelper.saveAll();
	}
}