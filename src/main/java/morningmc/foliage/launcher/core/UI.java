package morningmc.foliage.launcher.core;

public class UI implements Core {
    private final String coreID = "foliage_ui";
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
}
