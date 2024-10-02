package minecraft.morningmc.mcli.utils

import minecraft.morningmc.mcli.utils.annotations.StaticClass

import java.io.*

/**
 * Utility class for managing file and directory metadata in the MCLI launcher.
 */
@StaticClass
object FileManager {
    /** The root directory for application data.  */
    @JvmField
    val appdata: File = resolveAppData()
    /** The working root directory for MCLI.  */
    @JvmField
    val workingRoot: File = File("data")
    /** The configuration file for MCLI.  */
    @JvmField
    val config: File = File(workingRoot, "config.nbt")
    /** The backup configuration file for MCLI.  */
    @JvmField
    val configBackup: File = File(workingRoot, "config.bak.nbt")

    /**
     * Resolves the root directory for application data.
     *
     * @return The root directory for application data.
     */
    private fun resolveAppData(): File {
        return try {
            when (Platform.current.operatingSystem) {
                Platform.OperatingSystem.WINDOWS -> File(System.getenv("AppData"))
                Platform.OperatingSystem.MACOS -> File(System.getProperty("user.home"), "Library/Application Support")
                Platform.OperatingSystem.LINUX -> File(System.getProperty("user.home"), ".config")
                else -> File(".")
            }
        } catch (e: Exception) {
            File(".")
        }
    }

    /**
     * Retrieves a buffered reader for the specified input stream.
     *
     * @param stream The specified input stream.
     * @return A [BufferedReader] for the specified input stream.
     */
    @JvmStatic
    fun getReader(stream: InputStream): BufferedReader {
        return BufferedReader(InputStreamReader(stream))
    }

    /**
     * Renames a file to a new name.
     *
     * @param file The file to be renamed.
     * @param name The new name for the file.
     * @return `true` if and only if the renaming succeeded, `false` otherwise.
     */
    @JvmStatic
    fun renameFile(file: File, name: String): Boolean {
        return file.renameTo(File(file.parentFile, name))
    }

    /**
     * Copies a file to a new location.
     *
     * @param source      The source file.
     * @param destination The destination file.
     * @throws IOException If an I/O error occurs.
     */
    @JvmStatic
    @Throws(IOException::class)
    fun copyFile(source: File, destination: File) {
        FileInputStream(source).use { `in` ->
            FileOutputStream(destination).use { `out` ->
                `in`.transferTo(`out`)
            }
        }
    }
}