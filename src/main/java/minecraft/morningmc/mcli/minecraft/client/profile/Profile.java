package minecraft.morningmc.mcli.minecraft.client.profile;

import minecraft.morningmc.mcli.launcher.main.FileManager;
import minecraft.morningmc.mcli.minecraft.client.Version;
import minecraft.morningmc.mcli.minecraft.launch.LaunchOptions;
import minecraft.morningmc.mcli.utils.interfaces.UniqueObject;
import minecraft.morningmc.mcli.utils.containers.Switchable;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import javafx.scene.image.Image;

import dev.dewy.nbt.tags.collection.CompoundTag;

import java.util.*;

/**
 * Represents a Minecraft profile, storing information such as name, icon, version, launch options, etc.
 */
public final class Profile implements UniqueObject {
	/** {@link NbtLoader} for loading and saving {@link Profile} objects from/to NBT data. */
	public static final NbtLoader<Profile, CompoundTag> loader = new NbtLoader<>() {
		
		@Override
		public Profile load(CompoundTag tag) throws IllegalNbtException {
			return new Profile(
					tag.getString("name").getValue(),
					tag.getString("icon").getValue(),
					Version.loader.load(tag.getString("version")),
					Switchable.generateLoader(LaunchOptions.loader).load(tag.getCompound("options")),
					UUID.fromString(tag.getString("identifier").getValue()));
		}
		
		@Override
		public CompoundTag save(Profile object) {
			CompoundTag tag = new CompoundTag();
			
			if (object == null) {
				return tag;
			}
			
			tag.putString("name", object.name);
			tag.putString("icon", object.icon);
			tag.put("version", Version.loader.save(object.version));
			tag.put("options", Switchable.generateLoader(LaunchOptions.loader).save(object.options));
			tag.putString("identifier", object.identifier.toString());
			
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
	public Image getIconImage() {
		try {
			return new Image(FileManager.getResource(icon));
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public UUID identifier() {
		return identifier;
	}
}