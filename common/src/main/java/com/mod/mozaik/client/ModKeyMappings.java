package com.mod.mozaik.client;

import com.mod.mozaik.Constants;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.util.NaturalDigitCollection;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("SameParameterValue")
public class ModKeyMappings {
	public static final List<KeyMapping> KEY_MAPPINGS = new ArrayList<>();

	// TOOLS
	public static final String MOD_TOOLS = "key.category.mozaik.tools";
	public static final KeyMapping PICKER = create("picker", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_A, MOD_TOOLS);
	public static final KeyMapping SELECT = create("select", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_S, MOD_TOOLS);
	public static final KeyMapping WAND = create("wand", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_D, MOD_TOOLS);
	public static final KeyMapping CURSOR = create("cursor", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F, MOD_TOOLS);
	public static final KeyMapping SWAP = create("swap", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, MOD_TOOLS);
	public static final KeyMapping CHISEL = create("chisel", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, MOD_TOOLS);

	// ACTIONS
	public static final String MOD_ACTIONS = "key.category.mozaik.actions";
	public static final KeyMapping DELETE = create("delete", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_DELETE, MOD_ACTIONS);
	public static final KeyMapping SELECT_ALL = create("select_all", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_A, GLFW.GLFW_MOD_CONTROL, MOD_ACTIONS);

	// FAVOURITES
	public static final String MOD_FAVOURITES = "key.category.mozaik.favourites";
	public static final NaturalDigitCollection<KeyMapping> FAVOURITE = NaturalDigitCollection.zipMap(NaturalDigitCollection.VALUES, NaturalDigitCollection.NAMES, (value, name) ->
			create("favourite_" + name, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_0 + value, MOD_FAVOURITES)
	);

	private static KeyMapping create(String name, InputConstants.Type type, int keyCode, String category) {
		return create(name, type, keyCode, 0x0, category);
	}

	private static KeyMapping create(String name, InputConstants.Type type, int keyCode, int keyMod, String category) {
		KeyMapping keyMapping = Services.MODLOADER.createKeyMapping(Constants.MOD_ID + "." + name, type, keyCode, keyMod, category);
		KEY_MAPPINGS.add(keyMapping);
		return keyMapping;
	}
}
