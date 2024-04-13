package minecraft.morningmc.mcli.minecraft.client;

import minecraft.morningmc.mcli.minecraft.client.directory.SourceMinecraftDirectory;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;

import dev.dewy.nbt.tags.collection.CompoundTag;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

public record Version(SourceMinecraftDirectory source,
					  String version,
                      Type type,
                      String mainClass) {
	/**
	 * NbtLoader for loading and saving {@code Version} objects from/to NBT data.
	 */
	public static final NbtLoader<Version, CompoundTag> LOADER = new NbtLoader<>() {
		
		@Override
		public Version load(CompoundTag tag) throws IllegalNbtException {
			return null;
		}
		
		@Override
		public CompoundTag save(Version object) {
			return null;
		}
	};
	
	
	public enum Type {
		RELEASE, SNAPSHOT, CUSTOM
	}
}
