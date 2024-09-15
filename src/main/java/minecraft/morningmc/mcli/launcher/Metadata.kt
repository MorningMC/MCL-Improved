package minecraft.morningmc.mcli.launcher

import minecraft.morningmc.mcli.utils.annotations.StaticClass

/**
 * This class provides metadata information about the MCL Improved (MCLI).
 */
@StaticClass
object Metadata {
    // Constants
    /** Represents the name of the launcher.  */
    const val name: String = "MCL Improved"

    /** Represents the long name of the launcher.  */
    const val longName: String = "Minecraft Launcher Improved"

    /** Represents the short name of the launcher.  */
    const val shortName: String = "MCLI"

    /** Represents the version of the launcher.  */
    @JvmField
    val version: Version = Version(0, 5, 3, 1)

    // Auto-complete
    /** Represents the full name of the launcher including name and version.  */
    @JvmField
    val fullName: String = "$name $version"

    /** Represents the long full name of the launcher including long name and detailed version information.  */
    @JvmField
    val longFullName: String = longName + " " + version.toFullString()

    /** Represents the short full name of the launcher including short name and version.  */
    val shortFullName: String = "$shortName $version"

    /**
     * Represents the launcher version.
     *
     * @param branch  The release branch of the launcher. (`0` for dev or nightly, `1` for stable)
     * @param version The version number of the launcher.
     * @param build   The build number of the launcher.
     * @param patch   The patch number of the launcher.
     */
    @JvmRecord
    data class Version(val branch: Int, val version: Int, val build: Int, val patch: Int) : Comparable<Version> {
        val isStable: Boolean
            /**
             * Checks if the version is stable.
             *
             * @return `true` if the version is stable, `false` otherwise.
             */
            get() = branch != 0 // 0 = DEV, 1 = STABLE

        /**
         * Returns the version string in the format `"channel.version.branch.build"`.
         *
         * @return The version string.
         */
        override fun toString(): String = "$branch.$version.$build.$patch"

        /**
         * Returns the full version string including channel information.
         *
         * @return The full version string.
         */
        fun toFullString(): String = "${if (isStable) "Stable" else "Dev"} Version $version Branch $build Build $patch"

        override fun compareTo(other: Version): Int =
            Comparator.comparingInt(Version::version)
                .thenComparingInt(Version::build)
                .thenComparingInt(Version::patch)
                .compare(this, other)
    }
}