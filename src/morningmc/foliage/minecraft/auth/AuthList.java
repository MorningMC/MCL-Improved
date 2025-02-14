package morningmc.foliage.minecraft.auth;

import morningmc.foliage.minecraft.auth.microsoft.MicrosoftAuth;
import morningmc.foliage.minecraft.auth.offline.OfflineAuth;
import morningmc.foliage.minecraft.auth.yggdrasil.YggdrasilAuth;

import java.util.List;

public class AuthList {
    public MicrosoftAuth microsoftAuth = new MicrosoftAuth();
    public OfflineAuth offlineAuth = new OfflineAuth();
    public List<YggdrasilAuth> yggdrasilAuths;

    public void addAuthServer(YggdrasilAuth authServer) {
        yggdrasilAuths.add(authServer);
    }
}
