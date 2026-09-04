package com.mod.mozaik.data.gen;

import com.mod.mozaik.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;

import java.util.concurrent.CompletableFuture;

public class AtlasGen extends SpriteSourceProvider {
	public AtlasGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper helper) {
		super(output, lookupProvider, Constants.MOD_ID, helper);
	}

	@Override
	protected void gather() {/*
		NeoForgeRegistryHelper.SHARD_MATERIALS.getEntries().forEach(holder -> {
			this.atlas(AtlasIds.GUI).addSource(
					new SingleFile(Constants.prefix("item/" + holder.getId().getPath() + "_shards"), Optional.of(Constants.prefix(holder.getId().getPath() + "/shard")))
			);
			for (int i = 0; i < holder.get().shades(); i++) {
				this.atlas(AtlasIds.GUI).addSource(
						new SingleFile(Constants.prefix("gui/mozaik/" + holder.getId().getPath() + "/gui_" + i), Optional.of(Constants.prefix(holder.getId().getPath() + "/" + i)))
				);
			}
		});*/
	}
}
