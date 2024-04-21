package minecraft.morningmc.mcli.minecraft.client.profile;

import minecraft.morningmc.mcli.launcher.metadata.FileMetadata;
import minecraft.morningmc.mcli.minecraft.client.Version;
import minecraft.morningmc.mcli.minecraft.launch.LaunchOptions;
import minecraft.morningmc.mcli.utils.containers.Modifiable;
import minecraft.morningmc.mcli.utils.containers.Switchable;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import javafx.scene.image.Image;

import dev.dewy.nbt.tags.collection.CompoundTag;

import java.util.*;

/**
 * Represents a Minecraft profile, storing information such as name, icon, version, launch options, etc.
 */
public record Profile(Modifiable<String> name,
                      Modifiable<String> icon,
                      Modifiable<Version> version,
                      Switchable<LaunchOptions> options,
                      UUID uuid) implements Comparable<Profile> {
	
	/** NbtLoader for loading and saving {@code Profile} objects from/to NBT data. */
	public static final NbtLoader<Profile, CompoundTag> LOADER = new NbtLoader<>() {
		
		@Override
		public Profile load(CompoundTag tag) throws IllegalNbtException {
			return new Profile(
					Modifiable.of(tag.getString("name").getValue()),
					Modifiable.of(tag.getString("icon").getValue()),
					Modifiable.of(Version.LOADER.load(tag.getCompound("version"))),
					Switchable.generateLoader(LaunchOptions.LOADER).load(tag.getCompound("options")),
					UUID.fromString(tag.getString("uuid").getValue())
			);
		}
		
		@Override
		public CompoundTag save(Profile object) {
			CompoundTag tag = new CompoundTag();
			
			if (object == null) {
				return tag;
			}
			
			tag.putString("name", object.name.get());
			tag.putString("icon", object.icon.get());
			tag.put("version", Version.LOADER.save(object.version.get()));
			tag.put("options", Switchable.generateLoader(LaunchOptions.LOADER).save(object.options));
			tag.putString("uuid", object.uuid.toString());
			
			return tag;
		}
	};
	
	/**
	 * Constructs a new Profile instance.
	 *
	 * @param name The name of the profile.
	 * @param icon The icon representing the profile.
	 * @param version The Minecraft version associated with the profile.
	 * @param options The launch options for this profile.
	 */
	public static Profile of(Modifiable<String> name, Modifiable<String> icon, Modifiable<Version> version, Switchable<LaunchOptions> options) {
		return new Profile(name, icon, version, options, UUID.randomUUID());
	}
	
	/**
	 * Gets the icon image associated with the profile.
	 *
	 * @return The icon image.
	 */
	public Image getIconImage() {
		try {
			return new Image(FileMetadata.getResource(icon.get()));
			
		} catch (Exception e) {
			return null;
		}
	}
	
	// Overrides
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o instanceof Profile that) {
			return uuid.equals(that.uuid);
		}
		return false;
	}
	
	@Override
	public int compareTo(Profile o) {
		return name.get().compareTo(o.name.get());
	}
}
