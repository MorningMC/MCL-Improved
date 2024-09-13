package minecraft.morningmc.mcli.minecraft.auth;

import minecraft.morningmc.mcli.utils.exceptions.AuthenticationException;

/**
 * An interface representing an authenticator used for authenticating Minecraft accounts.
 * Implementations of this interface are responsible for authenticating users and returning their account information.
 */
public interface Authenticator {
	
	/**
	 * Authenticates a Minecraft account and returns the corresponding account information.
	 *
	 * @return the authenticated Minecraft account.
	 * @throws AuthenticationException if authentication fails due to invalid credentials or other authentication issues.
	 */
	Account auth() throws AuthenticationException;
}