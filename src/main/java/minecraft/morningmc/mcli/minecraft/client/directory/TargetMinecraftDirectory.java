package minecraft.morningmc.mcli.minecraft.client.directory;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.primitive.StringTag;

import java.io.File;

public class TargetMinecraftDirectory extends MinecraftDirectory {
	/** NbtLoader for loading and saving {@code TargetMinecraftDirectory} objects from/to NBT data. */
	public static final NbtLoader<TargetMinecraftDirectory, StringTag> LOADER = new NbtLoader<>() {
		
		@Override
		public TargetMinecraftDirectory load(StringTag tag) {
			return new TargetMinecraftDirectory(new File(tag.getValue()));
		}

		@Override
		public StringTag save(TargetMinecraftDirectory object) {
			return new StringTag(object.getRoot().getAbsolutePath());
		}
	};
	
	public static final TargetMinecraftDirectory STANDARD = new TargetMinecraftDirectory(new File(FileMetadata.APPDATA, ".minecraft"));
	public static final File ISOLATE_ROOT = new File(STANDARD.getRoot(), "isolate");
	
	public TargetMinecraftDirectory() {
		super();
	}
	
	public TargetMinecraftDirectory(File root) {
		super(root);
	}
	
	public SourceMinecraftDirectory toSource() {
		return new SourceMinecraftDirectory(root);
	}
	
	public enum Policy {
		STANDARD, SOURCE, ISOLATED, CUSTOM
	}
}
