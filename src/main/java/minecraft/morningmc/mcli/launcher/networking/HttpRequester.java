package minecraft.morningmc.mcli.launcher.networking;

import minecraft.morningmc.mcli.utils.annotations.StaticClass;

@StaticClass
public class HttpRequester {
	
	/**
	 * Enumerates different HTTP request methods.
	 */
	public enum Method {
		GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE
	}
}
