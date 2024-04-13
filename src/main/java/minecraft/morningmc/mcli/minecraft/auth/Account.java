package minecraft.morningmc.mcli.minecraft.auth;

import java.util.*;

/**
 * Represents a Minecraft account.
 *
 * @param username The username of the account.
 * @param token The access token of the account.
 * @param uuid The UUID of the login.
 * @param properties The properties of the account.
 * @param userType The type of the login.
 * @param xboxUserId The Xbox User ID(XUID).
 */
public record Account(String username,
                      String token,
                      UUID uuid,
                      Map<String, String> properties,
                      String userType,
                      String xboxUserId) {
}
