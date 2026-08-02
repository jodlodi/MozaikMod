package com.mod.mozaik.data.gen;

import com.mod.mozaik.data.util.ModLangProvider;
import com.mod.mozaik.reg.ModBlocks;
import com.mod.mozaik.reg.ModItems;
import net.minecraft.data.PackOutput;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ModLangGen extends ModLangProvider {

	public ModLangGen(PackOutput output) {
		super(output);
	}

	@Override
	protected void addCustomTranslations() {
		// Blocks
		this.addBlock(ModBlocks.GLUE, "Glue");
		this.addItem(ModItems.GLUE, "Glue");
	}
}
