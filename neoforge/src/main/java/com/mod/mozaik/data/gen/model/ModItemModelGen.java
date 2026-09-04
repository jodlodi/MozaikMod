package com.mod.mozaik.data.gen.model;

import com.mod.mozaik.Constants;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.platform.NeoForgeRegistryHelper;
import com.mod.mozaik.reg.ModBlocks;
import com.mod.mozaik.reg.ModItems;
import com.mod.mozaik.reg.ResourceSupplier;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.loaders.ItemLayerModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ModItemModelGen extends ItemModelProvider {

	public ModItemModelGen(PackOutput output, ExistingFileHelper existingFileHelper) {
		super(output, Constants.MOD_ID, existingFileHelper);
	}

	@Override
	protected void registerModels() {
		ModBlocks.MORTARS.forEach(supplier -> {
			this.toBlock(supplier.get());
		});
        this.singleTex(ModItems.SHARD_BAG);
        NeoForgeRegistryHelper.ITEMS.getEntries().forEach(itemDeferredHolder -> {
            if (itemDeferredHolder.get() instanceof ShardItem shardItem) {
                this.singleTex(new ResourceSupplier<>(() -> shardItem, itemDeferredHolder.getId()));
            }
        });

		ShardItem.SHARDS.forEach((a, shard) -> {
		});
		this.singleTex(ModItems.BUTTON_TEMPLATE);
		this.singleTex(ModItems.BONE_TEMPLATE);
		this.singleTex(ModItems.BUBBLE_TEMPLATE);
		this.singleTex(ModItems.WORM_TEMPLATE);
		this.singleTex(ModItems.CANE_TEMPLATE);
		this.singleTex(ModItems.POINT_TEMPLATE);
		this.singleTex(ModItems.HORN_TEMPLATE);
		this.singleTex(ModItems.TREE_TEMPLATE);
		this.singleTex(ModItems.FORK_TEMPLATE);
	}

    private ItemModelBuilder generated(String name, ResourceLocation... layers) {
        return buildItem(name, "item/generated", 0, layers);
    }

    private ItemModelBuilder singleTex(ResourceSupplier<?> item) {
        return generated(item.id().getPath(), Constants.prefix("item/" + item.id().getPath()));
    }

	private void toBlock(Block b) {
		toBlockModel(b, BuiltInRegistries.BLOCK.getKey(b).getPath());
	}

	private void bushBlock(Block b) {
		toBlockModel(b, BuiltInRegistries.BLOCK.getKey(b).getPath() + "_0");
	}

	private void woodBlock(Block b, String variant) {
		woodBlockModel(b, BuiltInRegistries.BLOCK.getKey(b).getPath(), variant);
	}

	private void toBlockModel(Block b, String model) {
		toBlockModel(b, Constants.prefix("block/" + model));
	}

	private void woodBlockModel(Block b, String model, String variant) {
		toBlockModel(b, Constants.prefix("block/wood/" + variant + "/" + model));
	}

	private void toBlockModel(Block b, ResourceLocation model) {
		withExistingParent(BuiltInRegistries.BLOCK.getKey(b).getPath(), model);
	}

    private ItemModelBuilder buildItem(String name, String parent, int emissivity, ResourceLocation... layers) {
        ItemModelBuilder builder = withExistingParent(name, parent);
        for (int i = 0; i < layers.length; i++) {
            builder = builder.texture("layer" + i, layers[i]);
        }
        if (emissivity > 0) builder = builder.customLoader(ItemLayerModelBuilder::begin).emissive(emissivity, emissivity, 0).renderType("minecraft:translucent", 0).end();
        return builder;
    }
}
