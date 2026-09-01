package com.mod.mozaik.util;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface IMozaikKeyMapping {
	Modifier multiLoader_Template$getModifier();

	static boolean matches(KeyMapping mapping, KeyEvent event) {
		return switch (((IMozaikKeyMapping)mapping).multiLoader_Template$getModifier()) {
			case NONE -> mapping.matches(event);
			case SHIFT -> event.hasShiftDown() && mapping.matches(event);
			case CONTROL -> event.hasControlDown() && mapping.matches(event);
			case ALT -> event.hasAltDown() && mapping.matches(event);
		};
	}

	enum Modifier {
		NONE,
		SHIFT,
		CONTROL,
		ALT
	}
}
