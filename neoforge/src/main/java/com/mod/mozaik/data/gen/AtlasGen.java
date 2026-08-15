package com.mod.mozaik.data.gen;

import com.mod.mozaik.Constants;
import com.mod.mozaik.polyomino.TesseraMaterial;
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
		for (TesseraMaterial material : TesseraMaterial.values()) {
			for (int i = 0; i < material.getSpriteSheets().size(); i++) {
				this.atlas(AtlasIds.GUI).addSource(new SingleFile(Constants.prefix("block/mural/" + material.getSerializedName() + "/gui_" +  i), Optional.of(Constants.prefix( material.getSerializedName() + "/" +  i))));
			}
		}
	}
}
