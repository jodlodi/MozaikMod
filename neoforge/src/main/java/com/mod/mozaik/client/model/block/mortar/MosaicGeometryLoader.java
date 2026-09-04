package com.mod.mozaik.client.model.block.mortar;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mod.mozaik.Constants;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;

public class MosaicGeometryLoader implements IGeometryLoader<MosaicUnbakedGeometry> {
	public static final MosaicGeometryLoader INSTANCE = new MosaicGeometryLoader();
	public static final ResourceLocation ID = Constants.prefix("mosaic");

	private MosaicGeometryLoader() {

	}

	@Override
	public MosaicUnbakedGeometry read(JsonObject jsonObject, JsonDeserializationContext context) throws JsonParseException {
		// Use the given JsonObject and, if needed, the JsonDeserializationContext to get properties from the model JSON.
		// The MosaicUnbakedGeometry constructor may have constructor parameters (see below).
		return new MosaicUnbakedGeometry();
	}
}
