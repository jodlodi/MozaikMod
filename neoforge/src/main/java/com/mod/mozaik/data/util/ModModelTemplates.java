package com.mod.mozaik.data.util;

import com.mod.mozaik.Constants;
import com.mod.mozaik.polyomino.TesseraShape;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@NullMarked
public class ModModelTemplates extends ModelTemplates {
	public static final Map<TesseraShape.ModelReference, ModelTemplate> TEMPLATE_MAP = new HashMap<>();
	public static final Map<TesseraShape.ModelReference, ModelTemplate> FULLBRIGHT_MAP = new HashMap<>();

	static {
		for (TesseraShape.ModelReference shape : TesseraShape.ModelReference.values()) {
			TEMPLATE_MAP.put(shape, create(shape.getSerializedName(), false, TextureSlot.TEXTURE));
			FULLBRIGHT_MAP.put(shape, create(shape.getSerializedName(), true, TextureSlot.TEXTURE));
		}
	}

	public static ModelTemplate create(String id, boolean fullBright, TextureSlot... slots) {
		return create(Constants.prefix(id), slots).extend().parent(Constants.prefix("block/" + (fullBright ? "murals_fullbright/" : "murals/") + id)).build();
	}

	@SuppressWarnings("deprecation")
	public static ModelTemplate create(Identifier id, TextureSlot... slots) {
		return new ModelTemplate(Optional.of(ModelLocationUtils.decorateBlockModelLocation(id.toString())), Optional.empty(), slots);
	}
}
