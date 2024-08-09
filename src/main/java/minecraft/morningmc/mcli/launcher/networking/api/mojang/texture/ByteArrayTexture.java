package minecraft.morningmc.mcli.launcher.networking.api.mojang.texture;

import java.io.*;
import java.util.*;

public record ByteArrayTexture(byte[] data, Map<String, String> metadata) implements Texture {
	
	@Override
	public InputStream openStream() {
		return new ByteArrayInputStream(data);
	}
}
