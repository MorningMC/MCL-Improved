package minecraft.morningmc.mcli.minecraft.auth;

import minecraft.morningmc.mcli.utils.annotations.ObjectCollection;
import minecraft.morningmc.mcli.utils.annotations.StaticClass;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.functions.ExceptionUtils;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;
import minecraft.morningmc.mcli.utils.interfaces.UniqueObject;

import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.collection.ListTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.*;

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
	private static final Logger logger = LogManager.getLogger();
	
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
	
	@ObjectCollection
	@StaticClass
	public static class Collection {
		/** {@link NbtLoader} for loading and saving {@link Collection} objects from/to NBT data. */
		public static final NbtLoader<Void, ListTag<CompoundTag>> loader = new NbtLoader<>() {
			
			/**
			 * Loads a {@link Collection} object from a list of NBT compound tags.
			 *
			 * @param tag The list of NBT compound tags representing accounts.
			 * @return {@code null}.
			 */
			@Override
			public Void load(ListTag<CompoundTag> tag) {
				init(tag.getValue().stream()
						     .flatMap(subTag -> {
							     try {
								     return Stream.of(Account.loader.load(subTag));
							     } catch (IllegalNbtException e) {
								     logger.warn("Failed to load account from NBT: {}", ExceptionUtils.getMessages(e));
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
				
				accounts.values().stream()
						.map(Account.loader::save)
						.forEach(tag::add);
				
				return tag;
			}
		};
		
		public static Collection instance = null;
		
		private static final Map<UUID, Account> accounts = new HashMap<>();
		
		/**
		 * Initializes the {@link Collection} with the given profiles.
		 *
		 * @param accounts The initial set of accounts for the collection.
		 */
		public static void init(java.util.Collection<Account> accounts) {
			if (instance != null) {
				throw new IllegalStateException("AccountCollection already initialized");
			}
	
			instance = new Collection();
			for (Account account : accounts) {
				add(account);
			}
		}
		
		/**
		 * Gets the set of accounts in the collection.
		 *
		 * @return The set of accounts.
		 */
		public static java.util.Collection<Account> get() {
			return accounts.values();
		}
		
		/**
		 * Adds an account to the collection.
		 *
		 * @param account The profile to be added.
		 */
		public static void add(Account account) {
			accounts.put(account.identifier(), account);
		}
		
		/**
		 * Removes an account from the collection.
		 *
		 * @param account The account to be removed.
		 */
		public static void remove(Account account) {
			accounts.remove(account.identifier());
		}
		
		/**
		 * Resolves an account by name from the collection.
		 *
		 * @param uuid The UUID associated with the account to be resolved.
		 * @return The resolved account, or {@code null} if not found.
		 */
		public static Account resolve(UUID uuid) {
			return accounts.get(uuid);
		}
	}
}