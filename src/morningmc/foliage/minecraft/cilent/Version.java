package morningmc.foliage.minecraft.cilent;

import java.io.File;
import java.nio.file.Path;

public class Version {
    String name;
    MinecraftDirectory mcdir;
    File path;
    File json, jar;
    File nativesDir;
    String classPath;
    String type, indexName, mainClass;

    public Version(String name, MinecraftDirectory mcdir) {
        this.name = name;
        this.mcdir = mcdir;
        path = new File(this.mcdir.versionDir, "name");
        json = new File(path, name + ".json");
        jar = new File(path, name + ".jar");
        nativesDir = new File(path, name + "-natives");

        validJson();
    }

    public void validJson() {
        classPath = "\"";

        classPath += jar.toString() + "\"";
    }

    public String getName() { return name; }

    public File getJar() { return jar; }

    public File getJson() { return json; }

    public String getMainClass() { return mainClass; }

    public MinecraftDirectory getMcdir() { return mcdir; }

    public String getType() { return type; }

    @Override
    public String toString() { return name; }
}
