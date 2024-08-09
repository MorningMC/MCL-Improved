package minecraft.morningmc.mcli.minecraft.auth;

import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;
import minecraft.morningmc.mcli.utils.interfaces.UniqueObject;

import dev.dewy.nbt.tags.collection.CompoundTag;

import java.util.*;

/**
 * Represents a Minecraft account.
 *
 * @param username The username of the account.
 * @param token The access token of the account.
 * @param uuid The UUID of the login.
 * @param properties The properties of the account.
 * @param userType The type of the login.
 * @param xboxUserId The Xbox User ID (XUID).
 * @param identifier The unique identifier of the account.
 */
public record Account(String username,
                      String token,
                      UUID uuid,
                      Map<String, String> properties,
                      UserType userType,
                      String xboxUserId,
                      UUID identifier) implements UniqueObject {
	/** {@link NbtLoader} for loading and saving {@link Account} objects from/to NBT data. */
	public static final NbtLoader<Account, CompoundTag> loader = new NbtLoader<>() {

		@Override
		public Account load(CompoundTag tag) {
			return null;
		}
		
		@Override
		public CompoundTag save(Account account) {
			return null;
		}
	};
	
	/**
	 * Creates a new {@link Account} object with the specified attributes.
	 *
	 * @param username The username of the account.
	 * @param token The access token of the account.
	 * @param uuid The UUID of the login.
	 * @param properties The properties of the account.
	 * @param userType The type of the login.
	 * @param xboxUserId The Xbox User ID (XUID).
	 * @return A new {@link Account} object.
	 */
	public static Account of(String username, String token, UUID uuid, Map<String, String> properties, UserType userType, String xboxUserId) {
		return new Account(username, token, uuid, properties, userType, xboxUserId, UUID.randomUUID());
	}
	
	/**
	 * Enumerates the types of Minecraft account logins.
	 */
	public enum UserType {
		MICROSOFT("msa"), YGGDRASIL("mojang"), OFFLINE("legacy");
		
		public final String name;
		
		/**
		 * Creates a new {@link UserType} object with the specified name.
		 *
		 * @param name The name of the user type.
		 */
		UserType(String name) {
			this.name = name;
		}
	}
}