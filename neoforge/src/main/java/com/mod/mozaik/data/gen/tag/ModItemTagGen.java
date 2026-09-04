package com.mod.mozaik.data.gen.tag;

import com.mod.mozaik.items.MortarBlockItem;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.platform.NeoForgeRegistryHelper;
import com.mod.mozaik.reg.ModTags;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModItemTagGen extends ItemTagsProvider {

	public ModItemTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ModBlockTagGen blockTagGen) {
		super(output, lookupProvider, blockTagGen.contentsGetter());
	}

	@Override
	protected void addTags(HolderLookup.Provider provider) {
		NeoForgeRegistryHelper.ITEMS.getEntries().forEach(itemDeferredHolder -> {
			if (itemDeferredHolder.get() instanceof ShardItem shardItem) {
				this.tag(ModTags.Items.SHARDS).add(shardItem);
			} else if (itemDeferredHolder.get() instanceof MortarBlockItem mortarBlockItem) {
				this.tag(ModTags.Items.MORTARS).add(mortarBlockItem);
			}
		});
	}
}
