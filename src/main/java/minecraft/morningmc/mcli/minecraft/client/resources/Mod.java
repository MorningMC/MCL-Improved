package minecraft.morningmc.mcli.minecraft.client.resources;

import minecraft.morningmc.mcli.minecraft.client.resources.marker.Marker;
import minecraft.morningmc.mcli.utils.containers.Modifiable;

import java.io.File;

public record Mod(String name, String modid, String version, Loader loader,
                  Marker marker, File file, Modifiable<Boolean> enabled) {
	
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
		
		// Check if the mod is disabled
		Modifiable<Boolean> enabled = Modifiable.of(!isDisabled(file));
		enabled.observers.add(e -> {
			if (e) {
				if (isDisabled(file)) {
					file.renameTo(new File(file.getParentFile(), file.getName().substring(0, file.getName().lastIndexOf('.'))));
				}
			} else {
				if (!isDisabled(file)) {
					file.renameTo(new File(file.getParentFile(), file.getName() + ".disabled"));
				}
			}
		});
		
		return new Mod("", "", "", Loader.CUSTOM, marker, file, enabled);
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
		FORGE, NEOFORGE, FABRIC, QUILT, RIFT, CUSTOM
	}
}