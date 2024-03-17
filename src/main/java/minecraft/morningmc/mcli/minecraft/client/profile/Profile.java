package minecraft.morningmc.mcli.minecraft.client.profile;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.minecraft.client.directory.TargetMinecraftDirectory;
import minecraft.morningmc.mcli.minecraft.client.Version;
import minecraft.morningmc.mcli.minecraft.launch.LaunchOptions;
import minecraft.morningmc.mcli.utils.Switchable;
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
		public Profile loadFromNbt(CompoundTag tag) throws IllegalNbtException {
			String name = tag.getString("name").getValue();
			String icon = tag.getString("icon").getValue();
			Version.Policy versionPolicy = Version.Policy.valueOf(tag.getString("versionPolicy").getValue());
			Version version = Version.LOADER.loadFromNbt(tag.getCompound("version"));
			Switchable<LaunchOptions> options = NbtLoader.switchableLoader(LaunchOptions.LOADER).loadFromNbt(tag.getCompound("options"));
			
			return new Profile(name, icon, versionPolicy, version, options);
		}
		
		@Override
		public CompoundTag saveToNbt(Profile object) {
			CompoundTag tag = new CompoundTag();
			
			if (object == null) {
				return tag;
			}
			
			tag.putString("name", object.name);
			tag.putString("icon", object.icon);
			tag.putString("versionPolicy", object.versionPolicy.name());
			tag.put("version", Version.LOADER.saveToNbt(object.version));
			tag.put("options", NbtLoader.switchableLoader(LaunchOptions.LOADER).saveToNbt(object.options));
			
			return tag;
		}
	};
	
	private String name;
	private String icon;
	private Version.Policy versionPolicy;
	private Version version;
	private Switchable<LaunchOptions> options;
	
	/**
	 * Constructs a new Profile instance.
	 *
	 * @param name The name of the profile.
	 * @param icon The icon representing the profile.
	 * @param versionPolicy The version policy for the profile.
	 * @param version The Minecraft version associated with the profile.
	 * @param options The launch options for this profile.
	 */
	public Profile(String name,
	               String icon,
	               Version.Policy versionPolicy,
	               Version version,
	               Switchable<LaunchOptions> options) {
		
		this.name = name;
		this.icon = icon;
		this.versionPolicy = versionPolicy;
		this.version = version;
		this.options = options;
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
	public Version.Policy getVersionPolicy() {
		return versionPolicy;
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
	 * @param versionPolicy The new version policy.
	 */
	public void setVersionPolicy(Version.Policy versionPolicy) {
		this.versionPolicy = versionPolicy;
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
