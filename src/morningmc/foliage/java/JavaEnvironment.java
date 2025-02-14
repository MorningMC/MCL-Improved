package morningmc.foliage.java;

import morningmc.foliage.minecraft.launch.options.LaunchOption;

public class JavaEnvironment {
    JavaRuntime runtime;
    int maxMemory = 3096;
    int minMemory;

    public JavaEnvironment(JavaRuntime runtime) {
        this.runtime = runtime;
    }

    public JavaRuntime getRuntime() {
        return runtime;
    }

    public int getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(int maxMemory) {
        if (maxMemory < 0) {
            throw new IllegalArgumentException("maxMemory<0");
        }

        this.maxMemory = maxMemory;
    }

    public int getMinMemory() { return minMemory; }

    public void setMinMemory(int minMemory) {
        if (minMemory < 0) {
            throw new IllegalArgumentException("minMemory<0");
        }

        this.minMemory = minMemory;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof JavaEnvironment) {
            JavaEnvironment another = (JavaEnvironment) obj;
            return runtime.equals(another.getRuntime());
        }
        return false;
    }
}
