package com.mod.mozaik.data.gen.tag;

import com.mod.mozaik.Constants;
import com.mod.mozaik.reg.ModBlocks;
import com.mod.mozaik.reg.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModBlockTagGen extends BlockTagsProvider {

	public ModBlockTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, Constants.MOD_ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		ModBlocks.MORTARS.forEach(supplier -> {
			this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(supplier.get());
			this.tag(ModTags.Blocks.MORTARS).add(supplier.get());
		});
	}
}
