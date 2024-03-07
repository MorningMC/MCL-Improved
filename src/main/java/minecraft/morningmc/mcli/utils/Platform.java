package minecraft.morningmc.mcli.utils;

import minecraft.morningmc.mcli.minecraft.java.JavaRuntime;

import java.io.File;
import java.nio.charset.Charset;

/**
 * The {@code Platform} record represents a platform-specific configuration.
 *
 * @param operatingSystem The operating system of the platform.
 * @param architecture The architecture of the platform.
 * @param fileSeparator The file separator of the platform.
 * @param pathSeparator The path separator of the platform.
 * @param lineSeparator The line separator of the platform.
 * @param encoding The encoding of the platform.
 */
public record Platform(OperatingSystem operatingSystem,
                       Architecture architecture,
                       String fileSeparator,
                       String pathSeparator,
                       String lineSeparator,
                       Charset encoding) {
    
    /** The system platform */
    public static final Platform SYSTEM = resolveSystem();
    
    /** The current Java runtime platform */
    public static final Platform CURRENT = JavaRuntime.CURRENT != null ? JavaRuntime.CURRENT.platform() : SYSTEM;
    
    /**
     * Resolves the system platform.
     *
     * @return The system platform.
     */
    private static Platform resolveSystem() {
        OperatingSystem os = OperatingSystem.infer(System.getProperty("os.name"));
        
        Architecture arch;
        if (os == OperatingSystem.WINDOWS) {
            String processorArch = System.getenv("PROCESSOR_ARCHITECTURE");
            String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");
            
            arch = processorArch != null && processorArch.endsWith("64") || wow64Arch != null && wow64Arch.endsWith("64")
                           ? Architecture.BIT64 : Architecture.BIT32;
        } else {
            arch = System.getProperty("os.arch").contains("64")
                           ? Architecture.BIT64 : Architecture.BIT32;
        }
        
        return new Platform(
                os,
                arch,
		        File.separator,
                File.pathSeparator,
                System.lineSeparator(),
                inferEncoding(System.getProperty("sun.jnu.encoding"))
        );
    }
    
    /**
     * Infers the encoding based on the provided name.
     *
     * @param name The value of property {@code sun.jnu.encoding}.
     * @return The inferred encoding.
     */
    public static Charset inferEncoding(String name) {
        if (name != null) {
            try {
                return Charset.forName(name);
            } catch (Exception e) {
                return Charset.defaultCharset();
            }
        }
        
        return Charset.defaultCharset();
    }
    
    @Override
    public String toString() {
        return operatingSystem + " " + architecture;
    }
    
    /**
     * The {@code OperatingSystem} enum represents different operating systems and provides utility methods
     */
    public enum OperatingSystem {
        WINDOWS, MACOS, LINUX, UNKNOWN;
        
        /**
        * Infers the operating system based on the provided name.
        *
        * @param name The value of property {@code os.name}.
        * @return The inferred operating system.
        */
        public static OperatingSystem infer(String name) {
            if (name != null) {
                name = name.toLowerCase();
                
                if (name.contains("linux")) {
                    return LINUX;
                } else if (name.contains("osx") || name.contains("os x") || name.contains("mac")) {
                    return MACOS;
                } else if (name.contains("windows")) {
                    return WINDOWS;
                } else {
                    return UNKNOWN;
                }
            }
            
            return UNKNOWN;
        }
    }
    
    /**
     * The {@code Architecture} enum represents different architectures and provides utility methods.
     */
    public enum Architecture {
        BIT32, BIT64, UNKNOWN;
        
        /**
         * Infers the architecture based on the provided name.
         *
         * @param name The value of property {@code sun.arch.data.model}.
         * @param archName The value of property {@code os.arch}.
         * @return The inferred architecture.
         */
        public static Architecture infer(String name, String archName) {
            if (name != null) {
                return name.equals("64") ? BIT64 : BIT32;
            }
            
            if (archName != null) {
                return archName.contains("64") ? BIT64 : BIT32;
            }
            
            return UNKNOWN;
        }
        
        /**
         * Returns the number of bits of the architecture.
         *
         * @return The number of bits of the architecture, or 0 if unknown.
         */
        public int bits() {
	        return switch (this) {
		        case BIT32 -> 32;
		        case BIT64 -> 64;
		        default -> 0;
	        };
        }
    }
}