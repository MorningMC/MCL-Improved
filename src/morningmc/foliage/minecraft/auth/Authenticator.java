package morningmc.foliage.minecraft.auth;

import morningmc.foliage.util.exceptions.AuthenticationException;

public interface Authenticator {
    AuthInfo auth() throws AuthenticationException;
}
