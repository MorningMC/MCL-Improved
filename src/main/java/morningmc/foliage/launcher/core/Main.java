package morningmc.foliage.launcher.core;

import org.to2mbn.jmccc.auth.OfflineAuthenticator;
import org.to2mbn.jmccc.launch.Launcher;
import org.to2mbn.jmccc.launch.LauncherBuilder;
import org.to2mbn.jmccc.option.LaunchOption;
import org.to2mbn.jmccc.option.MinecraftDirectory;

public class Main implements Core {
    private final String coreID = "foliage";
    private Cores cores;

    @Override
    public void load(Cores cores) {
        this.cores = cores;
    }

    @Override
    public void run() {

    }

    @Override
    public String getCoreID() {
        return coreID;
    }

    private Process launch() {
        Launcher launcher = LauncherBuilder.create().printDebugCommandline(true).nativeFastCheck(true).build();
        try {
            LaunchOption option = new LaunchOption("1.11.2", new OfflineAuthenticator("Player"), new MinecraftDirectory(".minecraft"));
        } catch (Exception e) {

        }
    }
}
