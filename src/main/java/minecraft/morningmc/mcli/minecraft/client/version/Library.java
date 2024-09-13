package minecraft.morningmc.mcli.minecraft.client.version;

import minecraft.morningmc.mcli.launcher.networking.download.DownloadInfo;

import java.util.*;

/**
 * Represents a library in a Minecraft version.
 *
 * @param name The <a href="https://maven.apache.org/guides/mini/guide-naming-conventions.html">Maven name</a> of the library.
 * @param downloadInfo The download information of the library.
 * @param downloadPath The path of the library file relative to the libraries folder (contains the file name).
 */
public record Library(Name name,
                      DownloadInfo downloadInfo,
                      String downloadPath,
                      Set<String> extractExcludes) {
	
	/**
	 * Represents a <a href="https://maven.apache.org/guides/mini/guide-naming-conventions.html">Maven name</a>.
	 *
	 * @param groupID    The group ID of the library.
	 * @param artifactID The artifact ID of the library.
	 * @param version    The version of the library.
	 * @param classifier The classifier of the library (can be {@code null}).
	 */
	public record Name(String groupID, String artifactID, String version, String classifier) {
		
		/**
		 * Parses a <a href="https://maven.apache.org/guides/mini/guide-naming-conventions.html">Maven name</a> from a string.
		 *
		 * @param mavenName The string to be parsed.
		 * @return The parsed {@link Name}.
		 */
		public static Name parse(String mavenName) {
			String[] name = mavenName.split(":");
			return new Name(name[0], name[1], name[2], name.length > 3 ? name[3] : null /* classifier sometimes doesn't exist */);
		}
	}
}