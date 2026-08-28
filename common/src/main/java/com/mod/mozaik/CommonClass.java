package com.mod.mozaik;

import com.mod.mozaik.reg.*;
import com.mod.mozaik.structure.ModStructureTypes;
import com.mod.mozaik.structure.piece.ModStructurePieces;

public class CommonClass {
    public static void init(boolean fabric) {
        ModBlocks.init();
        ModItems.init();
        if (fabric) ModBlockEntities.init();
        ModMenus.init();
        ModTabs.init();
        ModSounds.init();
        ModRegistries.init();
        ModShardMaterials.init();
        ModDataComponents.init();
        ModStructurePieces.init();
        ModStructureTypes.init();
    }
}