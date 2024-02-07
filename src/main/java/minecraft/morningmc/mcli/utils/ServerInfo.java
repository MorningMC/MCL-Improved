package minecraft.morningmc.mcli.utils;

import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;

/**
 * Represents information about a Minecraft server, including its host and port.
 */
public record ServerInfo(String host, int port) {
	/** NbtLoader for loading and saving ServerInfo objects from/to NBT data. */
	public static final NbtLoader<ServerInfo, CompoundTag> LOADER = new NbtLoader<>() {
		
		/**
		 * Loads a ServerInfo object from an NBT compound tag.
		 *
		 * @param tag The NBT compound tag representing the ServerInfo object.
		 * @return The loaded ServerInfo object.
		 * @throws IllegalNbtException If the NBT data is invalid or missing required information.
		 */
		@Override
		public ServerInfo loadFromNbt(CompoundTag tag) throws IllegalNbtException {
			try {
				String host = tag.getString("host").getValue();
				int port = tag.getInt("port").getValue();
				
				return create(host, port);
			} catch (Exception e) {
				return null;
			}
		}
		
		/**
		 * Saves a ServerInfo object to an NBT compound tag.
		 *
		 * @param object The ServerInfo object to be saved.
		 * @return The NBT compound tag representing the ServerInfo object.
		 */
		@Override
		public CompoundTag saveToNbt(ServerInfo object) {
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
	 * Creates a ServerInfo object with the specified host and default port (25565).
	 *
	 * @param host The host of the Minecraft server.
	 * @return The created ServerInfo object.
	 */
	private static ServerInfo create(String host) {
		return create(host, 25565);
	}
	
	/**
	 * Creates a ServerInfo object with the specified host and port.
	 *
	 * @param host The host of the Minecraft server.
	 * @param port The port of the Minecraft server.
	 * @return The created ServerInfo object.
	 * @throws IndexOutOfBoundsException If the port is not within the valid range [0, 65535].
	 */
	public static ServerInfo create(String host, int port) {
		if (port < 0 || port > 65535) {
			throw new IndexOutOfBoundsException("Port must be between 0 and 65535");
		}
		
		return new ServerInfo(host, port);
	}
	
	/**
	 * Returns a string representation of the ServerInfo object in the format "host:port".
	 *
	 * @return The string representation of the ServerInfo object.
	 */
	@Override
	public String toString() {
		return host + ':' + port;
	}
}
