package minecraft.morningmc.mcli.minecraft.client.version;

import minecraft.morningmc.mcli.launcher.networking.download.DownloadInfo;
import minecraft.morningmc.mcli.utils.Conditional;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.primitive.StringTag;

import java.util.*;

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
	}
	
	public static class Manifest {
		// TODO version_manifest.json
	}
}
