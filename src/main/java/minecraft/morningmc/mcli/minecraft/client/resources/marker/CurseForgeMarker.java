package minecraft.morningmc.mcli.minecraft.client.resources.marker;

/**
 * Represents a marker for CurseForge resources.
 *
 * @param projectID The project ID of the marked resource.
 * @param fileID    The file ID of the marked resource.
 */
public record CurseForgeMarker(String projectID, String fileID) implements Marker {
	
	@Override
	public String name() {
		return "CurseForge"; // always return "CurseForge"
	}
}
