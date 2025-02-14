package morningmc.foliage.java;

import morningmc.foliage.util.Platform;

import java.io.File;
import java.util.Objects;

public record JavaRuntime(File javaRuntime) {
    public JavaRuntime {
        Objects.requireNonNull(javaRuntime);
    }

    public static JavaRuntime current() {
        return new JavaRuntime(getCurrentJavaPath());
    }

    public static File getCurrentJavaPath() {
        return new File(System.getProperty("java.home"), "bin/java" + (Platform.CURRENT == Platform.WINDOWS ? ".exe" : ""));
    }

    @Override
    public String toString() {
        return javaRuntime.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof JavaRuntime) {
            JavaRuntime another = (JavaRuntime) obj;
            return javaRuntime.equals(another.javaRuntime);
        }
        return false;
    }

}
