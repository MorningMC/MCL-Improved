package minecraft.morningmc.mcli.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * An annotation to mark a class as a launcher process.
 */
@Target({ ElementType.TYPE })
public @interface LauncherProcess {
	
	/**
	 * The category of the launcher process.
	 *
	 * @return The category of the launcher process.
	 */
	String value();
}
