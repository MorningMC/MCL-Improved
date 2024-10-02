package minecraft.morningmc.mcli.utils.functions;

import minecraft.morningmc.mcli.utils.annotations.StaticClass;

@StaticClass
public class ExceptionUtils {
	
	public static String getMessages(Throwable e) {
		return e.toString() + (e.getCause() != null ? " (Caused by: " + getMessages(e.getCause()) + ")" : "");
	}
}
