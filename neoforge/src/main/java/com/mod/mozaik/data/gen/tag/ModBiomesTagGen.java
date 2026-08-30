package com.mod.mozaik.data.gen.tag;

import com.mod.mozaik.Constants;
import com.mod.mozaik.reg.ModBlocks;
import com.mod.mozaik.reg.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class ModBiomesTagGen extends BiomeTagsProvider {

	public ModBiomesTagGen(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, Constants.MOD_ID);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void addTags(HolderLookup.Provider provider) {
		this.tag(ModTags.Biomes.HAS_WASTES_MOSAIC).add(
				Biomes.NETHER_WASTES
		);
		this.tag(ModTags.Biomes.HAS_SNOWY_TAIGA_MOSAIC).add(
				Biomes.SNOWY_TAIGA
		);
		this.tag(ModTags.Biomes.HAS_ICE_SPIKES_MOSAIC).add(
				Biomes.ICE_SPIKES
		);
		this.tag(ModTags.Biomes.HAS_DESERT_MOSAIC).add(
				Biomes.DESERT
		);
		this.tag(ModTags.Biomes.HAS_JAGGED_PEAK_MOSAIC).add(
				Biomes.JAGGED_PEAKS
		);
		this.tag(ModTags.Biomes.HAS_LUKEWARM_MOSAIC).add(
				Biomes.LUKEWARM_OCEAN,
				Biomes.DEEP_LUKEWARM_OCEAN
		);
		this.tag(ModTags.Biomes.HAS_RIVER_MOSAIC).add(
				Biomes.RIVER
		);
		this.tag(ModTags.Biomes.HAS_END_MOSAIC).addTag(
				BiomeTags.HAS_END_CITY
		);
		this.tag(ModTags.Biomes.HAS_WARPED_MOSAIC).add(
				Biomes.WARPED_FOREST
		);
		this.tag(ModTags.Biomes.HAS_BADLANDS_MOSAIC).add(
				Biomes.BADLANDS
		);
		this.tag(ModTags.Biomes.HAS_COLD_MOSAIC).add(
				Biomes.COLD_OCEAN,
				Biomes.DEEP_COLD_OCEAN
		);

		this.tag(ModTags.Biomes.HAS_BUTTON_MONUMENT).addTag(
				BiomeTags.HAS_END_CITY
		);
		this.tag(ModTags.Biomes.HAS_BONE_MONUMENT).addTag(
				BiomeTags.HAS_DESERT_PYRAMID
		);
		this.tag(ModTags.Biomes.HAS_BUBBLE_MONUMENT).addTag(
				BiomeTags.IS_DEEP_OCEAN
		);
		this.tag(ModTags.Biomes.HAS_WORM_MONUMENT).addTag(
				BiomeTags.IS_JUNGLE
		);
		this.tag(ModTags.Biomes.HAS_CANE_MONUMENT).addTag(
				BiomeTags.IS_OVERWORLD
		);
		this.tag(ModTags.Biomes.HAS_POINT_MONUMENT).addTag(
				BiomeTags.IS_OVERWORLD
		);
		this.tag(ModTags.Biomes.HAS_HORN_MONUMENT).addTag(
				BiomeTags.IS_NETHER
		);
		this.tag(ModTags.Biomes.HAS_TREE_MONUMENT).addTag(
				BiomeTags.HAS_SWAMP_HUT
		);
		this.tag(ModTags.Biomes.HAS_FORK_MONUMENT).addTag(
				BiomeTags.HAS_VILLAGE_PLAINS
		);
	}
}
