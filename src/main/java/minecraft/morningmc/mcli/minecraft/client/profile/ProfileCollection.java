package minecraft.morningmc.mcli.minecraft.client.profile;


import minecraft.morningmc.mcli.utils.annotations.ObjectCollection;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.collection.ListTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.*;

/**
 * The {@code ProfileCollection} class manages a collection of Minecraft profiles.
 */
@ObjectCollection
public class ProfileCollection {
	private static final Logger LOGGER = LogManager.getLogger();
	
	/** NbtLoader for loading and saving {@code ProfileCollection} objects from/to NBT data. */
	public static final NbtLoader<ProfileCollection, ListTag<CompoundTag>> LOADER = new NbtLoader<>() {
		
		/**
		 * Loads a {@code ProfileCollection} object from a list of NBT compound tags.
		 *
		 * @param tag The list of NBT compound tags representing profiles.
		 * @return The loaded ProfileCollection object.
		 * @throws IllegalNbtException If the NBT data is invalid or missing required information.
		 */
		@Override
		public ProfileCollection loadFromNbt(ListTag<CompoundTag> tag) throws IllegalNbtException {
			init(tag.getValue().stream()
					     .flatMap(subTag -> {
						     try {
							     return Stream.of(Profile.LOADER.loadFromNbt(subTag));
						     } catch (IllegalNbtException e) {
							     LOGGER.warn("Failed to load profile from NBT: " + e.getMessage());
							     return Stream.empty();
						     }
					     })
					     .collect(Collectors.toSet()));
			return instance;
		}
		
		/**
		 * Saves a {@code ProfileCollection} object to a list of NBT compound tags.
		 *
		 * @param object The ProfileCollection object to be saved.
		 * @return The list of NBT compound tags representing profiles.
		 */
		@Override
		public ListTag<CompoundTag> saveToNbt(ProfileCollection object) {
			ListTag<CompoundTag> tag = new ListTag<>();
			
			object.profiles.values().stream()
					.map(Profile.LOADER::saveToNbt)
					.forEach(tag::add);
			
			return tag;
		}
	};
	
	public static ProfileCollection instance = null;
	
	private final Map<String, Profile> profiles = new HashMap<>();
	
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
		for (Profile profile : profiles) {
			add(profile);
		}
	}
	
	/**
	 * Gets the set of profiles in the collection.
	 *
	 * @return The set of profiles.
	 */
	public static Collection<Profile> get() {
		return instance.profiles.values();
	}
	
	/**
	 * Adds a profile to the collection.
	 *
	 * @param profile The profile to be added.
	 */
	public static void add(Profile profile) {
		instance.profiles.put(profile.getName(), profile);
	}
	
	/**
	 * Removes a profile from the collection.
	 *
	 * @param profile The profile to be removed.
	 */
	public static void remove(Profile profile) {
		instance.profiles.remove(profile.getName());
	}
	
	/**
	 * Resolves a profile by name from the collection.
	 *
	 * @param name The name of the profile to be resolved.
	 * @return The resolved Profile object, or null if not found.
	 */
	public static Profile resolve(String name) {
		return instance.profiles.get(name);
	}
}