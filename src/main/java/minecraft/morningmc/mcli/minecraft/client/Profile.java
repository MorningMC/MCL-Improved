package minecraft.morningmc.mcli.minecraft.client;

import minecraft.morningmc.mcli.minecraft.launch.LaunchOptions;

public record Profile(Version version,
                      MinecraftDirectory gameDir,
                      boolean useCustomOptions,
                      LaunchOptions options) {
}
