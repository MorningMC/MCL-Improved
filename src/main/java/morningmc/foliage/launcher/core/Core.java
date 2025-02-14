package morningmc.foliage.launcher.core;

public interface Core extends Runnable {

    String getCoreID();

    void load(Cores cores);
}
