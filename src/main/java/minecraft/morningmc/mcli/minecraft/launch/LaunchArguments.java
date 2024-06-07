package minecraft.morningmc.mcli.minecraft.launch;

import minecraft.morningmc.mcli.minecraft.client.MinecraftDirectory;
import minecraft.morningmc.mcli.minecraft.client.profile.Profile;

import java.io.File;
import java.util.*;

/**
 * Represents the arguments to be passed to the Minecraft client.
 *
 * @param options the launch options.
 * @param profile the profile to be launched.
 */
public record LaunchArguments(LaunchOptions options, Profile profile) {
	
	public LaunchArguments(LaunchOptions options, Profile profile) {
		this.options = Objects.requireNonNull(options);
		this.profile = Objects.requireNonNull(profile);
	}
	
	public List<String> generateCommandline() {
		return List.of(); // TODO complete launch statement
	}
	
	public MinecraftDirectory getDirectory() {
		MinecraftDirectory directory = options.gameDir().get((value, policy) -> switch (policy) {
			case ISOLATED -> new MinecraftDirectory(new File(MinecraftDirectory.isolateRoot, profile.identifier().toString()));
			case CUSTOM -> value;
			default -> MinecraftDirectory.standard;
		});
		
		directory.root().mkdirs();
		return directory;
	}
}
