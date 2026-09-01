package com.mod.mozaik.reg;

import com.mod.mozaik.items.MortarBlockItem;
import com.mod.mozaik.items.PolyominoItem;
import com.mod.mozaik.items.ShardBagItem;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.items.components.ShardBagContents;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.util.ColorCollection;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.function.Function;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModItems {
	public static final ColorCollection<ResourceSupplier<MortarBlockItem>> MORTARS = registerColoredMosaicItems(ModBlocks.MORTARS, properties -> properties);
	
	public static final ResourceSupplier<ShardItem> STONE_SHARDS = Services.REGISTRY.registerItem("stone_shards", properties -> new ShardItem(properties, ModShardMaterials.STONE));
	public static final ResourceSupplier<ShardItem> BLACKSTONE_SHARDS = Services.REGISTRY.registerItem("blackstone_shards", properties -> new ShardItem(properties, ModShardMaterials.BLACKSTONE));
	public static final ResourceSupplier<ShardItem> GRANITE_SHARDS = Services.REGISTRY.registerItem("granite_shards", properties -> new ShardItem(properties, ModShardMaterials.GRANITE));
	public static final ResourceSupplier<ShardItem> DIORITE_SHARDS = Services.REGISTRY.registerItem("diorite_shards", properties -> new ShardItem(properties, ModShardMaterials.DIORITE));
	public static final ResourceSupplier<ShardItem> ANDESITE_SHARDS = Services.REGISTRY.registerItem("andesite_shards", properties -> new ShardItem(properties, ModShardMaterials.ANDESITE));
	public static final ResourceSupplier<ShardItem> DEEPSLATE_SHARDS = Services.REGISTRY.registerItem("deepslate_shards", properties -> new ShardItem(properties, ModShardMaterials.DEEPSLATE));
	public static final ResourceSupplier<ShardItem> TUFF_SHARDS = Services.REGISTRY.registerItem("tuff_shards", properties -> new ShardItem(properties, ModShardMaterials.TUFF));
	public static final ResourceSupplier<ShardItem> CALCITE_SHARDS = Services.REGISTRY.registerItem("calcite_shards", properties -> new ShardItem(properties, ModShardMaterials.CALCITE));
	public static final ResourceSupplier<ShardItem> DRIPSTONE_SHARDS = Services.REGISTRY.registerItem("dripstone_shards", properties -> new ShardItem(properties, ModShardMaterials.DRIPSTONE));
	public static final ResourceSupplier<ShardItem> MOSSY_SHARDS = Services.REGISTRY.registerItem("mossy_shards", properties -> new ShardItem(properties, ModShardMaterials.MOSSY));
	public static final ResourceSupplier<ShardItem> RESIN_SHARDS = Services.REGISTRY.registerItem("resin_shards", properties -> new ShardItem(properties, ModShardMaterials.RESIN));
	public static final ResourceSupplier<ShardItem> AMETHYST_SHARDS = Services.REGISTRY.registerItem("amethyst_shards", properties -> new ShardItem(properties, ModShardMaterials.AMETHYST));
	public static final ResourceSupplier<ShardItem> BRICK_SHARDS = Services.REGISTRY.registerItem("brick_shards", properties -> new ShardItem(properties, ModShardMaterials.BRICK));
	public static final ResourceSupplier<ShardItem> PACKED_MUD_SHARDS = Services.REGISTRY.registerItem("packed_mud_shards", properties -> new ShardItem(properties, ModShardMaterials.PACKED_MUD));
	public static final ResourceSupplier<ShardItem> SANDSTONE_SHARDS = Services.REGISTRY.registerItem("sandstone_shards", properties -> new ShardItem(properties, ModShardMaterials.SANDSTONE));
	public static final ResourceSupplier<ShardItem> RED_SANDSTONE_SHARDS = Services.REGISTRY.registerItem("red_sandstone_shards", properties -> new ShardItem(properties, ModShardMaterials.RED_SANDSTONE));
	public static final ResourceSupplier<ShardItem> BONE_SHARDS = Services.REGISTRY.registerItem("bone_shards", properties -> new ShardItem(properties, ModShardMaterials.BONE));
	public static final ResourceSupplier<ShardItem> NETHERRACK_SHARDS = Services.REGISTRY.registerItem("netherrack_shards", properties -> new ShardItem(properties, ModShardMaterials.NETHERRACK));
	public static final ResourceSupplier<ShardItem> NETHER_BRICK_SHARDS = Services.REGISTRY.registerItem("nether_brick_shards", properties -> new ShardItem(properties, ModShardMaterials.NETHER_BRICK));
	public static final ResourceSupplier<ShardItem> RED_NETHER_BRICK_SHARDS = Services.REGISTRY.registerItem("red_nether_brick_shards", properties -> new ShardItem(properties, ModShardMaterials.RED_NETHER_BRICK));
	public static final ResourceSupplier<ShardItem> QUARTZ_SHARDS = Services.REGISTRY.registerItem("quartz_shards", properties -> new ShardItem(properties, ModShardMaterials.QUARTZ));
	public static final ResourceSupplier<ShardItem> GLOWSTONE_SHARDS = Services.REGISTRY.registerItem("glowstone_shards", properties -> new ShardItem(properties, ModShardMaterials.GLOWSTONE));
	public static final ResourceSupplier<ShardItem> ANCIENT_DEBRIS_SHARDS = Services.REGISTRY.registerItem("ancient_debris_shards", properties -> new ShardItem(properties, ModShardMaterials.ANCIENT_DEBRIS));
	public static final ResourceSupplier<ShardItem> BASALT_SHARDS = Services.REGISTRY.registerItem("basalt_shards", properties -> new ShardItem(properties, ModShardMaterials.BASALT));
	public static final ResourceSupplier<ShardItem> OBSIDIAN_SHARDS = Services.REGISTRY.registerItem("obsidian_shards", properties -> new ShardItem(properties, ModShardMaterials.OBSIDIAN));
	public static final ResourceSupplier<ShardItem> CRYING_OBSIDIAN_SHARDS = Services.REGISTRY.registerItem("crying_obsidian_shards", properties -> new ShardItem(properties, ModShardMaterials.CRYING_OBSIDIAN));
	public static final ResourceSupplier<ShardItem> END_STONE_SHARDS = Services.REGISTRY.registerItem("end_stone_shards", properties -> new ShardItem(properties, ModShardMaterials.END_STONE));
	public static final ResourceSupplier<ShardItem> PURPUR_SHARDS = Services.REGISTRY.registerItem("purpur_shards", properties -> new ShardItem(properties, ModShardMaterials.PURPUR));
	public static final ResourceSupplier<ShardItem> RAW_IRON_SHARDS = Services.REGISTRY.registerItem("raw_iron_shards", properties -> new ShardItem(properties, ModShardMaterials.RAW_IRON));
	public static final ResourceSupplier<ShardItem> RAW_COPPER_SHARDS = Services.REGISTRY.registerItem("raw_copper_shards", properties -> new ShardItem(properties, ModShardMaterials.RAW_COPPER));
	public static final ResourceSupplier<ShardItem> RAW_GOLD_SHARDS = Services.REGISTRY.registerItem("raw_gold_shards", properties -> new ShardItem(properties, ModShardMaterials.RAW_GOLD));
	public static final ResourceSupplier<ShardItem> DARK_PRISMARINE_SHARDS = Services.REGISTRY.registerItem("dark_prismarine_shards", properties -> new ShardItem(properties, ModShardMaterials.DARK_PRISMARINE));
	public static final ResourceSupplier<ShardItem> PRISMARINE_SHARDS = Services.REGISTRY.registerItem("prismarine_shards", properties -> new ShardItem(properties, ModShardMaterials.PRISMARINE));
	public static final ResourceSupplier<ShardItem> SEA_LANTERN_SHARDS = Services.REGISTRY.registerItem("sea_lantern_shards", properties -> new ShardItem(properties, ModShardMaterials.SEA_LANTERN));

	public static final ResourceSupplier<ShardItem> TERRACOTTA_SHARDS = Services.REGISTRY.registerItem("terracotta_shards", properties -> new ShardItem(properties, ModShardMaterials.TERRACOTTA));
	public static final ColorCollection<ResourceSupplier<ShardItem>> DYED_TERRACOTTA_SHARDS = ColorCollection.zipMap(ModShardMaterials.DYED_TERRACOTTA, ColorCollection.NAMES, (material, name) -> Services.REGISTRY.registerItem(name + "_terracotta_shards", (Item.Properties properties) -> new ShardItem(properties, material)));
	public static final ColorCollection<ResourceSupplier<ShardItem>> GLAZED_TERRACOTTA_SHARDS = ColorCollection.zipMap(ModShardMaterials.GLAZED_TERRACOTTA, ColorCollection.NAMES, (material, name) -> Services.REGISTRY.registerItem(name + "_glazed_terracotta_shards", (Item.Properties properties) -> new ShardItem(properties, material)));
	public static final ColorCollection<ResourceSupplier<ShardItem>> STAINED_GLASS_SHARDS = ColorCollection.zipMap(ModShardMaterials.STAINED_GLASS, ColorCollection.NAMES, (material, name) -> Services.REGISTRY.registerItem(name + "_stained_glass_shards", (Item.Properties properties) -> new ShardItem(properties, material)));

	public static final ResourceSupplier<ShardBagItem> SHARD_BAG = Services.REGISTRY.registerItem("shard_bag", properties -> new ShardBagItem(properties.stacksTo(1).component(ModDataComponents.SHARD_BAG_CONTENTS.get(), ShardBagContents.EMPTY)));

	public static final ResourceSupplier<PolyominoItem> BUTTON_TEMPLATE = Services.REGISTRY.registerItem("button_template", properties -> new PolyominoItem(properties, ModPolyominoShapes.BUTTON));
	public static final ResourceSupplier<PolyominoItem> BONE_TEMPLATE = Services.REGISTRY.registerItem("bone_template", properties -> new PolyominoItem(properties, ModPolyominoShapes.BONE));
	public static final ResourceSupplier<PolyominoItem> BUBBLE_TEMPLATE = Services.REGISTRY.registerItem("bubble_template", properties -> new PolyominoItem(properties, ModPolyominoShapes.BUBBLE));
	public static final ResourceSupplier<PolyominoItem> WORM_TEMPLATE = Services.REGISTRY.registerItem("worm_template", properties -> new PolyominoItem(properties, ModPolyominoShapes.WORM));
	public static final ResourceSupplier<PolyominoItem> CANE_TEMPLATE = Services.REGISTRY.registerItem("cane_template", properties -> new PolyominoItem(properties, ModPolyominoShapes.CANE));
	public static final ResourceSupplier<PolyominoItem> POINT_TEMPLATE = Services.REGISTRY.registerItem("point_template", properties -> new PolyominoItem(properties, ModPolyominoShapes.POINT));
	public static final ResourceSupplier<PolyominoItem> HORN_TEMPLATE = Services.REGISTRY.registerItem("horn_template", properties -> new PolyominoItem(properties, ModPolyominoShapes.HORN));
	public static final ResourceSupplier<PolyominoItem> TREE_TEMPLATE = Services.REGISTRY.registerItem("tree_template", properties -> new PolyominoItem(properties, ModPolyominoShapes.TREE));
	public static final ResourceSupplier<PolyominoItem> FORK_TEMPLATE = Services.REGISTRY.registerItem("fork_template", properties -> new PolyominoItem(properties, ModPolyominoShapes.FORK));

	public static void init() {

	}

	private static <T extends Block> ResourceSupplier<MortarBlockItem> registerMosaicItem(ResourceSupplier<T> block, Function<Item.Properties, Item.Properties> propertiesFunction) {
		return Services.REGISTRY.registerItem(block.id().getPath(), properties -> new MortarBlockItem(block.get(), propertiesFunction.apply(properties)));
	}

	public static <T extends Block> ColorCollection<ResourceSupplier<MortarBlockItem>> registerColoredMosaicItems(ColorCollection<ResourceSupplier<T>> blocks, Function<Item.Properties, Item.Properties> propertiesFunction) {
		return ColorCollection.zipMap(
				ColorCollection.VALUES,
				blocks,
				(color , supplier) -> registerMosaicItem(supplier, propertiesFunction)
		);
	}
}
