package morningmc.foliage.minecraft.launch;

import morningmc.foliage.minecraft.launch.options.LaunchOption;
import morningmc.foliage.util.Platform;
import morningmc.foliage.minecraft.cilent.Profile;

import java.io.File;
import java.util.*;

public class LaunchArgument {
    private final LaunchOption launchOption;
    private final File nativesPath;
    private final Set<File> libraries;
    private final Map<String, String> defaultVariables;

    public LaunchArgument(LaunchOption launchOption, Map<String, String> defaultVariables, Set<File> libraries, File nativesPath) {
        this.launchOption = launchOption;
        this.libraries = libraries;
        this.nativesPath = nativesPath;
        this.defaultVariables = defaultVariables;
    }

    public List<String> getJvmArguments() {
        List<String> args = new ArrayList<>();

        // min memory
        if (launchOption.javaEnvironment.getMinMemory() != 0) {
            args.add("-Xms" + launchOption.javaEnvironment.getMinMemory() + "M");
        }

        // max memory
        if (launchOption.javaEnvironment.getMaxMemory() != 0) {
            args.add("-Xmx" + launchOption.javaEnvironment.getMaxMemory() + "M");
        }

        // extra jvm arguments
        for (String arg : launchOption.extraJvmArguments) {
            if (arg != null) {
                args.add(arg);
            }
        }

        // libraries
        StringBuilder cpBuilder = new StringBuilder();
        for (File lib : libraries) {
            if (lib != null) {
                cpBuilder.append(lib.getAbsolutePath()).append(Platform.getPathSeparator());
            }
        }
        if (cpBuilder.length() > 0) {
            cpBuilder.deleteCharAt(cpBuilder.length() - 1); // to avoid the last unnecessary ':'
        }
        defaultVariables.put("classpath", cpBuilder.toString());

        // JVM arguments
        List<String> jvmArgs = launchOption.profile.version.getJvmArgs();
        if (jvmArgs.isEmpty()) {
            //Default JVM args
            jvmArgs.addAll(Arrays.asList("-Djava.library.path=${natives_directory}", "-cp", "${classpath}"));
        }
        args.addAll(getFormattedMinecraftArguments(jvmArgs));

        return args;
    }

    public List<String> getGameArguments() {

        // template arguments
        List<String> args = new ArrayList<>(getFormattedMinecraftArguments(launchOption.profile.version.getGameArgs()));

        // extra minecraft arguments
        for (String arg : launchOption.extraMinecraftArguments) {
            if (arg != null) {
                args.add(arg);
            }
        }

        // server
        if (launchOption.serverInfo != null && launchOption.serverInfo.getHost() != null && !launchOption.serverInfo.getHost().equals("")) {
            args.add("--server");
            args.add(launchOption.serverInfo.getHost());

            if (launchOption.serverInfo.getPort() > 0) {
                args.add("--port");
                args.add(String.valueOf(launchOption.serverInfo.getPort()));
            }
        }

        // window size settings
        if (launchOption.windowSize != null) {
            if (launchOption.windowSize.isFullScreen()) {
                args.add("--fullscreen");
            } else {
                if (launchOption.windowSize.getHeight() != 0) {
                    args.add("--height");
                    args.add(String.valueOf(launchOption.windowSize.getHeight()));
                }
                if (launchOption.windowSize.getWidth() != 0) {
                    args.add("--width");
                    args.add(String.valueOf(launchOption.windowSize.getWidth()));
                }
            }
        }

        return args;
    }

    public String[] generateCommandline() {
        List<String> args = new ArrayList<>();
        Profile profile = launchOption.profile;

        // java path
        args.add(launchOption.javaEnvironment.getRuntime().javaRuntime().getAbsolutePath());

        // jvm arguments
        args.addAll(getJvmArguments());

        // main class
        args.add(profile.version.getMainClass());

        // game arguments
        args.addAll(getGameArguments());

        return args.toArray(new String[0]);
    }

    private List<String> getFormattedMinecraftArguments(List<String> template) {
        Map<String, String> variables = new HashMap<>();
        variables.putAll(defaultVariables);
        variables.putAll(launchOption.commandlineVariables);

        List<String> args = new ArrayList<>();
        for (String arg : template) {
            for (Map.Entry<String, String> var : variables.entrySet()) {
                String k = var.getKey();
                String v = var.getValue();
                if (k != null && v != null) {
                    arg = arg.replace("${" + k + "}", v);
                }
            }
            args.add(arg);
        }
        return args;
    }

    public LaunchOption getLaunchOption() {
        return launchOption;
    }

    public File getNativesPath() {
        return nativesPath;
    }

    public Set<File> getLibraries() {
        return libraries;
    }

    public Map<String, String> getTokens() {
        return defaultVariables;
    }
}
