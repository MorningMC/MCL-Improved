package minecraft.morningmc.mcli.minecraft.client;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.primitive.StringTag;

import java.io.File;

/**
 * Represents a directory for Minecraft-related files.
 */
public class MinecraftDirectory {
	/** NbtLoader for loading and saving {@code MinecraftDirectory} objects from/to NBT data. */
	public static final NbtLoader<MinecraftDirectory, StringTag> LOADER = new NbtLoader<>() {
		
		@Override
		public MinecraftDirectory load(StringTag tag) {
			return new MinecraftDirectory(new File(tag.getValue()));
		}

		@Override
		public StringTag save(MinecraftDirectory object) {
			return new StringTag(object.root().getAbsolutePath());
		}
	};
	
	public static final MinecraftDirectory STANDARD = new MinecraftDirectory(new File(FileMetadata.APPDATA, ".minecraft"));
	public static final File ISOLATE_ROOT = new File(STANDARD.root(), "isolate");
	
	/**  The root directory for Minecraft-related files. */
	protected final File root;
	
	/**
	 * Constructs a {@code MinecraftDirectory} with the default root directory ".minecraft".
	 */
	public MinecraftDirectory() {
		this.root = new File(".minecraft");
	}
	
	/**
	 * Constructs a {@code MinecraftDirectory} with a specified root directory.
	 *
	 * @param root The root directory for Minecraft-related files.
	 */
	public MinecraftDirectory(File root) {
		this.root = root;
	}
	
	/**
	 * Gets the root directory for Minecraft-related files.
	 *
	 * @return The root directory.
	 */
	public File root() {
		return this.root;
	}
	
	public enum Policy {
		STANDARD, ISOLATED, CUSTOM
	}
}
