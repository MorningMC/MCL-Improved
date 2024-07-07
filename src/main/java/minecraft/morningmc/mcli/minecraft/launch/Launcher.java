package minecraft.morningmc.mcli.minecraft.launch;

import minecraft.morningmc.mcli.minecraft.client.profile.Profile;
import minecraft.morningmc.mcli.minecraft.client.profile.ProfileCollection;
import minecraft.morningmc.mcli.minecraft.launch.listener.ProcessListener;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.exceptions.LaunchException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * The {@link Launcher} class is responsible for launching the Minecraft client with specified options and profiles.
 */
public class Launcher {
	private static final Logger logger = LogManager.getLogger();
	
	/** {@link NbtLoader} for loading and saving {@link Launcher} objects from/to NBT data. */
	public static final NbtLoader<Launcher, CompoundTag> loader = new NbtLoader<>() {
		
		/**
		 * Loads a {@link Launcher} object from an NBT compound tag.
		 *
		 * @param tag The NBT compound tag representing the {@link Launcher} object.
		 * @return The loaded {@link Launcher} object.
		 * @throws IllegalNbtException If the NBT data is invalid or missing required information.
		 */
		@Override
		public Launcher load(CompoundTag tag) throws IllegalNbtException {
			LaunchOptions options = LaunchOptions.loader.load(tag.getCompound("options"));
			
			UUID profile;
			try {
				profile = UUID.fromString(tag.getString("profile").getValue());
			} catch (Exception e) {
				logger.warn("Failed to load profile: {}", e.getMessage());
				profile = null;
			}
			
			UUID account;
			try {
				account = UUID.fromString(tag.getString("account").getValue());
			} catch (Exception e) {
				logger.warn("Failed to load account: {}", e.getMessage());
				account = null;
			}
			
			return new Launcher(options, profile, account);
		}
		
		/**
		 * Saves a {@link Launcher} object to an NBT compound tag.
		 *
		 * @param object The {@link Launcher} object to be saved.
		 * @return The NBT compound tag representing the {@link Launcher} object.
		 */
		@Override
		public CompoundTag save(Launcher object) {
			CompoundTag tag = new CompoundTag();
			
			try {
				tag.putString("profile", object.profile.toString());
			} catch (Exception e) {
				logger.warn("Failed to save profile: {}", e.getMessage());
			}
			
			try {
				tag.putString("account", object.account.toString());
			} catch (Exception e) {
				logger.warn("Failed to save account: {}", e.getMessage());
			}
			
			tag.put("options", LaunchOptions.loader.save(object.options));
			
			return tag;
		}
	};
	
	public final LaunchOptions options;
	public UUID profile;
	public UUID account;
	
	/**
	 * Constructs a {@link Launcher} object with the specified launch options and profile.
	 *
	 * @param options The launch options for the Minecraft client.
	 * @param profile The UUID of Minecraft profile to be used for launching.
	 * @param account The UUID of Minecraft account to be used for launching.
	 */
	public Launcher(LaunchOptions options, UUID profile, UUID account) {
		this.options = options;
		this.profile = profile;
		this.account = account;
	}
	
	/**
	 * Launches the Minecraft client using the stored profile and options.
	 *
	 * @return A {@link ProcessListener} for monitoring the launched process.
	 * @throws LaunchException If there is an issue launching the Minecraft client.
	 */
	public ProcessListener launch() throws LaunchException {
		return launch(ProfileCollection.resolve(profile));
	}
	
	/**
	 * Launches the Minecraft client using the specified profile and the stored options.
	 *
	 * @param profile The Minecraft profile to be used for launching.
	 * @return A {@link ProcessListener} for monitoring the launched process.
	 * @throws LaunchException If there is an issue launching the Minecraft client.
	 */
	public ProcessListener launch(Profile profile) throws LaunchException {
		return launch(generateArguments(profile));
	}
	
	/**
	 * Launches the Minecraft client using the specified launch arguments.
	 *
	 * @param arguments The launch arguments for the Minecraft client.
	 * @return A {@link ProcessListener} for monitoring the launched process.
	 * @throws LaunchException If there is an issue launching the Minecraft client.
	 */
	private ProcessListener launch(LaunchArguments arguments) throws LaunchException {
		Objects.requireNonNull(arguments);
		
		logger.info("Launching Minecraft...");
		
		// log commandline
		List<String> commandline = arguments.generateCommandline();
		logger.debug("Commandline: ");
		for (String arg : commandline) {
			logger.debug(arg);
		}
		
		ProcessBuilder builder = new ProcessBuilder(commandline);
		builder.directory(arguments.getDirectory().root);
		
		Process process;
		try {
			process = builder.start();
		} catch (Exception e) {
			throw new LaunchException("Couldn't start process", e);
		}
		
		return new ProcessListener(process);
	}
	
	/**
	 * Generates the launch arguments based on the stored profile and options.
	 *
	 * @return The generated launch arguments.
	 */
	public LaunchArguments generateArguments() {
		return generateArguments(ProfileCollection.resolve(profile));
	}
	
	/**
	 * Generates the launch arguments based on the specified profile and stored options.
	 *
	 * @param profile The Minecraft profile to be used for generating launch arguments.
	 * @return The generated launch arguments.
	 * @throws NullPointerException If the profile is null.
	 */
	public LaunchArguments generateArguments(Profile profile) {
		return new LaunchArguments(profile.options.getIfEnabled(options), profile);
	}
}
