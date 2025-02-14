package morningmc.foliage.launcher.startup;

import morningmc.foliage.launcher.LauncherInfo;
import morningmc.foliage.launcher.main.*;
import morningmc.foliage.util.logging.Logger;

public class Startup {

    public static void main(String[] args) {
        Logger logger = new Logger(Startup.class.getName());

        logger.info("Launcher starting...");
        logger.info("Launcher version: " + LauncherInfo.versionString());
        logger.info("Work Directory: " + LauncherInfo.FileSystem.getWorkDir());

        try {
            Main main = new Main();
            int exitCode = main.run();

            logger.info("Process exit status: " + exitCode);
            System.exit(exitCode);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static Thread startThread(String name, Runnable task, boolean daemon) {
        Thread thread = new Thread(task, name);
        thread.setDaemon(daemon);
        thread.start();
        return thread;
    }
}
