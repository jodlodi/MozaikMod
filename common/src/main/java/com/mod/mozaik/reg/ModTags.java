package com.mod.mozaik.reg;

import com.mod.mozaik.Constants;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModTags {
	public static class Blocks {
		public static final TagKey<Block> MORTARS = TagKey.create(Registries.BLOCK, Constants.prefix("mortars"));
	}

	public static class Items {
		public static final TagKey<Item> SHARDS = TagKey.create(Registries.ITEM, Constants.prefix("shards"));
		public static final TagKey<Item> MORTARS = TagKey.create(Registries.ITEM, Constants.prefix("mortars"));
	}

	public static class Biomes {
		public static final TagKey<Biome> HAS_WASTES_MOSAIC = TagKey.create(Registries.BIOME, Constants.prefix("has_wastes_mosaic"));
		public static final TagKey<Biome> HAS_SNOWY_TAIGA_MOSAIC = TagKey.create(Registries.BIOME, Constants.prefix("has_snowy_taiga_mosaic"));
		public static final TagKey<Biome> HAS_ICE_SPIKES_MOSAIC = TagKey.create(Registries.BIOME, Constants.prefix("has_ice_spikes_mosaic"));
		public static final TagKey<Biome> HAS_DESERT_MOSAIC = TagKey.create(Registries.BIOME, Constants.prefix("has_desert_mosaic"));
		public static final TagKey<Biome> HAS_JAGGED_PEAK_MOSAIC = TagKey.create(Registries.BIOME, Constants.prefix("has_jagged_peak_mosaic"));
		public static final TagKey<Biome> HAS_LUKEWARM_MOSAIC = TagKey.create(Registries.BIOME, Constants.prefix("has_lukewarm_mosaic"));
		public static final TagKey<Biome> HAS_RIVER_MOSAIC = TagKey.create(Registries.BIOME, Constants.prefix("has_river_mosaic"));
		public static final TagKey<Biome> HAS_END_MOSAIC = TagKey.create(Registries.BIOME, Constants.prefix("has_end_mosaic"));
		public static final TagKey<Biome> HAS_WARPED_MOSAIC = TagKey.create(Registries.BIOME, Constants.prefix("has_warped_mosaic"));
		public static final TagKey<Biome> HAS_BADLANDS_MOSAIC = TagKey.create(Registries.BIOME, Constants.prefix("has_badlands_mosaic"));
		public static final TagKey<Biome> HAS_COLD_MOSAIC = TagKey.create(Registries.BIOME, Constants.prefix("has_cold_mosaic"));
		
		public static final TagKey<Biome> HAS_BUTTON_MONUMENT = TagKey.create(Registries.BIOME, Constants.prefix("has_button_monument"));
		public static final TagKey<Biome> HAS_BONE_MONUMENT = TagKey.create(Registries.BIOME, Constants.prefix("has_bone_monument"));
		public static final TagKey<Biome> HAS_BUBBLE_MONUMENT = TagKey.create(Registries.BIOME, Constants.prefix("has_bubble_monument"));
		public static final TagKey<Biome> HAS_WORM_MONUMENT = TagKey.create(Registries.BIOME, Constants.prefix("has_worm_monument"));
		public static final TagKey<Biome> HAS_CANE_MONUMENT = TagKey.create(Registries.BIOME, Constants.prefix("has_cane_monument"));
		public static final TagKey<Biome> HAS_POINT_MONUMENT = TagKey.create(Registries.BIOME, Constants.prefix("has_point_monument"));
		public static final TagKey<Biome> HAS_HORN_MONUMENT = TagKey.create(Registries.BIOME, Constants.prefix("has_horn_monument"));
		public static final TagKey<Biome> HAS_TREE_MONUMENT = TagKey.create(Registries.BIOME, Constants.prefix("has_tree_monument"));
		public static final TagKey<Biome> HAS_FORK_MONUMENT = TagKey.create(Registries.BIOME, Constants.prefix("has_fork_monument"));
	}
}
