package morningmc.foliage.minecraft.cilent;

import morningmc.foliage.minecraft.launch.options.LaunchOptions;

import java.io.File;

public record Profile(String name,
                      Version version,
                      File gameDir,
                      LaunchOptions options,
                      boolean useCustomOptions) {
}
