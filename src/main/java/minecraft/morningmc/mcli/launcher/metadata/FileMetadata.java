package minecraft.morningmc.mcli.launcher.metadata;

import minecraft.morningmc.mcli.launcher.Startup;

import java.io.File;
import java.io.IOException;

public class FileMetadata {
	public static final File INSTALL_ROOT = new File(Startup.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	public static final File WORKING_ROOT = new File(System.getenv("APPDATA"), ".mcli");

	public static final File CACHE_ROOT = new File(WORKING_ROOT, "cache");

	public static final File CONFIG = new File(WORKING_ROOT, "config.nbt");
	
	public static void completeFiles() throws IOException {
		File[] directories = {
				WORKING_ROOT,
				CACHE_ROOT
		};
		File[] files = {
				CONFIG
		};
		
		for (File file : directories) {
			if (!file.exists()) {
				file.mkdirs();
			}
		}
		for (File file : files) {
			if (!file.exists()) {
				file.createNewFile();
			}
		}
	}
}
