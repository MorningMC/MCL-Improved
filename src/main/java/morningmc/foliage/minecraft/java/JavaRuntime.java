package morningmc.foliage.minecraft.java;

import morningmc.foliage.util.Platform;

import java.io.File;
import java.util.Objects;

public record JavaRuntime(File runtime) {

    public JavaRuntime {
        Objects.requireNonNull(runtime);
    }

    public static JavaRuntime current() {
        return new JavaRuntime(getCurrentJavaPath());
    }

    public static File getCurrentJavaPath() {
        return new File(System.getProperty("java.home"), "bin/java" + (Platform.CURRENT == Platform.WINDOWS ? ".exe" : ""));
    }
}
