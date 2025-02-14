package morningmc.foliage.minecraft.launch;

import org.to2mbn.jmccc.launch.Launcher;
import org.to2mbn.jmccc.launch.LauncherBuilder;

public class Launch {
    Launcher launcher = LauncherBuilder.create().printDebugCommandline(true).nativeFastCheck(true).build();


}
