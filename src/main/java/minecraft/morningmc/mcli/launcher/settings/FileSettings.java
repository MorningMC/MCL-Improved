package minecraft.morningmc.mcli.launcher.settings;

import minecraft.morningmc.mcli.utils.FileManager;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

import java.io.File;

/**
 * Represents the file settings.
 */
public class FileSettings extends Settings {
	/** {@link NbtLoader} for loading and saving {@link FileSettings} objects from/to NBT data. */
	public static final NbtLoader<Void, CompoundTag> loader = new NbtLoader<>() {
		
		@Override
		public Void load(CompoundTag tag) {
			standardRoot = new File(tag.getString("standard_root").getValue());
			versionsRoot = new File(tag.getString("versions_root").getValue());
			isolateRoot = new File(tag.getString("isolate_root").getValue());
			downloadTempRoot = new File(tag.getString("download_temp_root").getValue());
			
			return null;
		}
		
		@Override
		public CompoundTag save(Void value) {
			CompoundTag tag = new CompoundTag();
			
			tag.putString("standard_root", standardRoot.getAbsolutePath());
			tag.putString("versions_root", versionsRoot.getAbsolutePath());
			tag.putString("isolate_root", isolateRoot.getAbsolutePath());
			tag.putString("download_temp_root", downloadTempRoot.getAbsolutePath());
			
			return tag;
		}
	};
	
	// Minecraft
	/** The root directory for the standard Minecraft directory. */
	public static File standardRoot;
	
	/** The root directory for the version files. */
	public static File versionsRoot;
	
	/** The root directory for the isolated Minecraft directories. */
	public static File isolateRoot;
	
	// Launcher
	/** The root directory for the download temporary files. */
	public static File downloadTempRoot;
	
	/**
	 * Initializes the {@link FileSettings}.
	 */
	public static void init() {
	}
	
	/**
	 * Initializes the {@link FileSettings} in default.
	 */
	public static void initDefault() {
		standardRoot = new File(FileManager.appdata, ".minecraft");
		versionsRoot = new File(FileManager.appdata, ".minecraft/versions");
		isolateRoot = new File(FileManager.appdata, ".minecraft/isolate");
		downloadTempRoot = new File(FileManager.appdata, ".mcli/download_temp");
		
		init();
	}
}
