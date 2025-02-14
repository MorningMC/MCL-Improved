package morningmc.foliage.minecraft.cilent;

import java.io.File;

public class Profile {
    public String name;
    public Version version;
    public File gamedir;

    public Profile(String name, Version version, File gamedir) {
        this.name = name;
        this.version = version;
        this.gamedir = gamedir;
    }
}
