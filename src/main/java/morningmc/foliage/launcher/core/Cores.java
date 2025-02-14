package morningmc.foliage.launcher.core;

import java.util.List;
import java.util.Map;

public class Cores {
    Map<String, Core> loadedCores = new java.util.HashMap<>(Map.of());

    public void loadCores(List<String> coreClassNames) throws Exception {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        for (String coreClassName : coreClassNames) {
            Core core = (Core)classLoader.loadClass(coreClassName).getDeclaredConstructor().newInstance();

            core.load(this);
            System.out.printf("Loaded core %s%n", core.getCoreID());
            loadedCores.put(core.getCoreID(), core);
        }
    }

    public Core getCore(String coreID) {
        return loadedCores.get(coreID);
    }

    public Map<String, Core> getLoadedCores() {
        return loadedCores;
    }

    public Map<String, Thread> runCores() {
        Map<String, Thread> threads = new java.util.HashMap<>(Map.of());
        for (Core core : loadedCores.values()) {
            Thread thread = startThread(core.getCoreID(), core);
            System.out.printf("Core %s is running%n", core.getCoreID());
            threads.put(core.getCoreID(), thread);
        }
        return threads;
    }

    public static Thread startThread(String name, Runnable task, boolean daemon) {
        Thread thread = new Thread(task, name);
        thread.setDaemon(daemon);
        thread.start();
        return thread;
    }

    public static Thread startThread(String name, Runnable task) {
        Thread thread = new Thread(task, name);
        thread.start();
        return thread;
    }
}
