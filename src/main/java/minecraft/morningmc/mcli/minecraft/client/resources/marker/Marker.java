package minecraft.morningmc.mcli.minecraft.client.resources.marker;

public interface Marker {
	
	/**
	 * Gets the name of download source.
	 *
	 * @return The name of download source.
	 */
	String name();
	
	/**
	 * Gets the project ID of the marked resource.
	 *
	 * @return The project ID of the marked resource.
	 */
	String projectID();
	
	/**
	 * Gets the file ID of the marked resource.
	 *
	 * @return The file ID of the marked resource.
	 */
	String fileID();
}