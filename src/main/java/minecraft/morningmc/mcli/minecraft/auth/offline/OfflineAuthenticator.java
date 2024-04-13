package minecraft.morningmc.mcli.minecraft.auth.offline;

import minecraft.morningmc.mcli.minecraft.auth.Account;
import minecraft.morningmc.mcli.minecraft.auth.Authenticator;
import minecraft.morningmc.mcli.utils.exceptions.AuthenticationException;

import java.util.*;

public class OfflineAuthenticator implements Authenticator {
	String username;
	UUID uuid;
	
	public OfflineAuthenticator(String username) {
		this(username, UUID.randomUUID());
	}
	
	public OfflineAuthenticator(String username, UUID uuid) {
		if (username.isEmpty()) {
			throw new IllegalArgumentException("username cannot be empty");
		}
		
		this.username = Objects.requireNonNull(username);
		this.uuid = Objects.requireNonNull(uuid);
	}
	
	@Override
	public Account auth() throws AuthenticationException {
		return new Account(username, uuid.toString(), uuid, Map.of(), "legacy", "");
	}
}
