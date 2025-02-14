package morningmc.foliage.launcher;

import java.io.File;

public final class LauncherInfo {
    private static final String name = "Foliage Launcher";

    private static final Channel channel = Channel.DEV;
    private static final int[] version = {0, 0, 0, 16};
    private enum Channel { DEV, BETA, RELEASE }

    public static class FileSystem {
        private static final File workDir = new File("%APPDATA%\\.foliage-launcher");
        private static final File logDir = new File(workDir, "logs");
        private static final File tempDir = new File(workDir, "temp");

        public static File getWorkDir() {
            return workDir;
        }

        public static File getLogDir() {
            return logDir;
        }

        public static File getTempDir() {
            return tempDir;
        }
    }

    public static String fullName() {
        return name + " " + channel + versionString();
    }

    public static String versionString() {
        return "%d.%d.%d.%03d".formatted(version[0], version[1], version[2], version[3]);
    }

    public static String getName() {
        return name;
    }

    public static Channel getChannel() {
        return channel;
    }

    public static int[] getVersion() {
        return version;
    }
}
