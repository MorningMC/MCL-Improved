package minecraft.morningmc.mcli.minecraft.client;

import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;

import dev.dewy.nbt.tags.collection.CompoundTag;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

public class Version {
	/** NbtLoader for loading and saving Version objects from/to NBT data. */
	public static final NbtLoader<Version, CompoundTag> LOADER = new NbtLoader<>() {
		
		@Override
		public Version loadFromNbt(CompoundTag tag) throws IllegalNbtException {
			return null;
		}
		
		@Override
		public CompoundTag saveToNbt(Version object) {
			return null;
		}
	};
	
	public enum Policy {
		RELEASE, SNAPSHOT, CUSTOM
	}
}
