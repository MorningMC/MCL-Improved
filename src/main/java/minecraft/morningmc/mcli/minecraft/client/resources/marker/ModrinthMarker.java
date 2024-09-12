package minecraft.morningmc.mcli.minecraft.client.resources.marker;

/**
 * Represents a marker for Modrinth resources.
 *
 * @param projectID The project ID of the marked resource.
 * @param fileID    The file ID of the marked resource.
 */
public record ModrinthMarker(String projectID, String fileID) implements Marker {
	
	@Override
	public String name() {
		return "Modrinth"; // always return "Modrinth"
	}
}