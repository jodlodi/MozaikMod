package com.mod.mozaik.data.util;

import com.mod.mozaik.Constants;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.polyomino.TesseraShape;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModModelTemplates extends ModelTemplates {
	public static final Map<ShardMaterial.Type, Map<TesseraShape.ModelReference, ModelTemplate>> TEMPLATE_MAP = new HashMap<>();

	static {
		for (ShardMaterial.Type type : ShardMaterial.Type.values()) {
			TEMPLATE_MAP.put(type, new HashMap<>());
		}
		for (TesseraShape.ModelReference shape : TesseraShape.ModelReference.values()) {
			for (ShardMaterial.Type type : ShardMaterial.Type.values()) {
				TEMPLATE_MAP.get(type).put(shape, create(shape.getSerializedName(), type, TextureSlot.TEXTURE));
			}
		}
	}

	public static ModelTemplate create(String id, ShardMaterial.Type type, TextureSlot... slots) {
		return create(Constants.prefix(id), slots).extend().parent(Constants.prefix("block/" + type.name().toLowerCase(Locale.ROOT) + "/" + id)).build();
	}

	@SuppressWarnings("deprecation")
	public static ModelTemplate create(ResourceLocation id, TextureSlot... slots) {
		return new ModelTemplate(Optional.of(ModelLocationUtils.decorateBlockModelLocation(id.toString())), Optional.empty(), slots);
	}
}
