package com.mod.mozaik.data.gen.model;

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
        ModItems.SHARDS.forEach(shard -> {
            this.generateFlatItem(shard.get(), ModelTemplates.FLAT_ITEM);
        });
    }
}
