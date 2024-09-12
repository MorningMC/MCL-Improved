package minecraft.morningmc.mcli.utils

import minecraft.morningmc.mcli.minecraft.java.JavaRuntime

import java.io.File
import java.nio.charset.Charset
import java.util.*

/**
 * Represents a platform-specific configuration.
 *
 * @param operatingSystem The operating system of the platform.
 * @param architecture The architecture of the platform.
 * @param fileSeparator The file separator of the platform.
 * @param pathSeparator The path separator of the platform.
 * @param lineSeparator The line separator of the platform.
 * @param encoding The encoding of the platform.
 */
@JvmRecord
data class Platform(
    @JvmField val operatingSystem: OperatingSystem,
    @JvmField val architecture: Architecture,
    @JvmField val fileSeparator: String,
    @JvmField val pathSeparator: String,
    @JvmField val lineSeparator: String,
    @JvmField val encoding: Charset
) {

    override fun toString(): String {
        return "$operatingSystem $architecture"
    }

    /**
     * Enumerates different operating systems and provides utility methods.
     */
    enum class OperatingSystem {
        WINDOWS, MACOS, LINUX, UNKNOWN;

        companion object {
            /**
             * Infers the operating system based on the provided name.
             *
             * @param name The value of property `os.name`.
             * @return The inferred operating system.
             */
            @JvmStatic
            fun infer(name: String?): OperatingSystem {
                if (name != null) {
                    val lowerName = name.lowercase(Locale.getDefault())

                    return if (lowerName.contains("linux")) {
                        LINUX
                    } else if (lowerName.contains("osx") || lowerName.contains("os x") || lowerName.contains("mac")) {
                        MACOS
                    } else if (lowerName.contains("windows")) {
                        WINDOWS
                    } else {
                        UNKNOWN
                    }
                }

                return UNKNOWN
            }
        }
    }

    /**
     * Enumerates different architectures and provides utility methods.
     */
    enum class Architecture {
        BIT32, BIT64, UNKNOWN;

        /**
         * Returns the number of bits of the architecture.
         *
         * @return The number of bits of the architecture, or 0 if unknown.
         */
        fun bits(): Int {
            return when (this) {
                BIT32 -> 32
                BIT64 -> 64
                else -> 0
            }
        }

        companion object {
            /**
             * Infers the architecture based on the provided name.
             *
             * @param name The value of property `sun.arch.data.model`.
             * @param archName The value of property `os.arch`.
             * @return The inferred architecture.
             */
            @JvmStatic
            fun infer(name: String?, archName: String?): Architecture {
                if (name != null) {
                    return if (name == "64") BIT64 else BIT32
                }

                if (archName != null) {
                    return if (archName.contains("64")) BIT64 else BIT32
                }

                return UNKNOWN
            }
        }
    }

    companion object {
        /** The system platform  */
        @JvmField
        val system: Platform = resolveSystem()

        /** The current Java runtime platform  */
        @JvmField
        val current: Platform = if (JavaRuntime.current != null) JavaRuntime.current.platform else system

        /**
         * Resolves the system platform.
         *
         * @return The system platform.
         */
        private fun resolveSystem(): Platform {
            val os = OperatingSystem.infer(System.getProperty("os.name"))

            val arch: Architecture
            if (os == OperatingSystem.WINDOWS) {
                val processorArch = System.getenv("PROCESSOR_ARCHITECTURE")
                val wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432")

                arch = if (processorArch != null && processorArch.endsWith("64") || wow64Arch != null && wow64Arch.endsWith("64")) Architecture.BIT64 else Architecture.BIT32
            } else {
                arch = if (System.getProperty("os.arch").contains("64")) Architecture.BIT64 else Architecture.BIT32
            }

            return Platform(
                os,
                arch,
                File.separator,
                File.pathSeparator,
                System.lineSeparator(),
                inferEncoding(System.getProperty("sun.jnu.encoding"))
            )
        }

        /**
         * Infers the encoding based on the provided name.
         *
         * @param name The value of property `sun.jnu.encoding`.
         * @return The inferred encoding.
         */
        @JvmStatic
        fun inferEncoding(name: String?): Charset {
            if (name != null) {
                return try {
                    Charset.forName(name)
                } catch (e: Exception) {
                    Charset.defaultCharset()
                }
            }

            return Charset.defaultCharset()
        }
    }
}