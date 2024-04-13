package minecraft.morningmc.mcli.minecraft.client.profile;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.minecraft.client.directory.TargetMinecraftDirectory;
import minecraft.morningmc.mcli.minecraft.client.Version;
import minecraft.morningmc.mcli.minecraft.launch.LaunchOptions;
import minecraft.morningmc.mcli.utils.containers.Switchable;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import javafx.scene.image.Image;

import dev.dewy.nbt.tags.collection.CompoundTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.*;

/**
 * Represents a Minecraft profile, storing information such as name, icon, version, launch options, etc.
 */
public class Profile implements Comparable<Profile> {
	private static final Logger LOGGER = LogManager.getLogger();
	
	/** NbtLoader for loading and saving {@code Profile} objects from/to NBT data. */
	public static final NbtLoader<Profile, CompoundTag> LOADER = new NbtLoader<>() {
		
		@Override
		public Profile load(CompoundTag tag) throws IllegalNbtException {
			String name = tag.getString("name").getValue();
			String icon = tag.getString("icon").getValue();
			Version.Type versionType = Version.Type.valueOf(tag.getString("versionPolicy").getValue());
			Version version = Version.LOADER.load(tag.getCompound("version"));
			Switchable<LaunchOptions> options = Switchable.generateLoader(LaunchOptions.LOADER).load(tag.getCompound("options"));
			UUID uuid = UUID.fromString(tag.getString("uuid").getValue());
			
			return new Profile(name, icon, versionType, version, options, uuid);
		}
		
		@Override
		public CompoundTag save(Profile object) {
			CompoundTag tag = new CompoundTag();
			
			if (object == null) {
				return tag;
			}
			
			tag.putString("name", object.name);
			tag.putString("icon", object.icon);
			tag.putString("versionPolicy", object.versionType.name());
			tag.put("version", Version.LOADER.save(object.version));
			tag.put("options", Switchable.generateLoader(LaunchOptions.LOADER).save(object.options));
			tag.putString("uuid", object.uuid.toString());
			
			return tag;
		}
	};
	
	private String name;
	private String icon;
	private Version.Type versionType;
	private Version version;
	private Switchable<LaunchOptions> options;
	private final UUID uuid;
	
	/**
	 * Constructs a new Profile instance.
	 *
	 * @param name The name of the profile.
	 * @param icon The icon representing the profile.
	 * @param versionType The version policy for the profile.
	 * @param version The Minecraft version associated with the profile.
	 * @param options The launch options for this profile.
	 */
	public Profile(String name,
	               String icon,
	               Version.Type versionType,
	               Version version,
	               Switchable<LaunchOptions> options) {
		
		this(name, icon, versionType, version, options, UUID.randomUUID());
	}
	
	/**
	 * Constructs a new Profile instance.
	 *
	 * @param name The name of the profile.
	 * @param icon The icon representing the profile.
	 * @param versionType The version policy for the profile.
	 * @param version The Minecraft version associated with the profile.
	 * @param options The launch options for this profile.
	 * @param uuid The UUID associated with the profile.
	 */
	private Profile(String name,
	               String icon,
	               Version.Type versionType,
	               Version version,
	               Switchable<LaunchOptions> options,
	               UUID uuid) {
		
		this.name = name;
		this.icon = icon;
		this.versionType = versionType;
		this.version = version;
		this.options = options;
		this.uuid = uuid;
	}
	
	/**
	 * Renames the profile.
	 *
	 * @param name The new name for the profile.
	 */
	public void rename(String name) {
		File isolated = new File(TargetMinecraftDirectory.ISOLATE_ROOT, this.name);
		if (isolated.exists()) {
			isolated.renameTo(new File(TargetMinecraftDirectory.ISOLATE_ROOT, name));
		}
		
		this.name = name;
	}
	
	// Getters
	/**
	 * Gets the name of the profile.
	 *
	 * @return The name of the profile.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the icon representing the profile.
	 *
	 * @return The icon representing the profile.
	 */
	public String getIcon() {
		return icon;
	}
	
	/**
	 * Gets the icon image associated with the profile.
	 *
	 * @return The icon image.
	 */
	public Image getIconImage() {
		try {
			return new Image(FileMetadata.getResource(icon));
			
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Gets the version policy for the profile.
	 *
	 * @return The version policy for the profile.
	 */
	public Version.Type getVersionPolicy() {
		return versionType;
	}
	
	/**
	 * Gets the Minecraft version associated with the profile.
	 *
	 * @return The Minecraft version.
	 */
	public Version getVersion() {
		return version;
	}
	
	/**
	 * Gets the launch options for this profile.
	 *
	 * @return The launch options.
	 */
	public Switchable<LaunchOptions> getOptions() {
		return options;
	}
	
	/**
	 * Gets the UUID associated with the profile.
	 *
	 * @return The UUID associated with the profile.
	 */
	public UUID getUUID() {
		return uuid;
	}
	
	// Setters
	/**
	 * Sets the icon representing the profile.
	 *
	 * @param icon The new icon.
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	/**
	 * Sets the version policy for the profile.
	 *
	 * @param versionType The new version policy.
	 */
	public void setVersionPolicy(Version.Type versionType) {
		this.versionType = versionType;
	}
	
	/**
	 * Sets the Minecraft version associated with the profile.
	 *
	 * @param version The new Minecraft version.
	 */
	public void setVersion(Version version) {
		this.version = version;
	}
	
	/**
	 * Sets the launch options for this profile.
	 *
	 * @param options The new launch options.
	 */
	public void setOptions(Switchable<LaunchOptions> options) {
		this.options = options;
	}
	
	// Overrides
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		
		Profile that = (Profile) o;
		return Objects.equals(name, that.name);
	}
	
	@Override
	public int compareTo(Profile o) {
		return String.CASE_INSENSITIVE_ORDER.compare(name, o.name);
	}
}
