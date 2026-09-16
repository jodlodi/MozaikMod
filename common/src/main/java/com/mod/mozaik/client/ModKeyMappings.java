package com.mod.mozaik.client;

import com.mod.mozaik.Constants;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.util.NaturalDigitCollection;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
@SuppressWarnings("SameParameterValue")
public class ModKeyMappings {
	public static final List<KeyMapping> KEY_MAPPINGS = new ArrayList<>();

	// TOOLS
	public static final KeyMapping.Category MOD_TOOLS = KeyMapping.Category.register(Constants.prefix("tools"));
	public static final KeyMapping PICKER = create("picker", InputConstants.Type.KEYBOARD, InputConstants.KEY_A, MOD_TOOLS);
	public static final KeyMapping SELECT = create("select", InputConstants.Type.KEYBOARD, InputConstants.KEY_S, MOD_TOOLS);
	public static final KeyMapping WAND = create("wand", InputConstants.Type.KEYBOARD, InputConstants.KEY_D, MOD_TOOLS);
	public static final KeyMapping CURSOR = create("cursor", InputConstants.Type.KEYBOARD, InputConstants.KEY_F, MOD_TOOLS);
	public static final KeyMapping SWAP = create("swap", InputConstants.Type.KEYBOARD, InputConstants.KEY_G, MOD_TOOLS);
	public static final KeyMapping CHISEL = create("chisel", InputConstants.Type.KEYBOARD, InputConstants.KEY_H, MOD_TOOLS);

	// ACTIONS
	public static final KeyMapping.Category MOD_ACTIONS = KeyMapping.Category.register(Constants.prefix("actions"));
	public static final KeyMapping DELETE = create("delete", InputConstants.Type.KEYBOARD, InputConstants.KEY_DELETE, MOD_ACTIONS);
	public static final KeyMapping SELECT_ALL = create("select_all", InputConstants.Type.KEYBOARD, InputConstants.KEY_A, InputConstants.MOD_CONTROL, MOD_ACTIONS);

	// FAVOURITES
	public static final KeyMapping.Category MOD_FAVOURITES = KeyMapping.Category.register(Constants.prefix("favourites"));
	public static final NaturalDigitCollection<KeyMapping> FAVOURITE = NaturalDigitCollection.zipMap(NaturalDigitCollection.VALUES, NaturalDigitCollection.NAMES, (value, name) ->
			create("favourite_" + name, InputConstants.Type.KEYBOARD, InputConstants.KEY_0 + value, MOD_FAVOURITES)
	);

	private static KeyMapping create(String name, InputConstants.Type type, int keyCode, KeyMapping.Category category) {
		return create(name, type, keyCode, 0x0, category);
	}

	private static KeyMapping create(String name, InputConstants.Type type, int keyCode, int keyMod, KeyMapping.Category category) {
		KeyMapping keyMapping = Services.MODLOADER.createKeyMapping(Constants.MOD_ID + "." + name, type, keyCode, keyMod, category);
		KEY_MAPPINGS.add(keyMapping);
		return keyMapping;
	}
}
