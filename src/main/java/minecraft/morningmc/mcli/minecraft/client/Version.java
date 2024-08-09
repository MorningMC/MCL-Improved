package minecraft.morningmc.mcli.minecraft.client;

import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.primitive.StringTag;

public record Version(String version,
                      Type type,
                      String mainClass) {
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
		RELEASE, SNAPSHOT, CUSTOM
	}
}
