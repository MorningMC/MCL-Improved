package minecraft.morningmc.mcli.minecraft.launch;

import minecraft.morningmc.mcli.minecraft.client.Profile;
import minecraft.morningmc.mcli.minecraft.java.JavaEnvironment;

public record LaunchOptions(JavaEnvironment environment,
                            Profile profile) {
}
