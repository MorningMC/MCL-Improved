package minecraft.morningmc.mcli.minecraft.launch;

import minecraft.morningmc.mcli.launcher.settings.FileSettings;
import minecraft.morningmc.mcli.minecraft.auth.Account;
import minecraft.morningmc.mcli.minecraft.client.MinecraftDirectory;
import minecraft.morningmc.mcli.minecraft.client.Profile;
import minecraft.morningmc.mcli.minecraft.launch.options.LaunchOptions;

import java.io.File;
import java.util.*;

/**
 * Represents the arguments to be passed to the Minecraft client.
 *
 * @param options The launch options.
 * @param profile The profile to be used.
 * @param account The account to be used.
 */
public record LaunchArguments(LaunchOptions options, Profile profile, Account account) {
	
	/**
	 * Constructs a new {@link LaunchArguments} instance.
	 *
	 * @param options The launch options.
	 * @param profile The profile to be used.
	 * @param account The account to be used.
	 *
	 * @throws NullPointerException If any of the parameters are {@code null}.
	 */
	public LaunchArguments(LaunchOptions options, Profile profile, Account account) {
		this.options = Objects.requireNonNull(options);
		this.profile = Objects.requireNonNull(profile);
		this.account = Objects.requireNonNull(account);
	}
	
	/**
	 * Generates the commandline for launching the Minecraft client.
	 *
	 * @return The commandline for launching the Minecraft client.
	 */
	public List<String> generateCommandline() {
		return List.of(); // TODO complete launch statement
	}
	
	/**
	 * Gets the directory for the Minecraft client.
	 *
	 * @return The directory for the Minecraft client.
	 */
	public MinecraftDirectory getDirectory() {
		MinecraftDirectory directory = options.gameDir.getIfEnabled(new MinecraftDirectory(new File(FileSettings.isolateRoot, profile.identifier().toString())));
		directory.root.mkdirs();
		return directory;
	}
}
