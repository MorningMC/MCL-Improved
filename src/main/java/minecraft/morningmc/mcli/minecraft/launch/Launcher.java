package minecraft.morningmc.mcli.minecraft.launch;

import minecraft.morningmc.mcli.minecraft.auth.Account;
import minecraft.morningmc.mcli.minecraft.client.Profile;
import minecraft.morningmc.mcli.minecraft.launch.options.LaunchOptions;
import minecraft.morningmc.mcli.utils.annotations.StaticClass;
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
@StaticClass
public class Launcher {
	private static final Logger logger = LogManager.getLogger();
	
	/** {@link NbtLoader} for loading and saving {@link Launcher} objects from/to NBT data. */
	public static final NbtLoader<Void, CompoundTag> loader = new NbtLoader<>() {
		
		/**
		 * Loads a {@link Launcher} object from an NBT compound tag.
		 *
		 * @param tag The NBT compound tag representing the {@link Launcher} object.
		 * @return {@code null}.
		 * @throws IllegalNbtException If the NBT data is invalid or missing required information.
		 */
		@Override
		public Void load(CompoundTag tag) throws IllegalNbtException {
			options = LaunchOptions.loader.load(tag.getCompound("options"));
			
			try {
				profile = UUID.fromString(tag.getString("profile").getValue());
			} catch (Exception e) {
				logger.warn("Failed to load profile: {}", e.getMessage());
				profile = null;
			}
			
			try {
				account = UUID.fromString(tag.getString("account").getValue());
			} catch (Exception e) {
				logger.warn("Failed to load account: {}", e.getMessage());
				account = null;
			}
			
			return null;
		}
		
		/**
		 * Saves a {@link Launcher} object to an NBT compound tag.
		 *
		 * @param object {@code null}.
		 * @return The NBT compound tag representing the {@link Launcher} object.
		 */
		@Override
		public CompoundTag save(Void object) {
			CompoundTag tag = new CompoundTag();
			
			try {
				tag.putString("profile", profile.toString());
			} catch (Exception e) {
				logger.warn("Failed to save profile: {}", e.getMessage());
			}
			
			try {
				tag.putString("account", account.toString());
			} catch (Exception e) {
				logger.warn("Failed to save account: {}", e.getMessage());
			}
			
			tag.put("options", LaunchOptions.loader.save(options));
			
			return tag;
		}
	};
	
	public static LaunchOptions options;
	public static UUID profile;
	public static UUID account;
	
	/**
	 * Initializes the {@link Launcher} with default options, profile, and account.
	 */
	public static void initDefault() {
		options = LaunchOptions.defaultOptions;
		profile = null;
		account = null;
	}
	
	/**
	 * Launches the Minecraft client using the stored profile and options.
	 *
	 * @return A {@link ProcessListener} for monitoring the launched process.
	 * @throws LaunchException If there is an issue launching the Minecraft client.
	 */
	public static ProcessListener launch() throws LaunchException {
		return launch(Profile.Collection.resolve(profile));
	}
	
	/**
	 * Launches the Minecraft client using the specified profile and the stored options.
	 *
	 * @param profile The Minecraft profile to be used for launching.
	 * @return A {@link ProcessListener} for monitoring the launched process.
	 * @throws LaunchException If there is an issue launching the Minecraft client.
	 */
	public static ProcessListener launch(Profile profile) throws LaunchException {
		return launch(generateArguments(profile));
	}
	
	/**
	 * Launches the Minecraft client using the specified launch arguments.
	 *
	 * @param arguments The launch arguments for the Minecraft client.
	 * @return A {@link ProcessListener} for monitoring the launched process.
	 * @throws LaunchException If there is an issue launching the Minecraft client.
	 */
	private static ProcessListener launch(LaunchArguments arguments) throws LaunchException {
		Objects.requireNonNull(arguments);
		
		logger.info("Launching Minecraft...");
		
		// log commandline
		logger.debug("Commandline: {}", arguments.commandline);
		
		ProcessBuilder builder = new ProcessBuilder(arguments.commandline);
		builder.directory(arguments.getDirectory().root);
		
		Process process;
		try {
			process = builder.start();
		} catch (Exception e) {
			throw new LaunchException("Couldn't start process", e);
		}
		
		ProcessListener listener = new ProcessListener(process, arguments);
		ProcessListener.Collection.add(listener);
		return listener;
	}
	
	/**
	 * Generates the launch arguments based on the stored profile and options.
	 *
	 * @return The generated launch arguments.
	 */
	public static LaunchArguments generateArguments() {
		return generateArguments(Profile.Collection.resolve(profile));
	}
	
	/**
	 * Generates the launch arguments based on the specified profile and stored options.
	 *
	 * @param profile The Minecraft profile to be used for generating launch arguments.
	 * @return The generated launch arguments.
	 * @throws NullPointerException If the profile is null.
	 */
	public static LaunchArguments generateArguments(Profile profile) {
		return new LaunchArguments(profile.options.getIfEnabled(options), profile, Account.Collection.resolve(account));
	}
}
