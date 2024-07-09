package minecraft.morningmc.mcli.minecraft.client.resources;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.minecraft.client.resources.marker.Marker;
import minecraft.morningmc.mcli.utils.containers.Modifiable;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * Represents a Minecraft mod.
 *
 * @param name        The name of the mod.
 * @param description The description of the mod.
 * @param modid       The modid of the mod.
 * @param version     The version of the mod.
 * @param loader      The mod loader of the mod.
 * @param authors     The authors of the mod.
 * @param contact     The contact information of the mod.
 * @param marker      The marker of the mod.
 * @param file        The file of the mod.
 * @param enabled     Whether the mod is enabled or not.
 */
public record Mod(String name,
                  String description,
                  String modid,
                  String version,
                  Loader loader,
                  List<String> authors,
                  Map<String, URL> contact,
                  String icon,
                  Marker marker,
                  File file,
                  Modifiable<Boolean> enabled) {
	
	/**
	 * Creates a new {@link Mod} instance from a file.
	 *
	 * @param file The mod file.
	 * @return The created {@link Mod} instance.
	 */
	public static Mod of(File file) {
		return of(file, null);
	}
	
	/**
	 * Creates a new {@link Mod} instance from a file and a marker.
	 *
	 * @param file The mod file.
	 * @param marker The marker.
	 * @return The created {@link Mod} instance.
	 */
	public static Mod of(File file, Marker marker) {
		
		// Infer the mod loader of the mod
		
		
		// Check if the mod is enabled
		Modifiable<Boolean> enabled = Modifiable.of(!isDisabled(file));
		enabled.observers.add(e -> {
			if (e) {
				if (isDisabled(file)) {
					FileMetadata.renameFile(file, file.getName().substring(0, file.getName().lastIndexOf('.')));
				}
			} else {
				if (!isDisabled(file)) {
					FileMetadata.renameFile(file, file.getName() + ".disabled");
				}
			}
		});
		
		return new Mod("", "", "", "", Loader.UNKNOWN, List.of(), Map.of(), "", marker, file, enabled);
	}
	
	/**
	 * Checks if a mod file is disabled.
	 *
	 * @param file The mod file to be checked.
	 * @return {@code true} if the mod file is disabled, {@code false} otherwise.
	 */
	private static boolean isDisabled(File file) {
		return file.getName().endsWith(".disabled") || file.getName().endsWith(".disable");
	}
	
	/**
	 * Enumerates the different mod loaders.
	 */
	public enum Loader {
		FORGE, NEOFORGE, FABRIC, QUILT, LITELOADER, RIFT, UNKNOWN
	}
}