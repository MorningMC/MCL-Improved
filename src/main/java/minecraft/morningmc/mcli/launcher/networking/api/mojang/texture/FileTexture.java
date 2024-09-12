package minecraft.morningmc.mcli.launcher.networking.api.mojang.texture;

import java.io.*;
import java.util.*;

public record FileTexture(File file, Map<String, String> metadata) implements Texture {
	
	@Override
	public InputStream openStream() throws IOException {
		return new FileInputStream(file);
	}
}
