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
    
    /**
     * The current platform of the system.
     */
    public static final Platform CURRENT = inferPlatform(System.getProperty("os.name"));
    
    /**
     * Gets the file separator for the current platform.
     *
     * @return The file separator.
     */
    public static String getFileSeparator() {
        return FileSystems.getDefault().getSeparator();
    }
    
    /**
     * Gets the path separator for the current platform.
     *
     * @return The path separator.
     */
    public static String getPathSeparator() {
        return File.pathSeparator;
    }
    
    /**
     * Gets the line separator for the current platform.
     *
     * @return The line separator.
     */
    public static String getLineSeparator() {
        return System.lineSeparator();
    }
    
    /**
     * Gets the default encoding for the current platform.
     *
     * @return The default encoding.
     */
    public static String getEncoding() {
        return System.getProperty("sun.jnu.encoding", Charset.defaultCharset().name());
    }
    
    /**
     * Checks if the current system architecture is 64-bit.
     *
     * @return True if the system architecture is 64-bit, false otherwise.
     */
    public static boolean isX64() {
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