package minecraft.morningmc.mcli.utils;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;

/**
 * The Platform enum represents different operating systems and provides utility methods
 * for platform-specific operations.
 */
public enum Platform {
    WINDOWS, MACOS, LINUX, UNKNOWN;
    
    /** The current platform of the system. */
    public static final Platform CURRENT = inferPlatform(System.getProperty("os.name"));
    
    /** The file separator for the current platform. */
    public static final String FILE_SEPARATOR = File.separator;
    
    /** The path separator for the current platform. */
    public static final String PATH_SEPARATOR = File.pathSeparator;

    /** The line separator for the current platform. */
    public static final String LINE_SEPARATOR = System.lineSeparator();
    
    /** The default encoding for the current platform. */
    public static final Charset ENCODING = Charset.forName(System.getProperty("sun.jnu.encoding", Charset.defaultCharset().name()));
    
    /**
     * Checks if the current system architecture is 64-bit.
     *
     * @return {@code true} if the system architecture is 64-bit, {@code false} otherwise.
     */
    public static boolean is64bit() {
        String sunArchDataModel = System.getProperty("sun.arch.data.model");
        
        if (sunArchDataModel != null) {
            return "64".equals(sunArchDataModel);
        }
        
        return System.getProperty("os.arch").contains("64");
    }
    
    /**
     * Infers the platform based on the provided OS name.
     *
     * @param osName The name of the operating system.
     * @return The inferred platform.
     */
    public static Platform inferPlatform(String osName) {
        if (osName == null) return UNKNOWN;
        osName = osName.toLowerCase();
        
        if (osName.contains("linux") || osName.contains("unix")) {
            return LINUX;
            
        } else if (osName.contains("osx") || osName.contains("os x") || osName.contains("mac")) {
            return MACOS;
            
        } else if (osName.contains("windows")) {
            return WINDOWS;
            
        } else {
            return UNKNOWN;
        }
    }
}