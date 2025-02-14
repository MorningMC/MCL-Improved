package morningmc.foliage.launcher;

public class LauncherInfo {
    public enum Channel {
        DEV, PRE, RELEASE
    }

    public static String name = "Foliage Launcher";
    public static String version = "0.0.0.000";
    public static Channel channel = Channel.DEV;

    public static String fullName() {
        return name + " " + channel + version;
    }
}
