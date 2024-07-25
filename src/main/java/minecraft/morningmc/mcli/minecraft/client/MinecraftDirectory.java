package minecraft.morningmc.mcli.minecraft.client;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.primitive.StringTag;

import java.io.File;

/**
 * Represents a directory for Minecraft-related files.
 */
public class MinecraftDirectory {
	/** {@link NbtLoader} for loading and saving {@link MinecraftDirectory} objects from/to NBT data. */
	public static final NbtLoader<MinecraftDirectory, StringTag> loader = new NbtLoader<>() {
		
		@Override
		public MinecraftDirectory load(StringTag tag) {
			return new MinecraftDirectory(new File(tag.getValue()));
		}

		@Override
		public StringTag save(MinecraftDirectory object) {
			return new StringTag(object.root.getAbsolutePath());
		}
	};
	
	public static final MinecraftDirectory standard = new MinecraftDirectory(new File(FileMetadata.appdata, ".minecraft"));
	public static final File isolateRoot = new File(standard.root, "isolate");
	
	/**  The root directory for Minecraft-related files. */
	public final File root;
	
	/**
	 * Constructs a {@link MinecraftDirectory} with the default root directory ".minecraft".
	 */
	public MinecraftDirectory() {
		this.root = new File(".minecraft");
	}
	
	/**
	 * Constructs a {@link MinecraftDirectory} with a specified root directory.
	 *
	 * @param root The root directory for Minecraft-related files.
	 */
	public MinecraftDirectory(File root) {
		this.root = root;
	}
	
	/**
	 * Enumerates the directory structure for the specified policy.
	 */
	public enum Policy {
		STANDARD, ISOLATED, CUSTOM
	}
}
