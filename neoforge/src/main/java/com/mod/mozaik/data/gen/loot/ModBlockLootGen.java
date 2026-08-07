package com.mod.mozaik.data.gen.loot;

import com.mod.mozaik.platform.NeoForgeRegistryHelper;
import com.mod.mozaik.reg.ModBlocks;
import com.mod.mozaik.reg.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jspecify.annotations.NullMarked;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;
import java.util.stream.Collectors;

@NullMarked
@ParametersAreNonnullByDefault
public class ModBlockLootGen extends BlockLootSubProvider {

	public ModBlockLootGen(HolderLookup.Provider provider) {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
	}

	@Override
	protected void generate() {
		ModBlocks.MORTARS.forEach(supplier -> this.dropWhenSilkTouch(supplier.get()));
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return NeoForgeRegistryHelper.BLOCKS.getEntries().stream().map(DeferredHolder::value).collect(Collectors.toList());
	}
}
