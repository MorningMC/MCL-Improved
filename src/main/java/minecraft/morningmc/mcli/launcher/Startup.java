package minecraft.morningmc.mcli.launcher;

import minecraft.morningmc.mcli.launcher.main.Main;
import minecraft.morningmc.mcli.utils.FileManager;
import minecraft.morningmc.mcli.minecraft.java.JavaRuntime;
import minecraft.morningmc.mcli.utils.Platform;

import javafx.application.Application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * The entry point for launching MCL Improved.
 *
 * @author MorningMC
 */
public class Startup {
    private static final Logger logger = LogManager.getLogger();
    
    /** Indicates whether to restart after the application is exited. */
    private static boolean pendingRestart = false;
    
    /**
     * Constructs a new instance of {@link Startup}, logging launcher information.
     */
    public Startup() {
        logger.info(Metadata.longFullName);
        logger.info("Commandline: {}", ProcessHandle.current().info().commandLine().orElse("N/A"));
	    logger.info("System platform: {}", Platform.system);
	    logger.info("Current platform: {}", Platform.current);
        logger.info("Java runtime: {}", JavaRuntime.current.map(JavaRuntime::toString).orElse("N/A"));
	    logger.info("Working root: {}", FileManager.workingRoot.getAbsolutePath());
        
        if (!Metadata.version.isStable()) {
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
        }
        
        logger.info("Launcher quit.");
        if (pendingRestart) {
            System.exit(64); // this will be handled by the wrapper and took a restart
        }
    }
    
    /**
     * Plans a restart after the application is exited.
     */
    public static void planRestart() {
        pendingRestart = true;
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