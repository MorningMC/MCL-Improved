package minecraft.morningmc.mcli.launcher.networking;

/**
 * Enumerates the possible server statuses.
 * This class is used to represent the different states a server can be in.
 */
public enum ServerStatus {
	/** Service available. */
	GOOD,
	
	/** Something wrong with the server. */
	BAD,
	
	/** Service unavailable. */
	OFFLINE,
	
	/** Cannot get the server status. */
	UNKNOWN
}
