package morningmc.foliage.minecraft.launch;

import morningmc.foliage.minecraft.launch.options.LaunchOptions;
import morningmc.foliage.util.exceptions.LaunchException;
import morningmc.foliage.util.logging.Logger;

import java.io.IOException;
import java.util.Objects;

import static morningmc.foliage.launcher.startup.Startup.startThread;

public class Launch {
    private Logger logger = new Logger(this.getClass().getName());

    public Process launch(LaunchOptions options) throws LaunchException {
        return launch(generateLaunchArgs(options));
    }

    private Process launch(LaunchArgument arg) throws LaunchException {
        String[] commandLine = arg.generateCommandLine();

        logger.debug(commandLine.toString());

        ProcessBuilder processBuilder = new ProcessBuilder(commandLine);
        processBuilder.directory(arg.getOptions().profile().gameDir());

        Process minecraftProcess;

        try {
            minecraftProcess = processBuilder.start();
        } catch (IOException | SecurityException e) {
            throw new LaunchException("Launch failed: could not start Minecraft process", e);
        }

        // listener

        return minecraftProcess;
    }

    public LaunchArgument generateLaunchArgs(LaunchOptions options) {
        Objects.requireNonNull(options);

        LaunchArgument arg = new LaunchArgument(options);

        return arg;
    }
}
