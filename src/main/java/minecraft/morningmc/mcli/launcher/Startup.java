package minecraft.morningmc.mcli.launcher;

import minecraft.morningmc.mcli.launcher.main.Main;
import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.launcher.metadata.LauncherMetadata;
import minecraft.morningmc.mcli.utils.Platform;

import javafx.application.Application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The entry point for launching the Minecraft launcher.
 */
public class Startup {
    private static final Logger LOGGER = LogManager.getLogger();
    
    /**
     * Constructs a new instance of Startup, logging launcher information.
     */
    public Startup() {
        LOGGER.info(LauncherMetadata.LONG_FULL_NAME);
        LOGGER.info("Current platform: " + Platform.CURRENT);
        LOGGER.info("Install root: " + FileMetadata.INSTALL_ROOT);
        LOGGER.info("Working root: " + FileMetadata.WORKING_ROOT);
        
        if (!LauncherMetadata.isStable()) {
            LOGGER.warn("This is a development build. There may be some issues.");
        }
    }
    
    /**
     * The main method for launching the MCL Improved.
     *
     * @param args Command-line arguments.
     */
    public static void main(String... args) {
        LOGGER.info("Starting launcher...");
        
        try {
            new Startup().run(args);
            
        } catch (Exception e) {
            LOGGER.fatal("Launcher crashed: ", e);
            System.exit(-1);
            
        } finally {
            LOGGER.info("Launcher quit.");
        }
    }
    
    /**
     * Runs the launcher by launching the JavaFX application.
     *
     * @param args Command-line arguments.
     */
    private void run(String... args) {
        Application.launch(Main.class, args);
    }
}