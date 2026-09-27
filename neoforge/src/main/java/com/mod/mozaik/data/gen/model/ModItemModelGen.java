package com.mod.mozaik.data.gen.model;

import com.mod.mozaik.client.ShardBagSpecialRenderer;
import com.mod.mozaik.items.ShardBagItem;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.reg.ModItems;
import com.mod.mozaik.util.ColorCollection;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NullMarked;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiConsumer;

@NullMarked
@ParametersAreNonnullByDefault
public class ModItemModelGen extends ItemModelGenerators {

    public ModItemModelGen(ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
        super(itemModelOutput, modelOutput);
    }

    @Override
    public void run() {
        this.generateBagModels(ModItems.SHARD_BAG.get(), Items.BUNDLE);
        ColorCollection.zipApply(ModItems.DYED_SHARD_BAG, ColorCollection.ItemCollections.BUNDLES, (shardBags, bundle) ->
                this.generateBagModels(shardBags.get(), bundle)
        );

        ShardItem.SHARDS.values().forEach(shard -> this.generateFlatItem(shard, ModelTemplates.FLAT_ITEM, "shards/"));
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

    private void generateBagModels(Item bag, Item bundle) {
        ItemModel.Unbaked closedModel = ItemModelUtils.plainModel(this.createFlatItemModel(bag, ModelTemplates.FLAT_ITEM, "shard_bag/"));

        Identifier openBackCover = this.generateBundleCoverModel(bundle, ModelTemplates.BUNDLE_OPEN_BACK_INVENTORY, "_open_back");
        Identifier openFrontCover = this.generateBundleCoverModel(bundle, ModelTemplates.BUNDLE_OPEN_FRONT_INVENTORY, "_open_front");

        ItemModel.Unbaked openModel = ItemModelUtils.composite(
                ItemModelUtils.plainModel(openBackCover), new ShardBagSpecialRenderer.Unbaked(), ItemModelUtils.plainModel(openFrontCover)
        );

        ItemModel.Unbaked inGuiModel = ItemModelUtils.conditional(new ShardBagItem.ShardBagHasSelectedItem(), openModel, closedModel);
        this.itemModelOutput.accept(bag, ItemModelUtils.select(new DisplayContext(), closedModel, ItemModelUtils.when(ItemDisplayContext.GUI, inGuiModel)));
    }

    public void generateFlatItem(Item item, ModelTemplate template, String prefix) {
        this.itemModelOutput.accept(item, ItemModelUtils.plainModel(this.createFlatItemModel(item, template, prefix)));
    }

    public Identifier createFlatItemModel(Item item, ModelTemplate template, String prefix) {
        Identifier location = BuiltInRegistries.ITEM.getKey(item).withPrefix("item/" + prefix);
        return template.create(location, new TextureMapping().put(TextureSlot.LAYER0, new Material(location)), this.modelOutput);
    }
}
