package com.mod.mozaik.data.gen;

import com.google.common.base.Ascii;
import com.mod.mozaik.client.ModKeyMappings;
import com.mod.mozaik.client.buttons.ToolButton;
import com.mod.mozaik.client.screens.MozaikTool;
import com.mod.mozaik.data.util.ModLangProvider;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.reg.ModItems;
import com.mod.mozaik.reg.ModTabs;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@NullMarked
public class ModLangGen extends ModLangProvider {

	public ModLangGen(PackOutput output) {
		super(output);
	}

	@Override
	protected void addCustomTranslations() {
		this.addItem(ModItems.SHARD_BAG, identifierToTitleCase(ModItems.SHARD_BAG.id()));
		this.addItem(ModItems.MORTARS.white(), "White Mortar");
		this.addItem(ModItems.MORTARS.orange(), "Orange Mortar");
		this.addItem(ModItems.MORTARS.magenta(), "Magenta Mortar");
		this.addItem(ModItems.MORTARS.lightBlue(), "Light Blue Mortar");
		this.addItem(ModItems.MORTARS.yellow(), "Yellow Mortar");
		this.addItem(ModItems.MORTARS.lime(), "Lime Mortar");
		this.addItem(ModItems.MORTARS.pink(), "Pink Mortar");
		this.addItem(ModItems.MORTARS.gray(), "Gray Mortar");
		this.addItem(ModItems.MORTARS.lightGray(), "Light Gray Mortar");
		this.addItem(ModItems.MORTARS.cyan(), "Cyan Mortar");
		this.addItem(ModItems.MORTARS.purple(), "Purple Mortar");
		this.addItem(ModItems.MORTARS.blue(), "Blue Mortar");
		this.addItem(ModItems.MORTARS.brown(), "Brown Mortar");
		this.addItem(ModItems.MORTARS.green(), "Green Mortar");
		this.addItem(ModItems.MORTARS.red(), "Red Mortar");
		this.addItem(ModItems.MORTARS.black(), "Black Mortar");

		ShardItem.SHARDS.forEach((key, item) -> this.addItem(() -> item, toTitleCase(key.identifier().getPath() + "_shards")));

		this.addCreativeTab(ModTabs.TAB, "Mozaik");

		for (MozaikTool tool : MozaikTool.values()) {
			this.add(tool.asTranslationString(), toTitleCase(tool.getSerializedName()));
		}

		this.addKeyCategory(ModKeyMappings.MOD_CATEGORY, "Mozaik");
		this.add(ToolButton.SHORTCUT, "§8Shortcut key: [%1$s]");
		this.addKeyMapping(ModKeyMappings.PICKER, "Picker Tool");
		this.addKeyMapping(ModKeyMappings.SELECT, "Select Tool");
		this.addKeyMapping(ModKeyMappings.WAND, "Wand Tool");
		this.addKeyMapping(ModKeyMappings.CURSOR, "Cursor Tool");
		this.addKeyMapping(ModKeyMappings.SWAP, "Swap Tool");
		this.addKeyMapping(ModKeyMappings.CHISEL, "Chisel Tool");
	}

	private static String identifierToTitleCase(Identifier id) {
		return toTitleCase(id.getPath());
	}

	private static String toTitleCase(String s) {
		return Arrays
				.stream(s.split("_"))
				.map(word -> word.isEmpty()
						? word
						: Ascii.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase())
				.collect(Collectors.joining(" "));
	}

	@Override
	public void addItem(Supplier<? extends Item> key, String name) {
		this.add(key.get(), name);
		if (key.get() instanceof BlockItem blockItem) this.add(blockItem.getBlock(), name);
	}
}
