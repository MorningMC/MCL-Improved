package minecraft.morningmc.mcli.minecraft.client.directory;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;

import java.io.File;

public class SourceMinecraftDirectory extends MinecraftDirectory {
	public static final SourceMinecraftDirectory STANDARD = new SourceMinecraftDirectory(new File(FileMetadata.APPDATA, ".minecraft"));
	
	
	public SourceMinecraftDirectory() {
		super();
	}
	
	public SourceMinecraftDirectory(File root) {
		super(root);
	}
}
