package morningmc.foliage.minecraft.auth.offline;

import morningmc.foliage.minecraft.auth.AuthInfo;
import morningmc.foliage.minecraft.auth.Authenticator;
import morningmc.foliage.util.exceptions.AuthenticationException;

public class OfflineAuth implements Authenticator {
    @Override
    public AuthInfo auth() throws AuthenticationException {
        return null;
    }
}
