package com.mod.mozaik.reg;

import com.mod.mozaik.platform.Services;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class ModItems {
	public static final ResourceSupplier<BlockItem> GLUE = registerBlock(ModBlocks.GLUE);

	public static void init() {

	}

	private static ResourceSupplier<BlockItem> registerBlock(ResourceSupplier<Block> block) {
		return Services.REGISTRY.registerItem(block.id().getPath(), properties -> new BlockItem(block.get(), properties));
	}
}
