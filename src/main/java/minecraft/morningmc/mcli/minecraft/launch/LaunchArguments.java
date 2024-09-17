package minecraft.morningmc.mcli.minecraft.launch;

import minecraft.morningmc.mcli.minecraft.auth.Account;
import minecraft.morningmc.mcli.minecraft.client.MinecraftDirectory;
import minecraft.morningmc.mcli.minecraft.client.Profile;
import minecraft.morningmc.mcli.minecraft.java.JavaRuntime;
import minecraft.morningmc.mcli.minecraft.launch.options.LaunchOptions;
import minecraft.morningmc.mcli.utils.Conditional;

import java.util.*;

/**
 * Represents the arguments to be passed to the Minecraft client.
 */
public final class LaunchArguments {
	public final LaunchOptions options;
	public final Profile profile;
	public final Account account;
	
	public final List<String> commandline;
	
	/**
	 * Constructs a new {@link LaunchArguments} instance.
	 *
	 * @param options The launch options.
	 * @param profile The profile to be used.
	 * @param account The account to be used.
	 * @throws NullPointerException If any of the parameters are {@code null}.
	 */
	public LaunchArguments(LaunchOptions options, Profile profile, Account account) {
		this.options = Objects.requireNonNull(options);
		this.profile = Objects.requireNonNull(profile);
		this.account = Objects.requireNonNull(account);
		
		this.commandline = generateCommandline();
	}
	
	/**
	 * Generates the commandline for launching the Minecraft client.
	 *
	 * @return The commandline for launching the Minecraft client.
	 */
	private List<String> generateCommandline() {
		return List.of(); // TODO complete launch statement
	}
	
	/**
	 * Generates the commandline template for launching the Minecraft client.
	 *
	 * @param features The features that supported in the launch options.
	 * @return The commandline template.
	 */
	private List<String> generateCommandlineTemplate(Map<String, Boolean> features) {
		List<String> template = new ArrayList<>();
		
		template.add(options.javaRuntime.getIfEnabled(JavaRuntime.Collection.getOne(profile.version.javaVersion(), options.javaRuntime.get())).executable().getAbsolutePath());
		template.addAll(options.javaArguments.getIfEnabled(() -> profile.version.javaArguments().stream().filter(arg -> arg.check(features)).map(Conditional::value).toList()));
		template.add(profile.version.mainClass());
		template.addAll(profile.version.gameArguments().stream().filter(arg -> arg.check(features)).map(Conditional::value).toList());
		
		return template;
	}
	
	/**
	 * Generates the commandline parameters for launching the Minecraft client.
	 *
	 * @return The commandline parameters.
	 */
	private Map<String, String> generateCommandlineParameters() {
		return options.customArgumentParameters;
	}
	
	/**
	 * Gets the directory for the Minecraft client.
	 *
	 * @return The directory for the Minecraft client.
	 */
	public MinecraftDirectory getDirectory() {
		MinecraftDirectory directory = options.gameDir.getIfEnabled(profile::isolatedDirectory);
		directory.root.mkdirs();
		return directory;
	}
}
