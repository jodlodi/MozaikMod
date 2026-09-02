package com.mod.mozaik;

import com.mod.mozaik.platform.FabricNetworkHelper;
import com.mod.mozaik.util.FabricServerLifecycleHooks;
import net.fabricmc.api.ModInitializer;

public class MozaikMod implements ModInitializer {
    
    @Override
    public void onInitialize() {
        FabricServerLifecycleHooks.init();
        CommonClass.init(true);
        FabricNetworkHelper.commonPacketRegistration();
    }
}
