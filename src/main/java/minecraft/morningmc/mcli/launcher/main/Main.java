package minecraft.morningmc.mcli.launcher.main;

import minecraft.morningmc.mcli.launcher.GlobalSettings;
import minecraft.morningmc.mcli.launcher.Translation;
import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.minecraft.java.JavaRuntimeCollection;
import minecraft.morningmc.mcli.minecraft.client.profile.ProfileCollection;
import minecraft.morningmc.mcli.minecraft.launch.LaunchOptions;
import minecraft.morningmc.mcli.minecraft.launch.Launcher;

import javafx.application.Application;
import javafx.stage.Stage;

import minecraft.morningmc.mcli.ui.UIManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;

import java.io.IOException;
import java.util.*;

/**
 * The Main class represents the main entry point for the Minecraft launcher application.
 * It extends the JavaFX Application class and initializes the application, creates the main stage,
 * and handles the lifecycle of the application.
 */
public class Main extends Application {
	private static final Logger logger = LogManager.getLogger();
	
	private Launcher launcher;
	private UIManager manager;
	
	/**
	 * Initializes the application. Completes files, loads configuration.
	 *
	 */
	@Override
	public void init() {
		logger.info("Initializing launcher...");
		
		try {
			int created = FileMetadata.completeFiles();
			logger.debug("Completed {} files.", created);
			
		} catch (IOException e) {
			logger.error("Complete files failed: ", e);
		}
		
		// Load config
		CompoundTag config;
		try {
			config = new Nbt().fromFile(FileMetadata.config);
		} catch (IOException e) {
			logger.warn("Failed to load config: {}", e.getMessage());
			config = new CompoundTag();
		}
		
		try {
			GlobalSettings.loader.load(config.getCompound("globalSettings"));
		} catch (Exception e) {
			logger.warn("Failed to load globalSettings: {}", e.getMessage());
			GlobalSettings.initDefault();
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
			launcher = Launcher.loader.load(config.getCompound("launcher"));
		} catch (Exception e) {
			logger.warn("Failed to load launcher: {}", e.getMessage());
			launcher = new Launcher(LaunchOptions.DEFAULT, null, null);
		}
		
		try {
			Translation.loader.load(config.getString("translation"));
		} catch (Exception e) {
			logger.warn("Failed to load translation: {}", e.getMessage());
			Translation.init("en");
		}
	}
	
	/**
	 * Starts the application. Sets up the main stage.
	 *
	 * @param mainStage The main stage of the application.
	 */
	@Override
	public void start(Stage mainStage) {
		logger.info("Starting launcher lifecycle...");
		
		manager = new UIManager(mainStage);
		
		mainStage.show();
	}
	
	/**
	 * Stops the application. Saves the configuration when the application is closed.
	 */
	@Override
	public void stop() {
		logger.info("Stopping launcher...");
		
		// Save config
		CompoundTag config = new CompoundTag();
		
		config.put("globalSettings", GlobalSettings.loader.save(new GlobalSettings()));
		config.put("profileCollection", ProfileCollection.loader.save(ProfileCollection.instance));
		config.put("javaRuntimeCollection", JavaRuntimeCollection.loader.save(JavaRuntimeCollection.instance));
		config.put("launcher", Launcher.loader.save(launcher));
		config.put("translation", Translation.loader.save(Translation.instance));
		
		try {
			new Nbt().toFile(config, FileMetadata.config);
		} catch (Exception e) {
			logger.error("Failed to save config: ", e);
		}
	}
}