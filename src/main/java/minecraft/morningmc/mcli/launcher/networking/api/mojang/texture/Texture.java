package minecraft.morningmc.mcli.launcher.networking.api.mojang.texture;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Represents a Minecraft texture.
 */
public interface Texture {
	
	/**
	 * Gets the metadata of the texture.
	 *
	 * @return The metadata of the texture.
	 */
	Map<String, String> metadata();
	
	/**
	 * Creates a {@link InputStream} of the texture.
	 *
	 * @return A {@link InputStream} of the texture.
	 * @throws IOException If an I/O error occurs.
	 */
	InputStream openStream() throws IOException;
	
	/**
	 * Enumerates different types of textures.
	 */
	enum Type {
		SKIN, CAPE, ELYTRA
	}
}