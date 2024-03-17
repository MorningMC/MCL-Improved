package minecraft.morningmc.mcli.utils.annotations;

import java.lang.annotation.*;

/**
 * An annotation to mark a class as a collection of objects.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.CLASS)
public @interface ObjectCollection {
}
