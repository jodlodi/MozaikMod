package com.mod.mozaik.client.model.block.mortar;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mod.mozaik.Constants;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MosaicGeometryLoader implements IGeometryLoader<MosaicUnbakedGeometry> {
	public static final MosaicGeometryLoader INSTANCE = new MosaicGeometryLoader();
	public static final ResourceLocation ID = Constants.prefix("mosaic");

	private MosaicGeometryLoader() {

	}

	@Override
	public MosaicUnbakedGeometry read(JsonObject jsonObject, JsonDeserializationContext context) throws JsonParseException {
		jsonObject.remove("loader");
		BlockModel base = context.deserialize(jsonObject, BlockModel.class);
		return new MosaicUnbakedGeometry(base);
	}
}
