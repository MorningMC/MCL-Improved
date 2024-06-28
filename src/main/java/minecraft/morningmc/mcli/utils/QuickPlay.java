package minecraft.morningmc.mcli.utils;

import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

/**
 * Represents a world entry in the client.
 *
 * @param type The type of world entry.
 * @param singleplayer The singleplayer entry for the world.
 * @param multiplayer The multiplayer entry for the world.
 */
public record QuickPlay(Type type, Singleplayer singleplayer, Multiplayer multiplayer) {
	/** {@code NbtLoader} for loading and saving {@code QuickPlay} objects from/to NBT data. */
	public static final NbtLoader<QuickPlay, CompoundTag> loader = new NbtLoader<>() {
		
		@Override
		public QuickPlay load(CompoundTag tag) throws IllegalNbtException {
			return new QuickPlay(
					Type.valueOf(tag.getString("type").getValue()),
					Singleplayer.loader.load(tag.getCompound("singleplayer")),
					Multiplayer.loader.load(tag.getCompound("multiplayer"))
			);
		}
		
		@Override
		public CompoundTag save(QuickPlay object) {
			CompoundTag tag = new CompoundTag();
			
			tag.putString("type", object.type().name());
			tag.put("singleplayer", Singleplayer.loader.save(object.singleplayer()));
			tag.put("multiplayer", Multiplayer.loader.save(object.multiplayer()));
			
			return tag;
		}
	};
	
	/**
	 * Enumerates the types of the world entry.
	 */
	public enum Type {
		NONE, SAVE, SERVER
	}
	
	public record Singleplayer() {
		/** {@code NbtLoader} for loading and saving {@code Singleplayer} objects from/to NBT data. */
		public static final NbtLoader<Singleplayer, CompoundTag> loader = new NbtLoader<>() {
			
			@Override
			public Singleplayer load(CompoundTag tag) throws IllegalNbtException {
				try {
					return null;
				} catch (Exception e) {
					return null;
				}
			}
			
			@Override
			public CompoundTag save(Singleplayer object) {
				CompoundTag tag = new CompoundTag();
				
				if (object == null) {
					return tag;
				}
				
				return tag;
			}
		};
	}
	
	/**
	 * Represents information about a Minecraft multiplayer, including its host and port.
	 */
	public record Multiplayer(String host, int port) {
		/** {@code NbtLoader} for loading and saving {@code Multiplayer} objects from/to NBT data. */
		public static final NbtLoader<Multiplayer, CompoundTag> loader = new NbtLoader<>() {
			
			/**
			 * Loads a {@code Multiplayer} object from an NBT compound tag.
			 *
			 * @param tag The NBT compound tag representing the {@code Multiplayer} object.
			 * @return The loaded {@code Multiplayer} object, or null if an error occurs.
			 */
			@Override
			public Multiplayer load(CompoundTag tag) throws IllegalNbtException {
				try {
					String host = tag.getString("host").getValue();
					int port = tag.getInt("port").getValue();
					
					return of(host, port);
				} catch (Exception e) {
					return null;
				}
			}
			
			/**
			 * Saves a {@code Multiplayer} object to an NBT compound tag.
			 *
			 * @param object The {@code Multiplayer} object to be saved.
			 * @return The NBT compound tag representing the {@code Multiplayer} object.
			 */
			@Override
			public CompoundTag save(Multiplayer object) {
				CompoundTag tag = new CompoundTag();
				
				if (object == null) {
					return tag;
				}
				
				tag.putString("host", object.host);
				tag.putInt("port", object.port);
				
				return tag;
			}
		};
		
		/**
		 * Constructs a {@code Multiplayer} object with the specified host and default port (25565).
		 *
		 * @param host The host of the Minecraft multiplayer.
		 * @return A new {@code Multiplayer} object.
		 */
		public static Multiplayer of(String host) {
			return of(host, 25565);
		}
		
		/**
		 * Constructs a {@code Multiplayer} object with the specified host and port.
		 *
		 * @param host The host of the Minecraft multiplayer.
		 * @param port The port of the Minecraft multiplayer.
		 * @return A new {@code Multiplayer} object.
		 * @throws IndexOutOfBoundsException If the port is not within the valid range [0, 65535].
		 */
		public static Multiplayer of(String host, int port) {
			if (port < 0 || port > 65535) {
				throw new IndexOutOfBoundsException("Port must be between 0 and 65535");
			}
			
			return new Multiplayer(host, port);
		}
		
		/**
		 * Returns a string representation of the {@code Multiplayer} object in the format {@code "host:port"}.
		 *
		 * @return The string representation of the {@code Multiplayer} object.
		 */
		@Override
		public String toString() {
			return host + ':' + port;
		}
	}
}