package morningmc.foliage.util;

import java.nio.charset.Charset;

public enum Platform {
    WINDOWS, LINUX, OSX, UNKNOWN;

    public static final Platform CURRENT = inferPlatform(System.getProperty("os.name"));

    public static String getFileSeparator() {
        return System.getProperty("file.separator");
    }

    public static String getPathSeparator() {
        return System.getProperty("path.separator");
    }

    public static String getLineSeparator() {
        return System.lineSeparator();
    }

    public static String getEncoding() {
        return System.getProperty("sun.jnu.encoding", Charset.defaultCharset().name());
    }

    public static boolean isX64() {
        String sunArchDataModel = System.getProperty("sun.arch.data.model");
        if (sunArchDataModel != null) {
            return "64".equals(sunArchDataModel);
        }
        return System.getProperty("os.arch").contains("64");
    }

    public static Platform inferPlatform(String osName) {
        if (osName == null) return UNKNOWN;
        osName = osName.toLowerCase();

        if (osName.contains("linux") || osName.contains("unix")) {
            return LINUX;
        } else if (osName.contains("osx") || osName.contains("os x") || osName.contains("mac")) {
            return OSX;
        } else if (osName.contains("windows")) {
            return WINDOWS;
        } else {
            return UNKNOWN;
        }
    }
}
