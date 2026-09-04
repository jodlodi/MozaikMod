package com.mod.mozaik.client.model.block.mortar;

import com.mod.mozaik.client.GraphicsRenderHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;
import java.util.function.Function;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MosaicUnbakedGeometry implements IUnbakedGeometry<MosaicUnbakedGeometry> {

	// The constructor may have any parameters you need, and store them in fields for further usage below.
	// If the constructor has parameters, the constructor call in MyGeometryLoader#read must match them.
	public MosaicUnbakedGeometry() {

	}

	// Method responsible for model baking, returning our dynamic model. Parameters in this method are:
	// - The geometry baking context. Contains many properties that we will pass into the model, e.g. light and ao values.
	// - The model baker. Can be used for baking sub-models.
	// - The sprite getter. Maps materials (= texture variables) to TextureAtlasSprites. Materials can be obtained from the context.
	//   For example, to get a model's particle texture, call spriteGetter.apply(context.getMaterial("particle"));
	// - The model state. This holds the properties from the blockstate file, e.g. rotations and the uvlock boolean.
	// - The item overrides. This is the code representation of an "overrides" block in an item model.
	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
		GraphicsRenderHelper.BAKER = baker;
		// See info on the parameters below.
		return new MosaicDynamicBakedModel(context.useAmbientOcclusion(), context.isGui3d(), context.useBlockLight(),
				spriteGetter.apply(context.getMaterial("particle")), overrides);
	}

	// Method responsible for correctly resolving parent properties. Required if this model loads any nested models or reuses the vanilla loader on itself (see below).
	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
		IUnbakedGeometry.super.resolveParents(modelGetter, context);
	}

	@Override
	public Set<String> getConfigurableComponentNames() {
		return IUnbakedGeometry.super.getConfigurableComponentNames();
	}
}
