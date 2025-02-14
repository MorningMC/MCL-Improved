package morningmc.foliage.launcher.startup;

import morningmc.foliage.launcher.main.*;
import morningmc.foliage.launcher.LauncherInfo;

public class Startup {
    public static void main(String[] args) {
        try {
            Main main = new Main();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static Thread startThread(String name, boolean daemon, Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setName(name);
        t.setDaemon(daemon);
        t.start();
        return t;
    }
}
