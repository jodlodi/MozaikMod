package com.mod.mozaik.data.gen.tag;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class ModBlockTagGen extends BlockTagsProvider {

	public ModBlockTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
		super(output, lookupProvider, modId);
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {

	}
}
