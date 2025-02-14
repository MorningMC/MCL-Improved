package morningmc.foliage.launcher.startup;

import morningmc.foliage.launcher.core.*;

import java.util.List;

public class Startup {
    private Cores cores = new Cores();
    private List<String> coreClassNames = List.of(
            "morningmc.foliage.launcher.core.Main",
            "morningmc.foliage.launcher.core.UI");

    public static void main(String[] args) {
        try {
            new Startup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Startup() throws Exception {
        cores.loadCores(coreClassNames);
        cores.runCores();
    }
}
