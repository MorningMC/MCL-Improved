package minecraft.morningmc.mcli.launcher.metadata;

import java.util.*;

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
	public static final Version VERSION = new Version(0, 5, 2, 0);
	
	// Auto-complete
	/** Represents the full name of the launcher including name and version. */
	public static final String FULL_NAME = NAME + " " + VERSION;
	
	/** Represents the long full name of the launcher including long name and detailed version information. */
	public static final String LONG_FULL_NAME = LONG_NAME + " " + VERSION.toFullString();
	
	/** Represents the short full name of the launcher including short name and version. */
	public static final String SHORT_FULL_NAME = SHORT_NAME + " " + VERSION;
	
	/**
	 * Represents the launcher version.
	 *
	 * @param channel The release channel of the launcher.
	 * @param version The version number of the launcher.
	 * @param branch The branch number of the launcher.
	 * @param build The build number of the launcher.
	 */
	public record Version(int channel, int version, int branch, int build) implements Comparable<Version> {
		
		/**
		 * Checks if the version is stable.
		 *
		 * @return {@code true} if the version is stable, {@code false} otherwise.
		 */
		public boolean isStable() {
			return channel != 0; // 0 = DEV, 1 = STABLE
		}
		
		/**
		 * Returns the version string in the format {@code "channel.version.branch.build"}.
		 *
		 * @return The version string.
		 */
		@Override
		public String toString() {
			return "%d.%d.%d.%d".formatted(channel, version, branch, build);
		}
		
		/**
		 * Returns the full version string including channel information.
		 *
		 * @return The full version string.
		 */
		public String toFullString() {
			return "%s Version %d Branch %d Build %d".formatted(isStable() ? "STABLE" : "DEV", version, branch, build);
		}
		
		@Override
		public int compareTo(Version o) {
			return Comparator.comparingInt(Version::version)
					       .thenComparingInt(Version::branch)
					       .thenComparingInt(Version::build)
					       .compare(this, o);
		}
	}
}