package com.mod.mozaik.data.gen;

import com.mod.mozaik.data.gen.loot.ModArchaeologyLootGen;
import com.mod.mozaik.data.gen.loot.ModChestLootGen;
import com.mod.mozaik.data.gen.loot.ModBlockLootGen;
import com.mod.mozaik.reg.ModLootTables;
import net.minecraft.core.registries.SingleRegistryBootstrap;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.packs.TradeRebalanceChestLoot;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Set;

@NullMarked
public class ModLootGen {
	public static void bootstrap(BootstrapContext<LootTable> context) {
		new LootTableProvider(ModLootTables.allBuiltin(), List.of(
				new LootTableProvider.SubProviderEntry(ModBlockLootGen::new, LootContextParamSets.BLOCK),
				new LootTableProvider.SubProviderEntry(ModChestLootGen::new, LootContextParamSets.CHEST),
				new LootTableProvider.SubProviderEntry(ModArchaeologyLootGen::new, LootContextParamSets.CHEST)
		)).run(context);
	}
}
