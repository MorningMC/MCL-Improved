package minecraft.morningmc.mcli.minecraft.client.version;

import minecraft.morningmc.mcli.launcher.networking.download.DownloadInfo;
import minecraft.morningmc.mcli.utils.Conditional;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.primitive.StringTag;

import org.apache.logging.log4j.Level;

import java.util.*;

/**
 * Represents a Minecraft version, containing various details about the game, such as libraries, assets, and more.
 *
 * @param version       The version folder name, e.g., "1.19.2".
 * @param javaArguments The arguments to pass to the Java runtime.
 * @param gameArguments The arguments to pass to the game.
 * @param assets        The assets folder name.
 * @param assetIndex    The asset index download information.
 * @param client        The client download information.
 * @param clientMappings The client mappings download information.
 * @param server        The server download information.
 * @param serverMappings The server mappings download information.
 * @param id            The version ID.
 * @param javaVersion   The Java version required for this version.
 * @param libraries     The libraries required for this version.
 * @param logging       The logging configuration.
 * @param mainClass     The main class for the game.
 * @param releaseTime   The release time of this version.
 * @param time          The time of this version.
 * @param type          The type of this version (e.g., release, snapshot).
 */
public record Version(String version,
					  List<Conditional<String>> javaArguments,
					  List<Conditional<String>> gameArguments,
					  String assets,
					  DownloadInfo assetIndex,
					  DownloadInfo client,
					  DownloadInfo clientMappings,
					  DownloadInfo server,
					  DownloadInfo serverMappings,
					  String id,
					  int javaVersion,
					  Set<Conditional<Library>> libraries,
					  Logging logging,
					  String mainClass,
					  Date releaseTime,
					  Date time,
                      Type type) {
	/** {@link NbtLoader} for loading and saving {@link Version} objects from/to NBT data. */
	public static final NbtLoader<Version, StringTag> loader = new NbtLoader<>() {
		
		@Override
		public Version load(StringTag tag) {
			return of(tag.getValue());
		}
		
		@Override
		public StringTag save(Version object) {
			return new StringTag(object.version);
		}
	};
	
	public static Version of(String version) {
		return null;
	}
	
	public enum Type {
		RELEASE, SNAPSHOT, ALPHA, BETA, CUSTOM
	}
	
	public record Logging(String type, DownloadInfo downloads, String argument) {
		
		public Log parse(String log) {
			return null;
		}
		
		public record Log(Date timestamp, Level level, String thread, String message, String raw) {
		}
	}
	
	public static class Manifest {
		// TODO version_manifest.json
	}
}
