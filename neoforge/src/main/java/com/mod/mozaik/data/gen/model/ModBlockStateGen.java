package com.mod.mozaik.data.gen.model;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.model.block.mortar.MosaicStateModelBuilder;
import com.mod.mozaik.data.util.ModExtendedModelTemplates;
import com.mod.mozaik.data.util.ModModelTemplates;
import com.mod.mozaik.platform.NeoForgeRegistryHelper;
import com.mod.mozaik.polyomino.TesseraShape;
import com.mod.mozaik.reg.ModBlocks;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModBlockStateGen extends BlockModelGenerators {

	public ModBlockStateGen(Consumer<BlockModelDefinitionGenerator> blockStateOutput, ItemModelOutput itemModelOutput, BiConsumer<ResourceLocation, ModelInstance> modelOutput) {
		super(blockStateOutput, itemModelOutput, modelOutput);
	}

	@Override
	public void run() {
		ModBlocks.MORTARS.forEach(supplier -> this.wrapBlockItem(supplier.get(), block -> {
			TextureMapping dryTextures = (new TextureMapping())
					.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block))
					.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block))
					.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(block))
					.put(TextureSlot.TOP, TextureMapping.getBlockTexture(block));

			ResourceLocation horizontal = ModExtendedModelTemplates.MORTAR.create(block, dryTextures, this.modelOutput);

			this.blockStateOutput.accept(
					MultiVariantGenerator.dispatch(
							block,
							MultiVariant.of(new MosaicStateModelBuilder(horizontal))
					)
			);
		}));

		this.genTessera();
	}

	private void genTessera() {
		NeoForgeRegistryHelper.SHARD_MATERIALS.getEntries().forEach(holder -> {
			for (int color = 0; color < holder.get().shades(); color++) {
				for (TesseraShape.ModelReference shape : TesseraShape.ModelReference.values()) {
					this.createFromTemplate(ModModelTemplates.TEMPLATE_MAP.get(holder.get().type()).get(shape), shape.getSerializedName(), holder.getId().getPath(), color);
				}
			}
		});
	}

	protected void createFromTemplate(ModelTemplate template, String modelPath, String texturePath, int i) {
		Material material = new Material(Constants.prefix("block/mozaik/" + texturePath + "/block_" + i));
		template.create(
				Constants.prefix("mozaik/" + texturePath + "/" + i + "/" + modelPath),
				TextureMapping.defaultTexture(material),
				this.modelOutput
		);
	}

	public void wrapBlockItem(Block block, Consumer<Block> blockRegistry) {
		blockRegistry.accept(block);
		this.generateBlockItem(block);
	}

	public void generateBlockItem(Block block) {
		this.registerSimpleItemModel(block, BuiltInRegistries.BLOCK.getKey(block).withPrefix("block/"));
	}
}
