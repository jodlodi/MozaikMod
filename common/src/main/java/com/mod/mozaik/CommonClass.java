package com.mod.mozaik;

import com.mod.mozaik.reg.*;

public class CommonClass {
    public static void init(boolean fabric) {
        ModBlocks.init();
        ModItems.init();
        if (fabric) ModBlockEntities.init();
        ModMenus.init();
        ModTabs.init();
        ModRegistries.init();
        ModShardMaterials.init();
        ModDataComponents.init();
    }
}