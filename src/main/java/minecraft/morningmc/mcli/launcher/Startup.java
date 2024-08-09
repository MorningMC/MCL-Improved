package minecraft.morningmc.mcli.launcher;

import minecraft.morningmc.mcli.launcher.main.Main;
import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.launcher.metadata.LauncherMetadata;
import minecraft.morningmc.mcli.utils.Platform;

import javafx.application.Application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The entry point for launching MCL Improved.
 *
 * @author MorningMC
 */
public class Startup {
    private static final Logger logger = LogManager.getLogger();
    
    /**
     * Constructs a new instance of {@link Startup}, logging launcher information.
     */
    public Startup() {
        logger.info(LauncherMetadata.longFullName);
	    logger.info("System platform: {}", Platform.system);
	    logger.info("Current platform: {}", Platform.current);
	    logger.info("Working root: {}", FileMetadata.workingRoot);
        
        if (!LauncherMetadata.version.isStable()) {
            logger.warn("This is a development build. There may be some issues.");
        }
    }
    
    /**
     * The main method for launching the MCL Improved.
     *
     * @param args Command-line arguments.
     */
    public static void main(String... args) {
        logger.info("Starting launcher...");
        
        try {
            new Startup().run(args);
            
        } catch (Throwable t) {
            logger.fatal("Launcher crashed: ", t);
            System.exit(-1);
            
        } finally {
            logger.info("Launcher quit.");
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