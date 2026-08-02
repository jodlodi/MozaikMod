package com.mod.mozaik.data.gen;

import com.mod.mozaik.data.util.ModLangProvider;
import com.mod.mozaik.reg.ModBlocks;
import com.mod.mozaik.reg.ModItems;
import com.mod.mozaik.reg.ModTabs;
import net.minecraft.data.PackOutput;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ModLangGen extends ModLangProvider {

	public ModLangGen(PackOutput output) {
		super(output);
	}

	@Override
	protected void addCustomTranslations() {
		this.addBlock(ModBlocks.GLUE, "Glue");
		this.addItem(ModItems.GLUE, "Glue");
		this.addCreativeTab(ModTabs.TAB, "Mozaik");
	}
}
