package morningmc.foliage.minecraft.launch.options;

import morningmc.foliage.java.JavaEnvironment;
import morningmc.foliage.minecraft.cilent.Profile;

import java.io.File;
import java.util.*;

public class LaunchOption {
    public JavaEnvironment javaEnvironment;
    public Profile profile;
    public WindowSize windowSize;
    public ServerInfo serverInfo;
    public List<String> extraJvmArguments = new ArrayList<>();
    public List<String> extraMinecraftArguments = new ArrayList<>();
    public Map<String, String> commandlineVariables = new LinkedHashMap<>();
    public Set<File> extraClasspath = new LinkedHashSet<>();

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof LaunchOption) {
            LaunchOption another = (LaunchOption) obj;
            return Objects.equals(profile, another.profile) &&
                    Objects.equals(javaEnvironment, another.javaEnvironment) &&
                    Objects.equals(serverInfo, another.serverInfo) &&
                    Objects.equals(windowSize, another.windowSize) &&
                    Objects.equals(extraJvmArguments, another.extraJvmArguments) &&
                    Objects.equals(extraMinecraftArguments, another.extraMinecraftArguments) &&
                    Objects.equals(commandlineVariables, another.commandlineVariables) &&
                    Objects.equals(extraClasspath, another.extraClasspath);
        }
        return false;
    }
}
