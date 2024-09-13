package minecraft.morningmc.mcli.utils.annotations;

import java.lang.annotation.*;

/**
 * An annotation to mark a class as a collection of objects.
 * An implementation of this class must be a static class.
 *
 * @see StaticClass
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.CLASS)
public @interface ObjectCollection {
}
