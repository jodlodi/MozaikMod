package com.mod.mozaik.data.gen;

import com.mod.mozaik.data.gen.loot.ModArchaeologyLootGen;
import com.mod.mozaik.data.gen.loot.ModBlockLootGen;
import com.mod.mozaik.data.gen.loot.ModChestLootGen;
import com.mod.mozaik.reg.ModLootTables;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModLootGen extends LootTableProvider {
	public ModLootGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, ModLootTables.allBuiltin(), List.of(
				new SubProviderEntry(ModBlockLootGen::new, LootContextParamSets.BLOCK),
				new SubProviderEntry(ModChestLootGen::new, LootContextParamSets.CHEST),
				new SubProviderEntry(ModArchaeologyLootGen::new, LootContextParamSets.CHEST)
		), provider);
	}
}
