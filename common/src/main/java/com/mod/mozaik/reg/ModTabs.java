package com.mod.mozaik.reg;

import com.mod.mozaik.platform.Services;
import net.minecraft.world.item.CreativeModeTab;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ModTabs {
	public static final ResourceSupplier<CreativeModeTab> TAB = Services.REGISTRY.registerCreativeTab("tab", () -> ModItems.MORTARS.black().get().getDefaultInstance(), (itemDisplayParameters, output) -> {
		output.accept(ModItems.SHARD_BAG.get());
		ModItems.MORTARS.forEach(supplier -> output.accept(supplier.get()));

		output.accept(ModItems.STONE_SHARDS.get());
		output.accept(ModItems.BLACKSTONE_SHARDS.get());
		output.accept(ModItems.GRANITE_SHARDS.get());
		output.accept(ModItems.DIORITE_SHARDS.get());
		output.accept(ModItems.ANDESITE_SHARDS.get());
		output.accept(ModItems.DEEPSLATE_SHARDS.get());
		output.accept(ModItems.TUFF_SHARDS.get());
		output.accept(ModItems.BRICK_SHARDS.get());
		output.accept(ModItems.PACKED_MUD_SHARDS.get());
		output.accept(ModItems.CALCITE_SHARDS.get());
		output.accept(ModItems.SANDSTONE_SHARDS.get());
		output.accept(ModItems.RED_SANDSTONE_SHARDS.get());
		output.accept(ModItems.CINNABAR_SHARDS.get());
		output.accept(ModItems.NETHERRACK_SHARDS.get());
		output.accept(ModItems.NETHER_BRICK_SHARDS.get());
		output.accept(ModItems.RED_NETHER_BRICK_SHARDS.get());
		output.accept(ModItems.QUARTZ_SHARDS.get());
		output.accept(ModItems.GLOWSTONE_SHARDS.get());
		output.accept(ModItems.ANCIENT_DEBRIS_SHARDS.get());
		output.accept(ModItems.BASALT_SHARDS.get());
		output.accept(ModItems.END_STONE_SHARDS.get());
		output.accept(ModItems.PURPUR_SHARDS.get());
		output.accept(ModItems.RAW_IRON_SHARDS.get());
		output.accept(ModItems.RAW_COPPER_SHARDS.get());
		output.accept(ModItems.RAW_GOLD_SHARDS.get());
		output.accept(ModItems.DARK_PRISMARINE_SHARDS.get());
		output.accept(ModItems.PRISMARINE_SHARDS.get());
		output.accept(ModItems.SEA_LANTERN_SHARDS.get());

		output.accept(ModItems.TERRACOTTA_SHARDS.get());
		ModItems.DYED_TERRACOTTA_SHARDS.forEach(supplier -> output.accept(supplier.get()));
		ModItems.GLAZED_TERRACOTTA_SHARDS.forEach(supplier -> output.accept(supplier.get()));
		ModItems.STAINED_GLASS_SHARDS.forEach(supplier -> output.accept(supplier.get()));
	});

	public static void init() {

	}
}
