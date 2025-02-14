package morningmc.foliage.minecraft.cilent;

import morningmc.foliage.minecraft.cilent.libraries.Asset;
import morningmc.foliage.minecraft.cilent.libraries.Library;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class MinecraftDirectory {
    File directory;
    List<Version> versions;
    File versionDir, libraryDir, assetsDir;

    public MinecraftDirectory(File directory) {
        this.directory = directory;
        versionDir = new File(this.directory, "versions");
        libraryDir = new File(this.directory, "libraries");
        assetsDir = new File(this.directory, "assets");

        searchVersions();
    }

    public void searchVersions() {
        File[] versionList = versionDir.listFiles();
        if (versionList != null) {
            for (File version : versionList) {
                if (version.isDirectory()) {
                    Version v = new Version(version.getName(), this);
                    versions.add(v);
                }
            }
        }
    }

    public List<Version> listVersions() {
        searchVersions();
        return versions;
    }

    public Version findVersion(String name) {
        for(Version version : listVersions()) {
            if (Objects.equals(version.name, name)) {
                return version;
            }
        }
        return null;
    }

    public File getDirectory() {
        return directory;
    }

    public File getLibraryDir() {
        return libraryDir;
    }

    public File getAssetsDir() {
        return assetsDir;
    }

    public File getVersionDir() {
        return versionDir;
    }

    public File getLibraryFile(Library library) {
        return new File(getLibraryDir(), library.getPath());
    }

    public File getAssetFile(Asset asset) {
        return new File(getAssetsDir(), asset.getPath());
    }
}
