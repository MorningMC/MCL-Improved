package minecraft.morningmc.mcli.minecraft.auth;

import minecraft.morningmc.mcli.utils.annotations.ObjectCollection;
import minecraft.morningmc.mcli.utils.annotations.StaticClass;
import minecraft.morningmc.mcli.utils.exceptions.IllegalNbtException;
import minecraft.morningmc.mcli.utils.interfaces.NbtLoader;

import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.collection.ListTag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.Collection;
import java.util.stream.*;

@ObjectCollection
@StaticClass
public class AccountCollection {
	private static final Logger logger = LogManager.getLogger();
	
	/** {@link NbtLoader} for loading and saving {@link AccountCollection} objects from/to NBT data. */
	public static final NbtLoader<AccountCollection, ListTag<CompoundTag>> loader = new NbtLoader<>() {
		
		/**
		 * Loads a {@link AccountCollection} object from a list of NBT compound tags.
		 *
		 * @param tag The list of NBT compound tags representing accounts.
		 * @return The loaded {@link AccountCollection} object.
		 */
		@Override
		public AccountCollection load(ListTag<CompoundTag> tag) {
			init(tag.getValue().stream()
					     .flatMap(subTag -> {
						     try {
							     return Stream.of(Account.loader.load(subTag));
						     } catch (IllegalNbtException e) {
							     logger.warn("Failed to load account from NBT: {}", e.getMessage());
							     return Stream.empty();
						     }
					     })
					     .collect(Collectors.toSet()));
			return instance;
		}
		
		/**
		 * Saves a {@link AccountCollection} object to a list of NBT compound tags.
		 *
		 * @param object The {@link AccountCollection} object to be saved.
		 * @return The list of NBT compound tags representing profiles.
		 */
		@Override
		public ListTag<CompoundTag> save(AccountCollection object) {
			ListTag<CompoundTag> tag = new ListTag<>();
			
			object.accounts.values().stream()
					.map(Account.loader::save)
					.forEach(tag::add);
			
			return tag;
		}
	};
	
	public static AccountCollection instance = null;
	
	private final Map<UUID, Account> accounts = new HashMap<>();
	
	/**
	 * Initializes the {@link AccountCollection} with the given profiles.
	 *
	 * @param accounts The initial set of accounts for the collection.
	 */
	public static void init(Collection<Account> accounts) {
		if (instance != null) {
			throw new IllegalStateException("AccountCollection already initialized");
		}

		instance = new AccountCollection();
		for (Account account : accounts) {
			add(account);
		}
	}
	
	/**
	 * Gets the set of accounts in the collection.
	 *
	 * @return The set of accounts.
	 */
	public static Collection<Account> get() {
		return instance.accounts.values();
	}
	
	/**
	 * Adds an account to the collection.
	 *
	 * @param account The profile to be added.
	 */
	public static void add(Account account) {
		instance.accounts.put(account.identifier(), account);
	}
	
	/**
	 * Removes an account from the collection.
	 *
	 * @param account The account to be removed.
	 */
	public static void remove(Account account) {
		instance.accounts.remove(account.identifier());
	}
	
	/**
	 * Resolves an account by name from the collection.
	 *
	 * @param uuid The UUID associated with the account to be resolved.
	 * @return The resolved account, or {@code null} if not found.
	 */
	public static Account resolve(UUID uuid) {
		return instance.accounts.get(uuid);
	}
}
