package minecraft.morningmc.mcli.minecraft.client.directory;

import java.io.File;

/**
 * The MinecraftDirectory class represents the base directory for Minecraft-related files.
 * It provides access to the root directory where Minecraft-related files are stored.
 */
public abstract class MinecraftDirectory {
	
	/**
	 * The root directory for Minecraft-related files.
	 */
	private final File root;
	
	/**
	 * Constructs a MinecraftDirectory with the default root directory ".minecraft".
	 */
	MinecraftDirectory() {
		this.root = new File(".minecraft");
	}
	
	/**
	 * Constructs a MinecraftDirectory with a specified root directory.
	 *
	 * @param root The root directory for Minecraft-related files.
	 */
	MinecraftDirectory(File root) {
		this.root = root;
	}
	
	/**
	 * Gets the root directory for Minecraft-related files.
	 *
	 * @return The root directory.
	 */
	public File getRoot() {
		return this.root;
	}
}