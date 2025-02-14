package minecraft.morningmc.mcli.launcher.metadata;

public final class LauncherMetadata {
	public static final String NAME = "MCL Improved";
	public static final String LONG_NAME = "Minecraft Launcher Improved";
	public static final String SHORT_NAME = "MCLI";

	public static final int[] VERSION_ARRAY = {0, 5, 1, 1};
	
	// Auto-complete
	public static final int CHANNEL = VERSION_ARRAY[0];
	public static final int VERSION = VERSION_ARRAY[1];
	public static final int BRANCH = VERSION_ARRAY[2];
	public static final int BUILD = VERSION_ARRAY[3];
	
	public static final String CHANNEL_NAME = isStable() ? "STABLE" : "DEV";
	
	public static final String VERSION_STRING = "%s.%s.%s.%s".formatted(CHANNEL, VERSION, BRANCH, BUILD);
	public static final String FULL_VERSION_STRING = "%s Version %d Branch %d Build %d".formatted(CHANNEL_NAME, VERSION, BRANCH, BUILD);
	
	public static final String FULL_NAME = NAME + " " + VERSION_STRING;
	public static final String SHORT_FULL_NAME = SHORT_NAME + " " + VERSION_STRING;
	public static final String LONG_FULL_NAME = LONG_NAME + " " + FULL_VERSION_STRING;
	
	public static boolean isStable() {
		return CHANNEL != 0; // 0 = DEV, 1 = STABLE
	}
}
