package morningmc.foliage.minecraft.launch;

import morningmc.foliage.minecraft.auth.AuthInfo;
import morningmc.foliage.minecraft.cilent.MinecraftDirectory;
import morningmc.foliage.minecraft.cilent.Version;
import morningmc.foliage.minecraft.cilent.libraries.Asset;
import morningmc.foliage.minecraft.cilent.libraries.Library;
import morningmc.foliage.minecraft.cilent.libraries.Native;
import morningmc.foliage.minecraft.cilent.parsing.Versions;
import morningmc.foliage.minecraft.launch.options.LaunchOption;
import morningmc.foliage.minecraft.launch.options.WindowSize;
import morningmc.foliage.launcher.listener.*;
import morningmc.foliage.util.Platform;
import morningmc.foliage.util.UUIDUtils;
import morningmc.foliage.util.exceptions.LaunchException;
import morningmc.foliage.util.file.FileOperation;
import morningmc.foliage.util.file.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static morningmc.foliage.launcher.startup.Startup.startThread;

public class Launch {
    private boolean nativeFastCheck = false;
    private boolean printDebugCommandline = false;
    private boolean useDaemonThreads = false;

    public Process launch(LaunchOption option) throws LaunchException {
        return launch(option, null);
    }

    public Process launch(LaunchOption option, ProcessListener listener) throws LaunchException {
        return launch(generateLaunchArgs(option), listener);
    }

    private Process launch(LaunchArgument arg, ProcessListener listener) throws LaunchException {
        String[] commandline = arg.generateCommandline();
        if (printDebugCommandline) {
            printDebugCommandline(commandline);
        }

        ProcessBuilder processBuilder = new ProcessBuilder(commandline);
        processBuilder.directory(arg.getLaunchOption().profile.gamedir);

        Process process;
        try {
            process = processBuilder.start();
        } catch (SecurityException | IOException e) {
            throw new LaunchException("Launch failed: Couldn't start process", e);
        }

        if (listener == null) {
            startStreamPumps(process);
        } else {
            startStreamLoggers(process, listener, useDaemonThreads);
        }

        return process;
    }

    public void setNativeFastCheck(boolean nativeFastCheck) {
        this.nativeFastCheck = nativeFastCheck;
    }

    public void setPrintDebugCommandline(boolean printDebugCommandline) {
        this.printDebugCommandline = printDebugCommandline;
    }

    public void setUseDaemonThreads(boolean useDaemonThreads) {
        this.useDaemonThreads = useDaemonThreads;
    }

    public LaunchArgument generateLaunchArgs(LaunchOption option) throws LaunchException {
        Objects.requireNonNull(option);

        if (option.javaEnvironment == null) {
            throw new IllegalArgumentException("No JavaEnvironment is specified");
        }

        MinecraftDirectory mcdir = option.profile.version.getMcdir();
        Version version = option.profile.version;

        // check libraries
        Set<Library> missing = version.getMissingLibraries(mcdir);
        if (!missing.isEmpty()) {
            throw new MissingDependenciesException(missing);
        }

        Set<File> javaLibraries = new LinkedHashSet<>();
        File nativesDir = mcdir.getNatives(version);
        for (Library library : version.getLibraries()) {
            File libraryFile = mcdir.getLibraryFile(library);
            if (library instanceof Native) {
                try {
                    decompressZipWithExcludes(libraryFile, nativesDir, ((Native) library).getExtractExcludes());
                } catch (IOException e) {
                    throw new LaunchException("Couldn't uncompress " + libraryFile, e);
                }
            } else {
                javaLibraries.add(libraryFile);
            }
        }
        javaLibraries.add(version.getJar());
        javaLibraries.addAll(option.extraClasspath);

        if (version.isLegacy()) {
            try {
                buildLegacyAssets(mcdir, version);
            } catch (IOException e) {
                throw new LaunchException("Couldn't build virtual assets", e);
            }
        }

        AuthInfo auth = option.getAuthenticator().auth();

        Map<String, String> tokens = new HashMap<>();
        String token = auth.getToken();
        String assetsDir = (version.isLegacy() ? mcdir.getVirtualLegacyAssets() : mcdir.getAssetsDir()).getAbsolutePath();
        tokens.put("assets_root", assetsDir);
        tokens.put("game_assets", assetsDir);
        tokens.put("auth_access_token", token);
        tokens.put("auth_session", token);
        tokens.put("auth_player_name", auth.getUsername());
        tokens.put("auth_uuid", UUIDUtils.unsign(auth.getUUID()));
        tokens.put("user_type", auth.getUserType());
        tokens.put("user_properties", new JSONObject(auth.getProperties()).toString());
        tokens.put("version_name", version.getName());
        tokens.put("assets_index_name", version.getAssets());
        tokens.put("game_directory", option.profile.gamedir.getAbsolutePath());
        tokens.put("natives_directory", nativesDir.getAbsolutePath());
        tokens.put("library_directory", mcdir.getLibraryDir().getAbsolutePath());
        tokens.put("classpath_separator", Platform.getPathSeparator());
        tokens.put("auth_xuid", auth.getXboxUserId());

        String type = version.getType();
        if (type != null) {
            tokens.put("version_type", type);
        }

        WindowSize windowSize = option.windowSize;
        if (windowSize != null) {
            tokens.put("resolution_width", Integer.toString(windowSize.getWidth()));
            tokens.put("resolution_height", Integer.toString(windowSize.getHeight()));
        }

        return new LaunchArgument(option, tokens, javaLibraries, nativesDir);
    }

    private void buildLegacyAssets(MinecraftDirectory mcdir, Version version) throws IOException {
        Set<Asset> assets = Versions.resolveAssets(mcdir, version);
        if (assets != null)
            for (Asset asset : assets)
                FileOperation.copyFile(mcdir.getAsset(asset), mcdir.getVirtualAsset(asset));
    }

    private void printDebugCommandline(String[] commandline) {
        StringBuilder sb = new StringBuilder();
        sb.append("jmccc:\n");
        for (String arg : commandline) {
            sb.append(arg).append('\n');
        }
        System.err.println(sb);
    }

    public void decompressZipWithExcludes(File zip, File outputDir, Set<String> excludes) throws IOException {
        if (!outputDir.exists())
            outputDir.mkdirs();

        try (ZipInputStream in = new ZipInputStream(Files.newInputStream(zip.toPath()))) {
            ZipEntry entry;
            byte[] buf = null;

            while ((entry = in.getNextEntry()) != null) {
                boolean excluded = false; // true if the file is in excludes list
                if (excludes != null) {
                    for (String exclude : excludes) {
                        if (entry.getName().startsWith(exclude)) {
                            excluded = true;
                            break;
                        }
                    }
                }

                if (!excluded) {
                    // 1 unused byte for sentinel
                    if (buf == null || buf.length < entry.getSize() - 1) {
                        buf = new byte[(int) entry.getSize() + 1];
                    }
                    int len = 0;
                    int read;
                    // read the zipped data fully
                    while ((read = in.read(buf, len, buf.length - len)) != -1) {
                        if (read == 0) {
                            // reach the sentinel
                            throw new IOException("actual length and entry length mismatch");
                        }
                        len += read;
                    }

                    File outFile = new File(outputDir, entry.getName());
                    boolean match; // true if two files are the same
                    if (outFile.isFile() && outFile.length() == entry.getSize()) {
                        // same length, check the content
                        match = true;
                        if (!nativeFastCheck) {
                            try (InputStream targetin = new BufferedInputStream(Files.newInputStream(outFile.toPath()))) {
                                for (int i = 0; i < len; i++) {
                                    if (buf[i] != (byte) targetin.read()) {
                                        match = false;
                                        break;
                                    }
                                }
                            }
                        }
                    } else {
                        // different length
                        match = false;
                    }

                    if (!match) {
                        if (entry.isDirectory()) {
                            outFile.mkdir();//Fix extract directory as file
                        } else {
                            try (OutputStream out = Files.newOutputStream(outFile.toPath())) {
                                out.write(buf, 0, len);
                            }
                        }
                    }
                }
                in.closeEntry();
            }
        }
    }

    private void startStreamPumps(Process process) {
        startThread("stdout-pump", true, new StreamPump(process.getInputStream()));
        startThread("stderr-pump", true, new StreamPump(process.getErrorStream()));
    }

    private void startStreamLoggers(Process process, ProcessListener listener, boolean daemon) {
        startThread("stdout-logger", daemon, new StreamLogger(listener, false, process.getInputStream()));
        startThread("stderr-logger", daemon, new StreamLogger(listener, true, process.getErrorStream()));
        startThread("exit-waiter", daemon, new ExitWaiter(process, listener));
    }
}
