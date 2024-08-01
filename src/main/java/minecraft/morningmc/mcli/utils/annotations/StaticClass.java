package minecraft.morningmc.mcli.utils.annotations;

import java.lang.annotation.*;

/**
 * An annotation to mark a class as a static class. This annotation is used to indicate that a class is a static class,
 * which means it does not have any method that requires instantiation of the class (except {@link Runnable} implementation).
 * Static classes are commonly used for utility or helper classes that do not require instantiation.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.CLASS)
public @interface StaticClass {
}
