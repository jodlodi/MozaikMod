package com.mod.mozaik.client.model.block.mortar;

import com.mod.mozaik.client.GraphicsRenderHelper;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.ElementsModel;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;
import java.util.function.Function;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MosaicUnbakedGeometry implements IUnbakedGeometry<MosaicUnbakedGeometry> {
	private final BlockModel base;

	public MosaicUnbakedGeometry(BlockModel base) {
		this.base = base;
	}

	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides) {
		return new MosaicDynamicBakedModel(this.base, baker, spriteGetter.apply(context.getMaterial("particle")), overrides);
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
		this.base.resolveParents(modelGetter);
	}

	@Override
	public Set<String> getConfigurableComponentNames() {
		return IUnbakedGeometry.super.getConfigurableComponentNames();
	}
}
