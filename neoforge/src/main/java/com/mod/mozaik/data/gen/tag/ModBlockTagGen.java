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
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class ModBlockTagGen extends BlockTagsProvider {

	public ModBlockTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, Constants.MOD_ID);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		ModBlocks.MORTARS.forEach(supplier -> {
			ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, supplier.id());
			this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(key);
			this.tag(ModTags.Blocks.MORTARS).add(key);
		});
	}
}
