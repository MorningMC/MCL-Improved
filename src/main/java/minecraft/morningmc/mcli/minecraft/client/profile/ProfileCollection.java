package minecraft.morningmc.mcli.minecraft.client.profile;

import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.collection.ListTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * The ProfileCollection class manages a collection of Minecraft profiles.
 */
public class ProfileCollection {
	private static final Logger LOGGER = LogManager.getLogger();
	
	/** NbtLoader for loading and saving ProfileCollection objects from/to NBT data. */
	public static final NbtLoader<ProfileCollection, ListTag<CompoundTag>> LOADER = new NbtLoader<>() {
		
		/**
		 * Loads a ProfileCollection object from a list of NBT compound tags.
		 *
		 * @param tag The list of NBT compound tags representing profiles.
		 * @return The loaded ProfileCollection object.
		 * @throws IllegalNbtException If the NBT data is invalid or missing required information.
		 */
		@Override
		public ProfileCollection loadFromNbt(ListTag<CompoundTag> tag) throws IllegalNbtException {
			Set<Profile> profiles = new TreeSet<>();
			
			for (CompoundTag subTag : tag) {
				try {
					profiles.add(Profile.LOADER.loadFromNbt(subTag));
				} catch (IllegalNbtException e) {
					LOGGER.warn("Failed to load profile from NBT: " + e.getMessage());
				}
			}
			
			init(profiles);
			return instance;
		}
		
		/**
		 * Saves a ProfileCollection object to a list of NBT compound tags.
		 *
		 * @param object The ProfileCollection object to be saved.
		 * @return The list of NBT compound tags representing profiles.
		 */
		@Override
		public ListTag<CompoundTag> saveToNbt(ProfileCollection object) {
			ListTag<CompoundTag> tag = new ListTag<>();
			
			for (Profile profile : object.profiles) {
				tag.add(Profile.LOADER.saveToNbt(profile));
			}
			
			return tag;
		}
	};
	
	public static ProfileCollection instance = null;
	
	private final Set<Profile> profiles = new TreeSet<>();
	
	/**
	 * Initializes the ProfileCollection with the given profiles.
	 *
	 * @param profiles The initial set of profiles for the collection.
	 */
	public static void init(Collection<Profile> profiles) {
		if (instance != null) {
			throw new IllegalStateException("ProfileCollection already initialized");
		}
		
		instance = new ProfileCollection();
		instance.profiles.addAll(profiles);
	}
	
	/**
	 * Gets the set of profiles in the collection.
	 *
	 * @return The set of profiles.
	 */
	public static Set<Profile> get() {
		return instance.profiles;
	}
	
	/**
	 * Adds a profile to the collection.
	 *
	 * @param profile The profile to be added.
	 */
	public static void add(Profile profile) {
		instance.profiles.add(profile);
	}
	
	/**
	 * Removes a profile from the collection.
	 *
	 * @param profile The profile to be removed.
	 */
	public static void remove(Profile profile) {
		instance.profiles.remove(profile);
	}
	
	/**
	 * Resolves a profile by name from the collection.
	 *
	 * @param name The name of the profile to be resolved.
	 * @return The resolved Profile object, or null if not found.
	 */
	public static Profile resolve(String name) {
		for (Profile profile : instance.profiles) {
			if (profile.getName().equals(name)) {
				return profile;
			}
		}
		
		LOGGER.warn("Failed to resolve profile: " + name);
		return null;
	}
}