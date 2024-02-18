package minecraft.morningmc.mcli.launcher.main;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.launcher.metadata.LauncherMetadata;
import minecraft.morningmc.mcli.minecraft.java.JavaRuntimeCollection;
import minecraft.morningmc.mcli.minecraft.client.profile.ProfileCollection;
import minecraft.morningmc.mcli.minecraft.launch.LaunchOptions;
import minecraft.morningmc.mcli.minecraft.launch.Launcher;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.tags.collection.CompoundTag;

import java.io.IOException;
import java.util.*;
import java.util.HashMap;

/**
 * The Main class represents the main entry point for the Minecraft launcher application.
 * It extends the JavaFX Application class and initializes the application, creates the main stage,
 * and handles the lifecycle of the application.
 */
public class Main extends Application {
	private static final Logger LOGGER = LogManager.getLogger();
	
	private Launcher launcher;
	
	private final Map<String, Scene> scenes = new HashMap<>();
	
	/**
	 * Initializes the application. Completes files, loads configuration.
	 *
	 * @throws Exception If an error occurs during initialization.
	 */
	@Override
	public void init() throws Exception {
		LOGGER.info("Initializing launcher...");
		
		try {
			int created = FileMetadata.completeFiles();
			LOGGER.debug("Completed " + created + " files.");
			
		} catch (IOException e) {
			LOGGER.error("Complete files failed: ", e);
		}
		
		// Load config
		CompoundTag config;
		try {
			config = new Nbt().fromFile(FileMetadata.CONFIG);
		} catch (IOException e) {
			LOGGER.warn("Failed to load config: " + e.getMessage());
			config = new CompoundTag();
		}
		
		try {
			ProfileCollection.LOADER.loadFromNbt(config.getList("profileCollection"));
		} catch (Exception e) {
			LOGGER.warn("Failed to load profileCollection: " + e.getMessage());
			ProfileCollection.init(Set.of());
		}
		
		try {
			JavaRuntimeCollection.LOADER.loadFromNbt(config.getList("javaRuntimeCollection"));
		} catch (Exception e) {
			LOGGER.warn("Failed to load javaRuntimeCollection: " + e.getMessage());
			JavaRuntimeCollection.init(Set.of());
		}
		JavaRuntimeCollection.search();
		
		try {
			launcher = Launcher.LOADER.loadFromNbt(config.getCompound("launcher"));
		} catch (Exception e) {
			LOGGER.warn("Failed to load launcher: " + e.getMessage());
			launcher = new Launcher(LaunchOptions.DEFAULT, null);
		}
		
		// Preparing scenes
		LOGGER.info("Preparing scenes...");
		
		FXMLLoader loader = new FXMLLoader();
		
		Scene launch = new Scene(loader.load(FileMetadata.getResource("fxmls/LaunchScene.fxml")));
		scenes.put("launch", launch);
	}
	
	/**
	 * Starts the application. Sets up the main stage.
	 *
	 * @param mainStage The main stage of the application.
	 * @throws Exception If an error occurs during the startup of the application.
	 */
	@Override
	public void start(Stage mainStage) throws Exception {
		LOGGER.info("Starting launcher lifecycle...");
		
		mainStage.getIcons().add(new Image(FileMetadata.getResource("assets/icon.png")));
		mainStage.setTitle(LauncherMetadata.FULL_NAME);
		
		mainStage.setScene(scenes.get("launch"));
		
		mainStage.show();
	}
	
	/**
	 * Stops the application. Saves the configuration when the application is closed.
	 *
	 * @throws Exception If an error occurs during the shutdown of the application.
	 */
	@Override
	public void stop() throws Exception {
		LOGGER.info("Stopping launcher...");
		
		// Save config
		CompoundTag config = new CompoundTag();
		
		config.put("profileCollection", ProfileCollection.LOADER.saveToNbt(ProfileCollection.instance));
		config.put("javaRuntimeCollection", JavaRuntimeCollection.LOADER.saveToNbt(JavaRuntimeCollection.instance));
		config.put("launcher", Launcher.LOADER.saveToNbt(launcher));
		
		try {
			new Nbt().toFile(config, FileMetadata.CONFIG);
		} catch (IOException e) {
			LOGGER.error("Failed to save config: ", e);
		}
	}
}
