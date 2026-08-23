package com.mod.mozaik.data.gen;

import com.mod.mozaik.Constants;
import com.mod.mozaik.platform.NeoForgeRegistryHelper;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.AtlasIds;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.data.SpriteSourceProvider;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class AtlasGen extends SpriteSourceProvider {
	public AtlasGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, Constants.MOD_ID);
	}

	@Override
	protected void gather() {
		NeoForgeRegistryHelper.SHARD_MATERIALS.getEntries().forEach(holder -> {
			this.atlas(AtlasIds.GUI).addSource(
					new SingleFile(Constants.prefix("item/" + holder.getId().getPath() + "_shards"), Optional.of(Constants.prefix(holder.getId().getPath() + "/shard")))
			);
			for (int i = 0; i < holder.get().shades(); i++) {
				this.atlas(AtlasIds.GUI).addSource(
						new SingleFile(Constants.prefix("gui/mozaik/" + holder.getId().getPath() + "/gui_" + i), Optional.of(Constants.prefix(holder.getId().getPath() + "/" + i)))
				);
			}
		});
	}
}
