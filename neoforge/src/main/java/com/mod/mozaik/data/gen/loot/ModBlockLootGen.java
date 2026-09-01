package com.mod.mozaik.data.gen.loot;

import com.mod.mozaik.platform.NeoForgeRegistryHelper;
import com.mod.mozaik.reg.ModBlocks;
import com.mod.mozaik.reg.ModDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ModBlockLootGen extends BlockLootSubProvider {

	public ModBlockLootGen(HolderLookup.Provider provider) {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
	}

	@Override
	protected void generate() {
		ModBlocks.MORTARS.forEach(supplier -> this.createMosaic(supplier.get()));
	}

	protected void createMosaic(Block mosaic) {
		this.add(mosaic, LootTable.lootTable().withPool(this.applyExplosionCondition(mosaic, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(mosaic).apply(CopyComponentsFunction.copyComponentsFromBlockEntity(LootContextParams.BLOCK_ENTITY)
				.include(DataComponents.CUSTOM_NAME)
				.include(DataComponents.CONTAINER)
				.include(DataComponents.LOCK)
				.include(DataComponents.BLOCK_ENTITY_DATA)
				.include(ModDataComponents.MOZAIK.get())
				.include(ModDataComponents.AUTHOR.get())
				.include(ModDataComponents.SIGNED.get())
		)))));
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return NeoForgeRegistryHelper.BLOCKS.getEntries().stream().map(DeferredHolder::value).collect(Collectors.toList());
	}
}
