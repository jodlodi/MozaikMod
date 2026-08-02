package com.mod.mozaik.data.util;

import com.mod.mozaik.Constants;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NullMarked;

import java.util.Optional;

@NullMarked
public class ModModelTemplates extends ModelTemplates {
	public static final ModelTemplate TESSERA_TEMPLATE_BASE = create("tessera_template_base", TextureSlot.TEXTURE);

	public static final ModelTemplate TESSERA_TEMPLATE_UP = create("no_bridge/tessera_template_up", TextureSlot.TEXTURE);
	public static final ModelTemplate TESSERA_TEMPLATE_RIGHT = create("no_bridge/tessera_template_right", TextureSlot.TEXTURE);
	public static final ModelTemplate TESSERA_TEMPLATE_DOWN = create("no_bridge/tessera_template_down", TextureSlot.TEXTURE);
	public static final ModelTemplate TESSERA_TEMPLATE_LEFT = create("no_bridge/tessera_template_left", TextureSlot.TEXTURE);

	public static final ModelTemplate TESSERA_TEMPLATE_UP_NO_RIGHT = create("no_corner/tessera_template_up_no_right", TextureSlot.TEXTURE);
	public static final ModelTemplate TESSERA_TEMPLATE_RIGHT_NO_UP = create("no_corner/tessera_template_right_no_up", TextureSlot.TEXTURE);
	public static final ModelTemplate TESSERA_TEMPLATE_RIGHT_NO_DOWN = create("no_corner/tessera_template_right_no_down", TextureSlot.TEXTURE);
	public static final ModelTemplate TESSERA_TEMPLATE_DOWN_NO_RIGHT = create("no_corner/tessera_template_down_no_right", TextureSlot.TEXTURE);
	public static final ModelTemplate TESSERA_TEMPLATE_DOWN_NO_LEFT = create("no_corner/tessera_template_down_no_left", TextureSlot.TEXTURE);
	public static final ModelTemplate TESSERA_TEMPLATE_LEFT_NO_DOWN = create("no_corner/tessera_template_left_no_down", TextureSlot.TEXTURE);
	public static final ModelTemplate TESSERA_TEMPLATE_LEFT_NO_UP = create("no_corner/tessera_template_left_no_up", TextureSlot.TEXTURE);
	public static final ModelTemplate TESSERA_TEMPLATE_UP_NO_LEFT = create("no_corner/tessera_template_up_no_left", TextureSlot.TEXTURE);

	public static ModelTemplate create(String id, TextureSlot... slots) {
		return create(Constants.prefix(id), slots).extend().parent(Constants.prefix("block/murals/" + id)).build();
	}

	@SuppressWarnings("deprecation")
	public static ModelTemplate create(Identifier id, TextureSlot... slots) {
		return new ModelTemplate(Optional.of(ModelLocationUtils.decorateBlockModelLocation(id.toString())), Optional.empty(), slots);
	}
}
