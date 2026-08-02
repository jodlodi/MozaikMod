package com.mod.mozaik.data.gen;

import com.mod.mozaik.Constants;
import com.mod.mozaik.data.gen.model.ModBlockStateGen;
import com.mod.mozaik.data.gen.model.ModItemModelGen;
import com.mod.mozaik.platform.NeoForgeRegistryHelper;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.core.Holder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

@NullMarked
public class ModelGen extends ModelProvider {
	private final PackOutput.PathProvider blocks;
	private final PackOutput.PathProvider items;
	private final PackOutput.PathProvider models;

	public ModelGen(PackOutput output) {
		super(output, Constants.MOD_ID);
		this.blocks = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
		this.items = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
		this.models = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
	}

	@Override
	public CompletableFuture<?> run(CachedOutput output) {
		ItemInfoCollector itemModelOutput = new ItemInfoCollector(this::getKnownItems);
		BlockStateGeneratorCollector blockModelOutput = new BlockStateGeneratorCollector(this::getKnownBlocks);
		SimpleModelCollector modelOutput = new SimpleModelCollector();
		this.registerModels(new ModBlockStateGen(blockModelOutput, itemModelOutput, modelOutput), new ModItemModelGen(itemModelOutput, modelOutput));
		blockModelOutput.validate();
		itemModelOutput.finalizeAndValidate();
		return CompletableFuture.allOf(blockModelOutput.save(output, this.blocks), modelOutput.save(output, this.models), itemModelOutput.save(output, this.items));
	}

	@Override
	protected Stream<? extends Holder<Block>> getKnownBlocks() {
		return NeoForgeRegistryHelper.BLOCKS.getEntries().stream();
	}

	@Override
	protected Stream<? extends Holder<Item>> getKnownItems() {
		return NeoForgeRegistryHelper.ITEMS.getEntries().stream();
	}
}
