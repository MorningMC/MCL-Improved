package morningmc.foliage.minecraft.launch.options;

import morningmc.foliage.minecraft.cilent.Profile;
import morningmc.foliage.minecraft.java.JavaRuntime;

public record LaunchOptions(JavaRuntime runtime,
                            int maxMemory,
                            int minMemory,
                            Profile profile,
                            WindowSize windowSize) {
}
