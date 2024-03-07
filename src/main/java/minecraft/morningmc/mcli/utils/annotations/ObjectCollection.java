package minecraft.morningmc.mcli.utils.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * An annotation to mark a class as a collection of objects.
 */
@Target({ ElementType.TYPE })
public @interface ObjectCollection {
}
