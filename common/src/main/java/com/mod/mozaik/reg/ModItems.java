package com.mod.mozaik.reg;

import com.mod.mozaik.platform.Services;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ColorCollection;
import org.jspecify.annotations.NullMarked;

import java.util.Properties;
import java.util.function.Function;

@NullMarked
public class ModItems {
	public static final ColorCollection<ResourceSupplier<BlockItem>> MORTARS = registerColoredBlockItems(ModBlocks.MORTARS, properties -> properties);

	public static void init() {

	}

	private static <T extends Block> ResourceSupplier<BlockItem> registerBlockItem(ResourceSupplier<T> block, Function<Item.Properties, Item.Properties> propertiesFunction) {
		return Services.REGISTRY.registerItem(block.id().getPath(), properties -> new BlockItem(block.get(), propertiesFunction.apply(properties)));
	}

	public static <T extends Block> ColorCollection<ResourceSupplier<BlockItem>> registerColoredBlockItems(ColorCollection<ResourceSupplier<T>> blocks, Function<Item.Properties, Item.Properties> propertiesFunction) {
		return ColorCollection.zipMap(
				ColorCollection.VALUES,
				blocks,
				(color , supplier) -> registerBlockItem(supplier, propertiesFunction)
		);
	}
}
