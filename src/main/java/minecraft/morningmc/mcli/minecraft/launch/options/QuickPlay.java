package minecraft.morningmc.mcli.minecraft.launch.options;

import minecraft.morningmc.mcli.minecraft.client.resources.World;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.primitive.StringTag;

import java.util.*;

/**
 * Represents a Quick Play in the client.
 *
 * @param type The type of the Quick Play.
 * @param singleplayer The singleplayer Quick Play.
 * @param multiplayer The multiplayer Quick Play.
 */
public record QuickPlay(Type type, Singleplayer singleplayer, Multiplayer multiplayer) {
	/** {@link NbtLoader} for loading and saving {@link QuickPlay} objects from/to NBT data. */
	public static final NbtLoader<QuickPlay, CompoundTag> loader = new NbtLoader<>() {
		
		@Override
		public QuickPlay load(CompoundTag tag) {
			try {
				return new QuickPlay(
						Type.values()[tag.getInt("type").getValue()],
						Singleplayer.loader.load(tag.getString("singleplayer")),
						Multiplayer.loader.load(tag.getCompound("multiplayer"))
				);
			} catch (Exception e) {
				throw new IllegalArgumentException("Exception while loading QuickPlay object", e);
			}
		}
		
		@Override
		public CompoundTag save(QuickPlay object) {
			CompoundTag tag = new CompoundTag();
			
			tag.putInt("type", object.type().ordinal());
			tag.put("singleplayer", Singleplayer.loader.save(object.singleplayer()));
			tag.put("multiplayer", Multiplayer.loader.save(object.multiplayer()));
			
			return tag;
		}
	};
	
	/**
	 * Creates a new {@link QuickPlay} object with the specified type, singleplayer, and multiplayer.
	 *
	 * @param type         The type of the Quick Play.
	 * @param singleplayer The singleplayer Quick Play.
	 * @param multiplayer  The multiplayer Quick Play.
	 * @return A new {@link QuickPlay} object.
	 */
	public static QuickPlay of(Type type, Singleplayer singleplayer, Multiplayer multiplayer) {
		return new QuickPlay(type, singleplayer, multiplayer);
	}
	
	/**
	 * Creates a new {@link QuickPlay} object with the type {@link Type#NONE}.
	 *
	 * @return A new {@link QuickPlay} object.
	 */
	public static QuickPlay none() {
		return of(Type.NONE, null, null);
	}
	
	/**
	 * Creates a new {@link QuickPlay} object with the type {@link Type#SINGLEPLAYER}.
	 *
	 * @param singleplayer The singleplayer Quick Play.
	 * @return A new {@link QuickPlay} object.
	 */
	public static QuickPlay singleplayer(Singleplayer singleplayer) {
		return of(Type.SINGLEPLAYER, singleplayer, null);
	}
	
	/**
	 * Creates a new {@link QuickPlay} object with the type {@link Type#MULTIPLAYER}.
	 *
	 * @param multiplayer The multiplayer Quick Play.
	 * @return A new {@link QuickPlay} object.
	 */
	public static QuickPlay multiplayer(Multiplayer multiplayer) {
		return of(Type.MULTIPLAYER, null, multiplayer);
	}
	
	/**
	 * Enumerates the types of the world entry.
	 */
	public enum Type {
		NONE, SINGLEPLAYER, MULTIPLAYER
	}
	
	public record Singleplayer(String world) {
		/** {@link NbtLoader} for loading and saving {@link Singleplayer} objects from/to NBT data. */
		public static final NbtLoader<Singleplayer, StringTag> loader = new NbtLoader<>() {
			
			@Override
			public Singleplayer load(StringTag tag) {
				String world = tag.getValue();
				return world.isEmpty() ? null : of(world);
			}
			
			@Override
			public StringTag save(Singleplayer object) {
				if (object != null) {
					return new StringTag(object.world);
				}
				return new StringTag("");
			}
		};
		
		/**
		 * Creates a new {@link Singleplayer} object with the specified world.
		 *
		 * @param world The world of the singleplayer Quick Play.
		 * @return A new {@link Singleplayer} object.
		 */
		public static Singleplayer of(World world) {
			return of(world.folderName());
		}
		
		/**
		 * Creates a new {@link Singleplayer} object with the folder name of the specified world.
		 *
		 * @param world The folder name of the world of the singleplayer Quick Play.
		 * @return A new {@link Singleplayer} object.
		 * @throws NullPointerException If the folder name of the world is null.
		 */
		public static Singleplayer of(String world) {
			return new Singleplayer(Objects.requireNonNull(world));
		}
		
		@Override
		public String toString() {
			return world;
		}
	}
	
	/**
	 * Represents information about a Minecraft multiplayer, including its host and port.
	 */
	public record Multiplayer(String host, int port) {
		/** {@link NbtLoader} for loading and saving {@link Multiplayer} objects from/to NBT data. */
		public static final NbtLoader<Multiplayer, CompoundTag> loader = new NbtLoader<>() {
			
			/**
			 * Loads a {@link Multiplayer} object from an NBT compound tag.
			 *
			 * @param tag The NBT compound tag representing the {@link Multiplayer} object.
			 * @return The loaded {@link Multiplayer} object, or null if an error occurs.
			 */
			@Override
			public Multiplayer load(CompoundTag tag) {
				try {
					String host = tag.getString("host").getValue();
					int port = tag.getInt("port").getValue();
					
					return of(host, port);
				} catch (Exception e) {
					return null;
				}
			}
			
			/**
			 * Saves a {@link Multiplayer} object to an NBT compound tag.
			 *
			 * @param object The {@link Multiplayer} object to be saved.
			 * @return The NBT compound tag representing the {@link Multiplayer} object.
			 */
			@Override
			public CompoundTag save(Multiplayer object) {
				CompoundTag tag = new CompoundTag();
				
				if (object != null) {
					tag.putString("host", object.host);
					tag.putInt("port", object.port);
				}
				
				return tag;
			}
		};
		
		/**
		 * Constructs a {@link Multiplayer} object with the specified host and default port (25565).
		 *
		 * @param host The host of the Minecraft multiplayer.
		 * @return A new {@link Multiplayer} object.
		 * @throws NullPointerException If the host is {@code null}.
		 */
		public static Multiplayer of(String host) {
			return of(host, 25565);
		}
		
		/**
		 * Constructs a {@link Multiplayer} object with the specified host and port.
		 *
		 * @param host The host of the Minecraft multiplayer.
		 * @param port The port of the Minecraft multiplayer.
		 * @return A new {@link Multiplayer} object.
		 * @throws IndexOutOfBoundsException If the port is not within the valid range [0, 65535].
		 * @throws NullPointerException If the host is {@code null}.
		 */
		public static Multiplayer of(String host, int port) {
			if (port < 0 || port > 65535) {
				throw new IndexOutOfBoundsException("Port must be between 0 and 65535");
			}
			
			return new Multiplayer(Objects.requireNonNull(host), port);
		}
		
		/**
		 * Returns a string representation of the {@link Multiplayer} object in the format {@code "host:port"}.
		 *
		 * @return The string representation of the {@link Multiplayer} object.
		 */
		@Override
		public String toString() {
			return host + ':' + port;
		}
	}
}