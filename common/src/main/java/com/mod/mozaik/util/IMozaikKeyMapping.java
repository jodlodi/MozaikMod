package com.mod.mozaik.util;

import net.minecraft.client.KeyMapping;
import net.minecraft.MethodsReturnNonnullByDefault;
import org.lwjgl.glfw.GLFW;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface IMozaikKeyMapping {
	Modifier multiLoader_Template$getModifier();

	static boolean hasShiftDown(int modifiers) {
		return (modifiers & GLFW.GLFW_MOD_SHIFT) != 0;
	}

	static boolean hasControlDown(int modifiers) {
		return (modifiers & GLFW.GLFW_MOD_CONTROL) != 0;
	}

	static boolean hasAltDown(int modifiers) {
		return (modifiers & GLFW.GLFW_MOD_ALT) != 0;
	}

	static boolean matches(KeyMapping mapping, int keyCode, int scanCode, int modifiers) {
		return switch (((IMozaikKeyMapping) mapping).multiLoader_Template$getModifier()) {
			case NONE -> mapping.matches(keyCode, scanCode);
			case SHIFT -> hasShiftDown(modifiers) && mapping.matches(keyCode, scanCode);
			case CONTROL -> hasControlDown(modifiers) && mapping.matches(keyCode, scanCode);
			case ALT -> hasAltDown(modifiers) && mapping.matches(keyCode, scanCode);
		};
	}

	enum Modifier {
		NONE,
		SHIFT,
		CONTROL,
		ALT
	}
}
