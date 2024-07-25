package minecraft.morningmc.mcli.launcher.metadata;

import minecraft.morningmc.mcli.utils.annotations.StaticClass;

import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * This class provides metadata information about the MCL Improved (MCLI).
 */
@StaticClass
public class LauncherMetadata {
	// Constants
	/** Represents the name of the launcher. */
	public static final String name = "MCL Improved";
	
	/** Represents the long name of the launcher. */
	public static final String longName = "Minecraft Launcher Improved";
	
	/** Represents the short name of the launcher. */
	public static final String shortName = "MCLI";
	
	/** Represents the version of the launcher. */
	public static final Version version = new Version(0, 5, 2, 0);
	
	// Auto-complete
	/** Represents the full name of the launcher including name and version. */
	public static final String fullName = name + " " + version;
	
	/** Represents the long full name of the launcher including long name and detailed version information. */
	public static final String longFullName = longName + " " + version.toFullString();
	
	/** Represents the short full name of the launcher including short name and version. */
	public static final String shortFullName = shortName + " " + version;
	
	/**
	 * Represents the launcher version.
	 *
	 * @param channel The release channel of the launcher.
	 * @param version The version number of the launcher.
	 * @param branch  The branch number of the launcher.
	 * @param build   The build number of the launcher.
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
		public int compareTo(@NotNull Version o) {
			return Comparator.comparingInt(Version::version)
					       .thenComparingInt(Version::branch)
					       .thenComparingInt(Version::build)
					       .compare(this, o);
		}
	}
}