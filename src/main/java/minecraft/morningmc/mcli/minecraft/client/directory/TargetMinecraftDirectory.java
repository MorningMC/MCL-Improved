package minecraft.morningmc.mcli.minecraft.client.directory;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;

import java.io.File;

public class TargetMinecraftDirectory extends MinecraftDirectory {
	public static final TargetMinecraftDirectory STANDARD = new TargetMinecraftDirectory(new File(FileMetadata.APPDATA, ".minecraft"));
	public static final File ISOLATE_ROOT = new File(STANDARD.getRoot(), "isolate");
	
	public TargetMinecraftDirectory() {
		super();
	}
	
	public TargetMinecraftDirectory(File root) {
		super(root);
	}
	
	public SourceMinecraftDirectory toSource() {
		return new SourceMinecraftDirectory(this.getRoot());
	}
	
	public enum Policy {
		STANDARD, SOURCE, ISOLATED, CUSTOM
	}
}
