package minecraft.morningmc.mcli.launcher;

import minecraft.morningmc.mcli.launcher.main.Main;
import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.launcher.metadata.LauncherMetadata;
import minecraft.morningmc.mcli.utils.Platform;

import javafx.application.Application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The entry point for launching the MCL Improved.
 */
public class Startup {
    private static final Logger LOGGER = LogManager.getLogger();
    
    /**
     * Constructs a new instance of {@code Startup}, logging launcher information.
     */
    public Startup() {
        LOGGER.info(LauncherMetadata.LONG_FULL_NAME);
        LOGGER.info("System platform: " + Platform.SYSTEM);
        LOGGER.info("Current platform: " + Platform.CURRENT);
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
            
        } catch (Throwable t) {
            LOGGER.fatal("Launcher crashed: ", t);
            System.exit(-1);
            
        } finally {
            LOGGER.info("Launcher quit.");
        }
    }
    
    /**
     * Runs the launcher.
     *
     * @param args Command-line arguments.
     */
    private void run(String... args) {
        Application.launch(Main.class, args);
    }
}