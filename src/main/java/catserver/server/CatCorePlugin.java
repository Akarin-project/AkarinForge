/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 */
package catserver.server;

import catserver.server.CatServer;
import catserver.server.remapper.NetworkTransformer;
import catserver.server.remapper.SideTransformer;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

public class CatCorePlugin
implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        String[] arrstring;
        if (!CatServer.isDev()) {
            String[] arrstring2 = new String[2];
            arrstring2[0] = NetworkTransformer.class.getCanonicalName();
            arrstring = arrstring2;
            arrstring2[1] = SideTransformer.class.getCanonicalName();
        } else {
            arrstring = null;
        }
        return arrstring;
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}

