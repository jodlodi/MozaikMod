package com.mod.mozaik.data.gen.model;

import com.mod.mozaik.Constants;
import com.mod.mozaik.TesseraMaterial;
import com.mod.mozaik.client.model.block.mortar.MosaicStateModelBuilder;
import com.mod.mozaik.data.util.ModExtendedModelTemplates;
import com.mod.mozaik.data.util.ModModelTemplates;
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
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NullMarked;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@NullMarked
public class ModBlockStateGen extends BlockModelGenerators {

	public ModBlockStateGen(Consumer<BlockModelDefinitionGenerator> blockStateOutput, ItemModelOutput itemModelOutput, BiConsumer<Identifier, ModelInstance> modelOutput) {
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

			Identifier horizontal = ModExtendedModelTemplates.MORTAR.create(block, dryTextures, this.modelOutput);

			this.blockStateOutput.accept(
					MultiVariantGenerator.dispatch(
							block,
							MultiVariant.of(new MosaicStateModelBuilder(horizontal))
					)
			);
		}));

		for (TesseraMaterial material : TesseraMaterial.values()) {
			for (int i = 0; i < material.getSpriteSheets().size(); i++) {
				this.genTessera(material, i);
			}
		}
	}

	public static final String TESSERA = "tessera";

	public static final String BRIDGE_UP = "bridge_up";
	public static final String BRIDGE_NO_UP = "bridge_no_up";

	public static final String BRIDGE_RIGHT = "bridge_right";
	public static final String BRIDGE_NO_RIGHT = "bridge_no_right";

	public static final String BRIDGE_DOWN = "bridge_down";
	public static final String BRIDGE_NO_DOWN = "bridge_no_down";

	public static final String BRIDGE_LEFT = "bridge_left";
	public static final String BRIDGE_NO_LEFT = "bridge_no_left";

	public static final String CORNER_UP_RIGHT = "corner_up_right";
	public static final String CORNER_UP_NO_RIGHT = "corner_up_no_right";

	public static final String CORNER_DOWN_RIGHT = "corner_down_right";
	public static final String CORNER_RIGHT_NO_DOWN = "corner_right_no_down";

	public static final String CORNER_DOWN_LEFT = "corner_down_left";
	public static final String CORNER_DOWN_NO_LEFT = "corner_down_no_left";

	public static final String CORNER_UP_LEFT = "corner_up_left";
	public static final String CORNER_LEFT_NO_UP = "corner_left_no_up";

	private void genTessera(TesseraMaterial material, int color) {
		this.createFromTemplate(ModModelTemplates.TESSERA_BASE, TESSERA, material, color);

		this.createFromTemplate(ModModelTemplates.TESSERA_UP, BRIDGE_UP, material, color);
		this.createFromTemplate(ModModelTemplates.TESSERA_NO_UP, BRIDGE_NO_UP, material, color);

		this.createFromTemplate(ModModelTemplates.TESSERA_RIGHT, BRIDGE_RIGHT, material, color);
		this.createFromTemplate(ModModelTemplates.TESSERA_NO_RIGHT, BRIDGE_NO_RIGHT, material, color);

		this.createFromTemplate(ModModelTemplates.TESSERA_DOWN, BRIDGE_DOWN, material, color);
		this.createFromTemplate(ModModelTemplates.TESSERA_NO_DOWN, BRIDGE_NO_DOWN, material, color);

		this.createFromTemplate(ModModelTemplates.TESSERA_LEFT, BRIDGE_LEFT, material, color);
		this.createFromTemplate(ModModelTemplates.TESSERA_NO_LEFT, BRIDGE_NO_LEFT, material, color);

		this.createFromTemplate(ModModelTemplates.TESSERA_UR, CORNER_UP_RIGHT, material, color);
		this.createFromTemplate(ModModelTemplates.TESSERA_TEMPLATE_UP_NO_RIGHT, CORNER_UP_NO_RIGHT, material, color);

		this.createFromTemplate(ModModelTemplates.TESSERA_DR, CORNER_DOWN_RIGHT, material, color);
		this.createFromTemplate(ModModelTemplates.TESSERA_TEMPLATE_RIGHT_NO_DOWN, CORNER_RIGHT_NO_DOWN, material, color);

		this.createFromTemplate(ModModelTemplates.TESSERA_DL, CORNER_DOWN_LEFT, material, color);
		this.createFromTemplate(ModModelTemplates.TESSERA_TEMPLATE_DOWN_NO_LEFT, CORNER_DOWN_NO_LEFT, material, color);

		this.createFromTemplate(ModModelTemplates.TESSERA_UL, CORNER_UP_LEFT, material, color);
		this.createFromTemplate(ModModelTemplates.TESSERA_TEMPLATE_LEFT_NO_UP, CORNER_LEFT_NO_UP, material, color);
	}

	protected void createFromTemplate(ModelTemplate template, String modelPath, TesseraMaterial texturePath, int i) {
		Material material = new Material(Constants.prefix("block/mural/" + texturePath.getSerializedName() + "/block_" + i));
		template.create(
				Constants.prefix("mozaik/" + texturePath.getSerializedName() + "/" + i + "/" + modelPath),
				TextureMapping.defaultTexture(material).put(TextureSlot.PARTICLE, material),
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
