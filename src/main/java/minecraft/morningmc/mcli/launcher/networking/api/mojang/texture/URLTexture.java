package minecraft.morningmc.mcli.launcher.networking.api.mojang.texture;

import minecraft.morningmc.mcli.launcher.networking.Requester;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public record URLTexture(URL url, Map<String, String> metadata) implements Texture {
	
	@Override
	public InputStream openStream() throws IOException {
		return Requester.openConnection(url).getInputStream();
	}
}
