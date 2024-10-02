package minecraft.morningmc.mcli.minecraft.client;

import minecraft.morningmc.mcli.launcher.settings.FileSettings;
import minecraft.morningmc.mcli.minecraft.client.version.Version;
import minecraft.morningmc.mcli.minecraft.launch.options.LaunchOptions;
import minecraft.morningmc.mcli.utils.annotations.ObjectCollection;
import minecraft.morningmc.mcli.utils.annotations.StaticClass;
import minecraft.morningmc.mcli.utils.functions.ExceptionUtils;
import minecraft.morningmc.mcli.utils.interfaces.*;
import minecraft.morningmc.mcli.utils.containers.Switchable;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;

import javafx.scene.image.Image;

import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.collection.ListTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.*;
import java.util.stream.*;

/**
 * Represents a Minecraft profile, storing information such as name, icon, version, launch options, etc.
 */
public final class Profile implements UniqueObject {
	private static final Logger logger = LogManager.getLogger();
	
	/** {@link NbtLoader} for loading and saving {@link Profile} objects from/to NBT data. */
	public static final NbtLoader<Profile, CompoundTag> loader = new NbtLoader<>() {
		
		@Override
		public Profile load(CompoundTag tag) throws IllegalNbtException {
			return new Profile(
					tag.getString("name").getValue(),
					tag.getString("icon").getValue(),
					Version.loader.load(tag.getString("version")),
					Switchable.generateLoader(LaunchOptions.loader).load(tag.getCompound("options")),
					NbtLoader.uuidLoader.load(tag.getIntArray("identifier"))
			);
		}
		
		@Override
		public CompoundTag save(Profile object) {
			CompoundTag tag = new CompoundTag();
			
			if (object != null) {
				tag.putString("name", object.name);
				tag.putString("icon", object.icon);
				tag.put("version", Version.loader.save(object.version));
				tag.put("options", Switchable.generateLoader(LaunchOptions.loader).save(object.options));
				tag.put("identifier", NbtLoader.uuidLoader.save(object.identifier));
			}
			
			return tag;
		}
	};
	public String name;
	public String icon;
	public Version version;
	public Switchable<LaunchOptions> options;
	private final UUID identifier;
	
	/**
	 * Constructs a new {@link Profile} instance.
	 *
	 * @param name       The name of the profile.
	 * @param icon       The icon representing the profile.
	 * @param version    The Minecraft version associated with the profile.
	 * @param options    The launch options for this profile.
	 * @param identifier The unique identifier of the profile.
	 */
	private Profile(String name, String icon, Version version, Switchable<LaunchOptions> options, UUID identifier) {
		this.name = name;
		this.icon = icon;
		this.version = version;
		this.options = options;
		this.identifier = identifier;
	}
	
	/**
	 * Constructs a new {@link Profile} instance.
	 *
	 * @param name    The name of the profile.
	 * @param icon    The icon representing the profile.
	 * @param version The Minecraft version associated with the profile.
	 * @param options The launch options for this profile.
	 */
	public static Profile of(String name, String icon, Version version, Switchable<LaunchOptions> options) {
		return new Profile(name, icon, version, options, UUID.randomUUID());
	}
	
	/**
	 * Gets the icon image associated with the profile.
	 *
	 * @return The icon image.
	 */
	public Image iconImage() {
		try {
			return new Image(ClassLoader.getSystemResourceAsStream(icon));
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Gets the isolated directory for the profile.
	 *
	 * @return The isolated directory.
	 */
	public MinecraftDirectory isolatedDirectory() {
		return new MinecraftDirectory(new File(FileSettings.isolateRoot, identifier.toString()));
	}
	
	@Override
	public UUID identifier() {
		return identifier;
	}
	
	/**
	 * Represents a collection of Minecraft profiles.
	 */
	@ObjectCollection
	@StaticClass
	public static class Collection {
		/** {@link NbtLoader} for loading and saving {@link Collection} objects from/to NBT data. */
		public static final NbtLoader<Void, ListTag<CompoundTag>> loader = new NbtLoader<>() {
			
			/**
			 * Loads a {@link Collection} object from a list of NBT compound tags.
			 *
			 * @param tag The list of NBT compound tags representing profiles.
			 * @return {@code null}.
			 */
			@Override
			public Void load(ListTag<CompoundTag> tag) {
				init(tag.getValue().stream()
						     .flatMap(subTag -> {
							     try {
								     return Stream.of(Profile.loader.load(subTag));
							     } catch (IllegalNbtException e) {
								     logger.warn("Failed to load profile from NBT: {}", ExceptionUtils.getMessages(e));
								     return Stream.empty();
							     }
						     })
						     .collect(Collectors.toSet()));
				return null;
			}
			
			/**
			 * Saves a {@link Collection} object to a list of NBT compound tags.
			 *
			 * @param object {@code null}.
			 * @return The list of NBT compound tags representing profiles.
			 */
			@Override
			public ListTag<CompoundTag> save(Void object) {
				ListTag<CompoundTag> tag = new ListTag<>();
				
				profiles.values().stream()
						.map(Profile.loader::save)
						.forEach(tag::add);
				
				return tag;
			}
		};
		
		private static final Map<UUID, Profile> profiles = new HashMap<>();
		
		/**
		 * Initializes the {@link Collection} with the given profiles.
		 *
		 * @param profiles The initial set of profiles for the collection.
		 */
		public static void init(java.util.Collection<Profile> profiles) {
			for (Profile profile : profiles) {
				add(profile);
			}
		}
		
		/**
		 * Gets the set of profiles in the collection.
		 *
		 * @return The set of profiles.
		 */
		public static java.util.Collection<Profile> get() {
			return profiles.values();
		}
		
		/**
		 * Adds a profile to the collection.
		 *
		 * @param profile The profile to be added.
		 */
		public static void add(Profile profile) {
			profiles.put(profile.identifier(), profile);
		}
		
		/**
		 * Removes a profile from the collection.
		 *
		 * @param profile The profile to be removed.
		 */
		public static void remove(Profile profile) {
			profiles.remove(profile.identifier());
		}
		
		/**
		 * Resolves a profile by name from the collection.
		 *
		 * @param uuid The UUID associated with the profile to be resolved.
		 * @return The resolved profile, or {@code null} if not found.
		 */
		public static Profile resolve(UUID uuid) {
			return profiles.get(uuid);
		}
	}
}