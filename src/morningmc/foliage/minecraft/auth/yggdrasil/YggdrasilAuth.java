package morningmc.foliage.minecraft.auth.yggdrasil;

import morningmc.foliage.minecraft.auth.AuthInfo;
import morningmc.foliage.minecraft.auth.Authenticator;
import morningmc.foliage.util.exceptions.AuthenticationException;

import java.net.URL;

public class YggdrasilAuth implements Authenticator {
    String name;
    URL authServer;

    public YggdrasilAuth(String name, URL authServer) {
        this.name = name;
        this.authServer = authServer;
    }

    @Override
    public AuthInfo auth() throws AuthenticationException {
        return null;
    }
}
