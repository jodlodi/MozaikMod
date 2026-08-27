package com.mod.mozaik;

import com.mod.mozaik.platform.NeoForgeRegistryHelper;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.DeferredRegister;

@Mod(Constants.MOD_ID)
public class MozaikMod {

    public MozaikMod(IEventBus bus) {
        CommonClass.init(false);

        NeoForgeRegistryHelper.BLOCKS.register(bus);
        NeoForgeRegistryHelper.BLOCK_ENTITY_TYPES.register(bus);
        NeoForgeRegistryHelper.ENTITY_TYPES.register(bus);
        NeoForgeRegistryHelper.ITEMS.register(bus);
        NeoForgeRegistryHelper.SOUND_EVENTS.register(bus);
        NeoForgeRegistryHelper.PARTICLE_TYPES.register(bus);
        NeoForgeRegistryHelper.TABS.register(bus);
        NeoForgeRegistryHelper.GAME_EVENTS.register(bus);
        NeoForgeRegistryHelper.MOB_EFFECTS.register(bus);
        NeoForgeRegistryHelper.SENSOR_TYPES.register(bus);
        NeoForgeRegistryHelper.ACTIVITIES.register(bus);
        NeoForgeRegistryHelper.MEMORY_MODULE_TYPES.register(bus);
        NeoForgeRegistryHelper.MENU_TYPES.register(bus);
        NeoForgeRegistryHelper.SHARD_MATERIALS.register(bus);
        NeoForgeRegistryHelper.DATA_COMPONENTS.register(bus);
        NeoForgeRegistryHelper.STRUCTURE_PIECE_TYPES.register(bus);
        NeoForgeRegistryHelper.STRUCTURE_TYPES.register(bus);
    }
}