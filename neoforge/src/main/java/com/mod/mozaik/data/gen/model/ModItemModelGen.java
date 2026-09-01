package com.mod.mozaik.data.gen.model;

import com.mod.mozaik.client.ShardBagSpecialRenderer;
import com.mod.mozaik.items.ShardBagItem;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.reg.ModItems;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ModItemModelGen extends ItemModelGenerators {

    public ModItemModelGen(ItemModelOutput itemModelOutput, BiConsumer<ResourceLocation, ModelInstance> modelOutput) {
        super(itemModelOutput, modelOutput);
    }

    @Override
    public void run() {
        this.generateBagModels(ModItems.SHARD_BAG.get());
        ShardItem.SHARDS.values().forEach(shard -> this.generateFlatItem(shard, ModelTemplates.FLAT_ITEM));
        this.generateFlatItem(ModItems.BUTTON_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.BONE_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.BUBBLE_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.WORM_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.CANE_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.POINT_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.HORN_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.TREE_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.FORK_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
    }

    private void generateBagModels(Item bundle) {
        ItemModel.Unbaked closedModel = ItemModelUtils.plainModel(this.createFlatItemModel(bundle, ModelTemplates.FLAT_ITEM));
        ResourceLocation openBackCover = this.generateBundleCoverModel(bundle, ModelTemplates.BUNDLE_OPEN_BACK_INVENTORY, "_open_back");
        ResourceLocation openFrontCover = this.generateBundleCoverModel(bundle, ModelTemplates.BUNDLE_OPEN_FRONT_INVENTORY, "_open_front");
        ItemModel.Unbaked openModel = ItemModelUtils.composite(
                ItemModelUtils.plainModel(openBackCover), new ShardBagSpecialRenderer.Unbaked(), ItemModelUtils.plainModel(openFrontCover)
        );
        ItemModel.Unbaked inGuiModel = ItemModelUtils.conditional(new ShardBagItem.ShardBagHasSelectedItem(), openModel, closedModel);
        this.itemModelOutput.accept(bundle, ItemModelUtils.select(new DisplayContext(), closedModel, ItemModelUtils.when(ItemDisplayContext.GUI, inGuiModel)));
    }
}
