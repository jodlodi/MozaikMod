package com.mod.mozaik.data.gen;

import com.mod.mozaik.data.util.ModLangProvider;
import com.mod.mozaik.reg.ModBlocks;
import com.mod.mozaik.reg.ModItems;
import com.mod.mozaik.reg.ModTabs;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NullMarked;

import java.util.function.Supplier;

@NullMarked
public class ModLangGen extends ModLangProvider {

	public ModLangGen(PackOutput output) {
		super(output);
	}

	@Override
	protected void addCustomTranslations() {
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

		this.addCreativeTab(ModTabs.TAB, "Mozaik");
	}

	@Override
	public void addItem(Supplier<? extends Item> key, String name) {
		this.add(key.get(), name);
		if (key.get() instanceof BlockItem blockItem) this.add(blockItem.getBlock(), name);
	}
}
