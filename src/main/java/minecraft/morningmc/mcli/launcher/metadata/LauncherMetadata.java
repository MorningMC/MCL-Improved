package minecraft.morningmc.mcli.launcher.metadata;

/**
 * This class provides metadata information about the MCL Improved (MCLI).
 */
public final class LauncherMetadata {
	// Constants
	public static final String NAME = "MCL Improved";
	public static final String LONG_NAME = "Minecraft Launcher Improved";
	public static final String SHORT_NAME = "MCLI";
	
	public static final byte[] VERSION_ARRAY = {0, 5, 2, 0};
	
	// Auto-complete
	public static final byte CHANNEL = VERSION_ARRAY[0];
	public static final byte VERSION = VERSION_ARRAY[1];
	public static final byte BRANCH = VERSION_ARRAY[2];
	public static final byte BUILD = VERSION_ARRAY[3];
	
	/** Represents the release channel of the launcher (0 for DEV, 1 for STABLE). */
	public static final String CHANNEL_STRING = isStable() ? "STABLE" : "DEV";
	
	/** Represents the version string in the format "CHANNEL.VERSION.BRANCH.BUILD". */
	public static final String VERSION_STRING = "%s.%s.%s.%s".formatted(CHANNEL, VERSION, BRANCH, BUILD);
	
	/** Represents the full version string including channel information. */
	public static final String FULL_VERSION_STRING = "%s Version %d Branch %d Build %d".formatted(CHANNEL_STRING, VERSION, BRANCH, BUILD);
	
	/** Represents the full name of the launcher including name and version. */
	public static final String FULL_NAME = NAME + " " + VERSION_STRING;
	
	/** Represents the short full name of the launcher including short name and version. */
	public static final String SHORT_FULL_NAME = SHORT_NAME + " " + VERSION_STRING;
	
	/** Represents the long full name of the launcher including long name and detailed version information. */
	public static final String LONG_FULL_NAME = LONG_NAME + " " + FULL_VERSION_STRING;
	
	/**
	 * Checks if the launcher version is stable.
	 * @return True if the launcher version is stable, false otherwise.
	 */
	public static boolean isStable() {
		return CHANNEL != 0; // 0 = DEV, 1 = STABLE
	}
}