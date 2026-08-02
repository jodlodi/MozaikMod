package com.mod.mozaik.data.gen;

import com.mod.mozaik.data.gen.loot.ModBlockLootGen;
import com.google.common.collect.Sets;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContextSource;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@NullMarked
public class ModLootGen extends LootTableProvider {

	public ModLootGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, Sets.newHashSet(), List.of(
				new SubProviderEntry(ModBlockLootGen::new, LootContextParamSets.BLOCK)
		), provider);
	}

	@Override
	protected void validate(WritableRegistry<LootTable> tables, ValidationContextSource validationContext, ProblemReporter.Collector problems) {

	}
}
