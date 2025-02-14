package minecraft.morningmc.mcli.launcher;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.launcher.metadata.LauncherMetadata;
import minecraft.morningmc.mcli.launcher.main.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Startup {
    private static final Logger LOGGER = LoggerFactory.getLogger(Startup.class);

    public Startup() {
        LOGGER.info(LauncherMetadata.LONG_FULL_NAME);

        LOGGER.info("Install root: " + FileMetadata.INSTALL_ROOT.getAbsolutePath());
        LOGGER.info("Working root: " + FileMetadata.WORKING_ROOT.getAbsolutePath());

        if (!LauncherMetadata.isStable()) {
            LOGGER.warn("This is a development build. There might be some issues.");
        }
    }

    public static void main(String[] args) {
        LOGGER.info("Launcher starting...");

        try {
            new Startup().exec(args);

        } catch (Exception e) {
            LOGGER.error("Launcher crashed: ", e);
            System.exit(-1);

        } finally {
            LOGGER.info("Launcher quit.");
        }
    }

    public void exec(String[] args) throws Exception {
        Main.launch(args);
    }
}
