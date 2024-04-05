package minecraft.morningmc.mcli.minecraft.launch;

import minecraft.morningmc.mcli.minecraft.client.directory.TargetMinecraftDirectory;
import minecraft.morningmc.mcli.minecraft.client.profile.Profile;

import java.io.File;
import java.util.*;

public record LaunchArguments(LaunchOptions options, Profile profile) {
	
	public LaunchArguments(LaunchOptions options, Profile profile) {
		this.options = Objects.requireNonNull(options);
		this.profile = Objects.requireNonNull(profile);
	}
	
	public List<String> generateCommandline() {
		return List.of();
	}
	
	public TargetMinecraftDirectory getDirectory() {
		TargetMinecraftDirectory directory = switch (options.gameDirPolicy().get()) {
			case ISOLATED -> new TargetMinecraftDirectory(new File(TargetMinecraftDirectory.ISOLATE_ROOT, profile.getName()));
			case CUSTOM -> options.gameDir().get();
			case STANDARD -> TargetMinecraftDirectory.STANDARD;
			default -> profile.getVersion().getSource().toTarget();
		};
		
		directory.getRoot().mkdirs();
		return directory;
	}
}
