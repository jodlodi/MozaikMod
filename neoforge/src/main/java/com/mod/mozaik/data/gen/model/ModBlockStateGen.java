package com.mod.mozaik.data.gen.model;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.model.block.mortar.MosaicLoaderBuilder;
import com.mod.mozaik.platform.NeoForgeRegistryHelper;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.polyomino.TesseraShape;
import com.mod.mozaik.reg.ModBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModBlockStateGen extends BlockStateProvider {

	public ModBlockStateGen(PackOutput output, ExistingFileHelper exFileHelper) {
		super(output, Constants.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		MosaicLoaderBuilder loaderBuilder = models().getBuilder("mosaic").customLoader(MosaicLoaderBuilder::new);

		ModBlocks.MORTARS.forEach(supplier -> {

		});

		this.genTessera();
	}

	private void genTessera() {
		Map<ShardMaterial.Type, Map<TesseraShape.ModelReference, ResourceLocation>> templateMap = new HashMap<>();

		for (ShardMaterial.Type type : ShardMaterial.Type.values()) {
			templateMap.put(type, new HashMap<>());
		}

		for (TesseraShape.ModelReference shape : TesseraShape.ModelReference.values()) {
			for (ShardMaterial.Type type : ShardMaterial.Type.values()) {
				templateMap.get(type).put(shape, Constants.prefix("block/" + type.name().toLowerCase(Locale.ROOT) + "/" + shape.getSerializedName()));
			}
		}

		NeoForgeRegistryHelper.SHARD_MATERIALS.getEntries().forEach(holder -> {
			for (int color = 0; color < holder.get().shades(); color++) {
				for (TesseraShape.ModelReference shape : TesseraShape.ModelReference.values()) {
					this.createFromTemplate(templateMap.get(holder.get().type()).get(shape), shape.getSerializedName(), holder.getId().getPath(), color);
				}
			}
		});
	}

	protected void createFromTemplate(ResourceLocation template, String modelPath, String texturePath, int i) {
		this.models().withExistingParent("mozaik/" + texturePath + "/" + i + "/" + modelPath, template)
						.texture("texture", Constants.prefix("block/mozaik/" + texturePath + "/block_" + i));
	}

}
