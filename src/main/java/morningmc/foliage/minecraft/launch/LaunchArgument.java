package morningmc.foliage.minecraft.launch;

import morningmc.foliage.minecraft.launch.options.LaunchOptions;

public class LaunchArgument {
    private final LaunchOptions options;

    public LaunchArgument(LaunchOptions options) {
        this.options = options;
    }

    public String[] generateCommandLine() {

    }

    public LaunchOptions getOptions() {
        return options;
    }
}
