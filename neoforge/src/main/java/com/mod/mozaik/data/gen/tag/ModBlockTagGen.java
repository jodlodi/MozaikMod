package com.mod.mozaik.data.gen.tag;

import com.mod.mozaik.Constants;
import com.mod.mozaik.reg.ModBlocks;
import com.mod.mozaik.reg.ModTags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModBlockTagGen extends BlockTagsProvider {

	public ModBlockTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper helper) {
		super(output, lookupProvider, Constants.MOD_ID, helper);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		ModBlocks.MORTARS.forEach(supplier -> {
			this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(supplier.get());
			this.tag(ModTags.Blocks.MORTARS).add(supplier.get());
		});
	}
}
