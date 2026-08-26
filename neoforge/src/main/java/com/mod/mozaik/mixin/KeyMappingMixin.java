package com.mod.mozaik.mixin;

import com.mod.mozaik.util.IMozaikKeyMapping;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@NullMarked
@Mixin(KeyMapping.class)
public abstract class KeyMappingMixin implements IMozaikKeyMapping {
	@Shadow(remap = false)
	public abstract KeyModifier getKeyModifier();

	@Override
	public Modifier multiLoader_Template$getModifier() {
		return switch (this.getKeyModifier()) {
			case CONTROL, CONTROL_OR_COMMAND -> Modifier.CONTROL;
			case SHIFT -> Modifier.SHIFT;
			case ALT -> Modifier.ALT;
			case NONE -> Modifier.NONE;
		};
	}
}
