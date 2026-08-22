package com.mod.mozaik.client;

import com.mod.mozaik.Constants;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.jspecify.annotations.NullMarked;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public class ModKeyMappings {
	public static final KeyMapping.Category MOD_CATEGORY = KeyMapping.Category.register(Constants.prefix("default"));

	public static final List<KeyMapping> KEY_MAPPINGS = new ArrayList<>();

	public static final KeyMapping PICKER = create("picker", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_A, MOD_CATEGORY);
	public static final KeyMapping SELECT = create("select", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_S, MOD_CATEGORY);
	public static final KeyMapping WAND = create("wand", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_D, MOD_CATEGORY);
	public static final KeyMapping CURSOR = create("cursor", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F, MOD_CATEGORY);
	public static final KeyMapping SWAP = create("swap", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, MOD_CATEGORY);
	public static final KeyMapping CHISEL = create("chisel", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, MOD_CATEGORY);

	private static KeyMapping create(String name, InputConstants.Type type, int keyCode, KeyMapping.Category category) {
		KeyMapping mapping = new KeyMapping(Constants.MOD_ID + "." + name, type, keyCode, category);
		KEY_MAPPINGS.add(mapping);
		return mapping;
	}
}
