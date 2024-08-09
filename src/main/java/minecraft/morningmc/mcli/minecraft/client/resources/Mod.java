package minecraft.morningmc.mcli.minecraft.client.resources;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.minecraft.client.resources.marker.Marker;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * Represents a Minecraft mod.
 */
public class Mod {
	public final Info info;
	public final Loader loader;
	public final File file;
	public Marker marker;
	
	/**
	 * Constructs a new {@link Mod} instance from a file.
	 *
	 * @param file The mod file.
	 */
	public Mod(File file) {
		this(file, null);
	}
	
	/**
	 * Constructs a new {@link Mod} instance from a file and a marker.
	 *
	 * @param file The mod file.
	 * @param marker The marker.
	 */
	public Mod(File file, Marker marker) {
		// infer the mod loader
		// TODO Implement mod loader inference logic
		loader = Loader.UNKNOWN;
		
		// parse the mod info
		// TODO Implement mod loading logic
		switch(loader) {
			default: info = null;
		}
		
		this.file = file;
		this.marker = marker;
	}
	
	/**
	 * Checks if a mod file is enabled.
	 *
	 * @return {@code true} if the mod file is enabled, {@code false} otherwise.
	 */
	public boolean isEnabled() {
		return !file.getName().endsWith(".disabled") && !file.getName().endsWith(".disable");
	}
	
	/**
	 * Enables the mod file.
	 * If the mod file is already enabled, this method does nothing.
	 */
	public void enable() {
		if (!isEnabled()) {
			FileMetadata.renameFile(file, file.getName().substring(0, file.getName().lastIndexOf('.')));
		}
	}
	
	/**
	 * Disables the mod file.
	 * If the mod file is already disabled, this method does nothing.
	 */
	public void disable() {
		if (isEnabled()) {
			FileMetadata.renameFile(file, file.getName() + ".disabled");
		}
	}
	
	/**
	 * Represents the information of a mod.
	 *
	 * @param schemaVersion   The schema version of the mod.
	 * @param modid           The mod ID.
	 * @param version         The version of the mod.
	 * @param name            The name of the mod.
	 * @param authors         The authors of the mod.
	 * @param contact         The contact information of the mod.
	 * @param license         The license of the mod.
	 * @param environment     The environment of the mod.
	 * @param entrypoints     The entrypoints of the mod.
	 * @param dependencies    The dependencies of the mod.
	 * @param recommendations The recommendations of the mod.
	 * @param accessWidener   The access widener of the mod.
	 * @param mixins          The mixins of the mod.
	 * @param jars            The jars of the mod.
	 */
	public record Info(int schemaVersion,
	                   String modid,
	                   String version,
	                   String name,
	                   String description,
	                   String icon,
	                   List<String> authors,
	                   Map<String, URL> contact,
	                   String license,
	                   Environment environment,
	                   Map<String, List<String>> entrypoints,
	                   Map<String, String> dependencies,
	                   Map<String, String> recommendations,
	                   String accessWidener,
	                   List<String> mixins,
	                   List<Map<String, String>> jars) {
		
		/**
		 * Enumerates the different mod environments.
		 */
		public enum Environment {
			CLIENT, SERVER, BOTH
		}
	}
	
	/**
	 * Enumerates the different mod loaders.
	 */
	public enum Loader {
		FORGE, NEOFORGE, FABRIC, QUILT, LITELOADER, RIFT, UNKNOWN
	}
}