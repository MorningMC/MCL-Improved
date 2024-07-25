package minecraft.morningmc.mcli.utils.annotations;

import java.lang.annotation.*;

/**
 * An annotation to mark a class as a static class.
 * This annotation is used to indicate that a class is a static class,
 * which means it does not have an instance associated with it.
 * Static classes are commonly used for utility or helper classes that do not require instantiation.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.CLASS)
public @interface StaticClass {
}
