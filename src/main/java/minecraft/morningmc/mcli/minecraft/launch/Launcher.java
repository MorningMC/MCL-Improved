package minecraft.morningmc.mcli.minecraft.launch;

import minecraft.morningmc.mcli.minecraft.client.profile.Profile;
import minecraft.morningmc.mcli.minecraft.client.profile.ProfileCollection;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.exceptions.LaunchException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.dewy.nbt.tags.collection.CompoundTag;

/**
 * The Launcher class is responsible for launching the Minecraft client with specified options and profiles.
 */
public class Launcher {
	private static final Logger LOGGER = LogManager.getLogger();
	
	/** NbtLoader for loading and saving Launcher objects from/to NBT data. */
	public static final NbtLoader<Launcher, CompoundTag> LOADER = new NbtLoader<>() {
		
		/**
		 * Loads a Launcher object from an NBT compound tag.
		 *
		 * @param tag The NBT compound tag representing the Launcher object.
		 * @return The loaded Launcher object.
		 * @throws IllegalNbtException If the NBT data is invalid or missing required information.
		 */
		@Override
		public Launcher loadFromNbt(CompoundTag tag) throws IllegalNbtException {
			LaunchOptions options = LaunchOptions.LOADER.loadFromNbt(tag.getCompound("options"));
			
			Profile profile;
			try {
				profile = ProfileCollection.resolve(tag.getString("profile").getValue());
			} catch (Exception e) {
				LOGGER.warn("Failed to load profile: " + e.getMessage());
				profile = null;
			}
			
			return new Launcher(options, profile);
		}
		
		/**
		 * Saves a Launcher object to an NBT compound tag.
		 *
		 * @param object The Launcher object to be saved.
		 * @return The NBT compound tag representing the Launcher object.
		 */
		@Override
		public CompoundTag saveToNbt(Launcher object) {
			CompoundTag tag = new CompoundTag();
			
			try {
				tag.putString("profile", object.profile.getName());
			} catch (Exception e) {
				LOGGER.warn("Failed to save profile: " + e.getMessage());
			}
			
			tag.put("options", LaunchOptions.LOADER.saveToNbt(object.options));
			
			return tag;
		}
	};
	
	private LaunchOptions options;
	private Profile profile;
	
	/**
	 * Constructs a Launcher object with the specified launch options and profile.
	 *
	 * @param options The launch options for the Minecraft client.
	 * @param profile The Minecraft profile to be used for launching.
	 */
	public Launcher(LaunchOptions options, Profile profile) {
		this.options = options;
		this.profile = profile;
	}
	
	/**
	 * Launches the Minecraft client using the stored profile and options.
	 *
	 * @return A ProcessListener for monitoring the launched process.
	 * @throws LaunchException If there is an issue launching the Minecraft client.
	 */
	public ProcessListener launch() throws LaunchException {
		return launch(profile);
	}
	
	/**
	 * Launches the Minecraft client using the specified profile and the stored options.
	 *
	 * @param profile The Minecraft profile to be used for launching.
	 * @return A ProcessListener for monitoring the launched process.
	 * @throws LaunchException If there is an issue launching the Minecraft client.
	 */
	public ProcessListener launch(Profile profile) throws LaunchException {
		return launch(generateArguments(profile));
	}
	
	/**
	 * Launches the Minecraft client using the specified launch arguments.
	 *
	 * @param arguments The launch arguments for the Minecraft client.
	 * @return A ProcessListener for monitoring the launched process.
	 * @throws LaunchException If there is an issue launching the Minecraft client.
	 */
	private ProcessListener launch(LaunchArguments arguments) throws LaunchException {
		if (arguments == null) {
			throw new NullPointerException("LaunchArguments cannot be null");
		}
		
		ProcessBuilder builder = new ProcessBuilder(arguments.generateCommandline());
		builder.directory(arguments.getDirectory());
		
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
	 * @return The generated LaunchArguments.
	 */
	public LaunchArguments generateArguments() {
		return generateArguments(profile);
	}
	
	/**
	 * Generates the launch arguments based on the specified profile and stored options.
	 *
	 * @param profile The Minecraft profile to be used for generating launch arguments.
	 * @return The generated LaunchArguments.
	 * @throws NullPointerException If the profile is null.
	 */
	public LaunchArguments generateArguments(Profile profile) {
		LaunchOptions o = profile.isUseCustomOptions() ? profile.getOptions() : options;
		return new LaunchArguments(o, profile);
	}
	
	/**
	 * Gets the stored launch options.
	 *
	 * @return The LaunchOptions object.
	 */
	public LaunchOptions getOptions() {
		return options;
	}
	
	/**
	 * Gets the stored Minecraft profile.
	 *
	 * @return The Profile object.
	 */
	public Profile getProfile() {
		return profile;
	}
	
	/**
	 * Sets the launch options for the Launcher.
	 *
	 * @param options The new LaunchOptions.
	 */
	public void setOptions(LaunchOptions options) {
		this.options = options;
	}
	
	/**
	 * Sets the Minecraft profile for the Launcher.
	 *
	 * @param profile The new Profile.
	 */
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
}
