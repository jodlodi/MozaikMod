package com.mod.mozaik.data.gen.tag;

import com.mod.mozaik.Constants;
import com.mod.mozaik.items.MortarBlockItem;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.platform.NeoForgeRegistryHelper;
import com.mod.mozaik.reg.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModItemTagGen extends ItemTagsProvider {

	public ModItemTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, Constants.MOD_ID);
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
