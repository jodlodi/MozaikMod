package com.mod.mozaik.data.gen.model;

import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.reg.ModItems;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.resources.Identifier;
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
        this.generateFlatItem(ModItems.SHARD_BAG.get(), ModelTemplates.FLAT_ITEM);
        ShardItem.SHARDS.values().forEach(shard -> {
            this.generateFlatItem(shard, ModelTemplates.FLAT_ITEM);
        });
        this.generateFlatItem(ModItems.BUTTON_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.BONE_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.BUBBLE_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.WORM_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.CANE_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.POINT_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.HORN_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
        this.generateFlatItem(ModItems.TREE_TEMPLATE.get(), ModelTemplates.FLAT_ITEM);
    }
}
