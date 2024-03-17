package minecraft.morningmc.mcli.launcher.metadata;

/**
 * This class provides metadata information about the MCL Improved (MCLI).
 *
 * @see FileMetadata
 */
public final class LauncherMetadata {
	// Constants
	/** Represents the name of the launcher. */
	public static final String NAME = "MCL Improved";
	
	/** Represents the long name of the launcher. */
	public static final String LONG_NAME = "Minecraft Launcher Improved";
	
	/** Represents the short name of the launcher. */
	public static final String SHORT_NAME = "MCLI";
	
	/** Represents the version of the launcher. */
	public static final byte[] VERSION_ARRAY = {0, 5, 2, 0};
	
	// Auto-complete
	/** Represents the release channel of the launcher. */
	public static final byte CHANNEL = VERSION_ARRAY[0];
	
	/** Represents the version number of the launcher. */
	public static final byte VERSION = VERSION_ARRAY[1];
	
	/** Represents the branch number of the launcher. */
	public static final byte BRANCH = VERSION_ARRAY[2];
	
	/** Represents the build number of the launcher. */
	public static final byte BUILD = VERSION_ARRAY[3];
	
	/** Represents the release channel name of the launcher (0 for {@code DEV}, 1 for {@code STABLE}). */
	public static final String CHANNEL_NAME = isStable() ? "STABLE" : "DEV";
	
	/** Represents the version string in the format {@code "CHANNEL.VERSION.BRANCH.BUILD"}. */
	public static final String VERSION_STRING = "%s.%s.%s.%s".formatted(CHANNEL, VERSION, BRANCH, BUILD);
	
	/** Represents the full version string including channel information. */
	public static final String FULL_VERSION_STRING = "%s Version %d Branch %d Build %d".formatted(CHANNEL_NAME, VERSION, BRANCH, BUILD);
	
	/** Represents the full name of the launcher including name and version. */
	public static final String FULL_NAME = NAME + " " + VERSION_STRING;
	
	/** Represents the long full name of the launcher including long name and detailed version information. */
	public static final String LONG_FULL_NAME = LONG_NAME + " " + FULL_VERSION_STRING;
	
	/** Represents the short full name of the launcher including short name and version. */
	public static final String SHORT_FULL_NAME = SHORT_NAME + " " + VERSION_STRING;
	
	/**
	 * Checks if the launcher version is stable.
	 *
	 * @return {@code true} if the launcher version is stable, {@code false} otherwise.
	 */
	public static boolean isStable() {
		return CHANNEL != 0; // 0 = DEV, 1 = STABLE
	}
}