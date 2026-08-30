package com.mod.mozaik.structure;

import com.mod.mozaik.Constants;
import com.mod.mozaik.reg.ModLootTables;
import com.mod.mozaik.reg.ModTags;
import com.mod.mozaik.structure.piece.CustomStructurePiece;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.AppendLoot;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@NullMarked
public class ModStructures {
	public static final int AIR_IS_AIR = 1;
	public static final int AIR_IS_REPLACED = 0;

	public static final ResourceKey<Structure> UNKNOWN_WASTES_MOSAIC = registerKey("unknown_wastes_mosaic");
	public static final ResourceKey<Structure> UNKNOWN_SNOWY_TAIGA_MOSAIC = registerKey("unknown_snowy_taiga_mosaic");
	public static final ResourceKey<Structure> UNKNOWN_ICE_SPIKES_MOSAIC = registerKey("unknown_ice_spikes_mosaic");
	public static final ResourceKey<Structure> UNKNOWN_DESERT_MOSAIC = registerKey("unknown_desert_mosaic");
	public static final ResourceKey<Structure> UNKNOWN_JAGGED_PEAK_MOSAIC = registerKey("unknown_jagged_peak_mosaic");
	public static final ResourceKey<Structure> UNKNOWN_LUKEWARM_MOSAIC = registerKey("unknown_lukewarm_mosaic");
	public static final ResourceKey<Structure> UNKNOWN_RIVER_MOSAIC = registerKey("unknown_river_mosaic");
	public static final ResourceKey<Structure> UNKNOWN_END_MOSAIC = registerKey("unknown_end_mosaic");
	public static final ResourceKey<Structure> UNKNOWN_WARPED_MOSAIC = registerKey("unknown_warped_mosaic");
	public static final ResourceKey<Structure> UNKNOWN_BADLANDS_MOSAIC = registerKey("unknown_badlands_mosaic");
	public static final ResourceKey<Structure> UNKNOWN_COLD_MOSAIC = registerKey("unknown_cold_mosaic");

	public static ResourceKey<Structure> registerKey(String name) {
		return ResourceKey.create(Registries.STRUCTURE, Constants.prefix(name));
	}

	private static Structure.StructureSettings structure(HolderSet<Biome> biomes, Map<MobCategory, StructureSpawnOverride> mobOverrides, GenerationStep.Decoration step, TerrainAdjustment adjustment) {
		return new Structure.StructureSettings(biomes, mobOverrides, step, adjustment);
	}

	private static Structure.StructureSettings structure(HolderSet<Biome> biomes, GenerationStep.Decoration step, TerrainAdjustment adjustment) {
		return structure(biomes, Map.of(), step, adjustment);
	}

	public static void bootstrap(BootstrapContext<Structure> context) {
		HolderGetter<Biome> holdergetter = context.lookup(Registries.BIOME);

		context.register(UNKNOWN_WASTES_MOSAIC, new CustomTemplateStructure(
				structure(holdergetter.getOrThrow(ModTags.Biomes.HAS_WASTES_MOSAIC), GenerationStep.Decoration.UNDERGROUND_STRUCTURES, TerrainAdjustment.BEARD_THIN),
				new CustomTemplateStructure.Setup(
						CustomStructurePiece.VerticalPlacement.IN_NETHER,
						asResourceList("mosaic/unknown_wastes_mosaic"),
						AIR_IS_AIR,
						1.0F,
						true,
						1
				)
		));
		context.register(UNKNOWN_SNOWY_TAIGA_MOSAIC, new CustomTemplateStructure(
				structure(holdergetter.getOrThrow(ModTags.Biomes.HAS_SNOWY_TAIGA_MOSAIC), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN),
				new CustomTemplateStructure.Setup(
						CustomStructurePiece.VerticalPlacement.ON_LAND_SURFACE,
						asResourceList("mosaic/unknown_snowy_taiga_mosaic"),
						AIR_IS_AIR,
						1.0F,
						true,
						1
				)
		));
		context.register(UNKNOWN_ICE_SPIKES_MOSAIC, new CustomTemplateStructure(
				structure(holdergetter.getOrThrow(ModTags.Biomes.HAS_ICE_SPIKES_MOSAIC), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN),
				new CustomTemplateStructure.Setup(
						CustomStructurePiece.VerticalPlacement.ON_LAND_SURFACE,
						asResourceList("mosaic/unknown_ice_spikes_mosaic"),
						AIR_IS_AIR,
						1.0F,
						true,
						1
				)
		));
		context.register(UNKNOWN_DESERT_MOSAIC, new CustomTemplateStructure(
				structure(holdergetter.getOrThrow(ModTags.Biomes.HAS_DESERT_MOSAIC), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN),
				new CustomTemplateStructure.Setup(
						CustomStructurePiece.VerticalPlacement.ON_LAND_SURFACE,
						asResourceList("mosaic/unknown_desert_mosaic"),
						AIR_IS_AIR,
						1.0F,
						true,
						1,
						Optional.empty(),
						Optional.of(archyRuleProcessor(Blocks.SAND, Blocks.SUSPICIOUS_SAND, ModLootTables.DESERT_ARCHAEOLOGY))
				)
		));
		context.register(UNKNOWN_JAGGED_PEAK_MOSAIC, new CustomTemplateStructure(
				structure(holdergetter.getOrThrow(ModTags.Biomes.HAS_JAGGED_PEAK_MOSAIC), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN),
				new CustomTemplateStructure.Setup(
						CustomStructurePiece.VerticalPlacement.ON_LAND_SURFACE,
						asResourceList("mosaic/unknown_jagged_peak_mosaic"),
						AIR_IS_AIR,
						1.0F,
						true,
						1
				)
		));
		context.register(UNKNOWN_LUKEWARM_MOSAIC, new CustomTemplateStructure(
				structure(holdergetter.getOrThrow(ModTags.Biomes.HAS_LUKEWARM_MOSAIC), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN),
				new CustomTemplateStructure.Setup(
						CustomStructurePiece.VerticalPlacement.ON_OCEAN_FLOOR,
						asResourceList("mosaic/unknown_lukewarm_mosaic"),
						AIR_IS_AIR,
						1.0F,
						true,
						1,
						Optional.empty(),
						Optional.of(archyRuleProcessor(Blocks.SAND, Blocks.SUSPICIOUS_SAND, ModLootTables.LUKEWARM_MOSAIC_ARCHAEOLOGY))
				)
		));
		context.register(UNKNOWN_RIVER_MOSAIC, new CustomTemplateStructure(
				structure(holdergetter.getOrThrow(ModTags.Biomes.HAS_RIVER_MOSAIC), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN),
				new CustomTemplateStructure.Setup(
						CustomStructurePiece.VerticalPlacement.ON_LAND_SURFACE,
						asResourceList("mosaic/unknown_river_mosaic"),
						AIR_IS_AIR,
						1.0F,
						true,
						1
				)
		));
		context.register(UNKNOWN_END_MOSAIC, new CustomTemplateStructure(
				structure(holdergetter.getOrThrow(ModTags.Biomes.HAS_END_MOSAIC), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN),
				new CustomTemplateStructure.Setup(
						CustomStructurePiece.VerticalPlacement.ON_END_SURFACE,
						asResourceList("mosaic/unknown_end_mosaic"),
						AIR_IS_AIR,
						1.0F,
						true,
						1
				)
		));
		context.register(UNKNOWN_WARPED_MOSAIC, new CustomTemplateStructure(
				structure(holdergetter.getOrThrow(ModTags.Biomes.HAS_WARPED_MOSAIC), GenerationStep.Decoration.UNDERGROUND_STRUCTURES, TerrainAdjustment.BEARD_THIN),
				new CustomTemplateStructure.Setup(
						CustomStructurePiece.VerticalPlacement.IN_NETHER,
						asResourceList("mosaic/unknown_warped_mosaic"),
						AIR_IS_AIR,
						1.0F,
						true,
						1
				)
		));
		context.register(UNKNOWN_BADLANDS_MOSAIC, new CustomTemplateStructure(
				structure(holdergetter.getOrThrow(ModTags.Biomes.HAS_BADLANDS_MOSAIC), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN),
				new CustomTemplateStructure.Setup(
						CustomStructurePiece.VerticalPlacement.ON_LAND_SURFACE,
						asResourceList("mosaic/unknown_badlands_mosaic"),
						AIR_IS_AIR,
						1.0F,
						true,
						1
				)
		));
		context.register(UNKNOWN_COLD_MOSAIC, new CustomTemplateStructure(
				structure(holdergetter.getOrThrow(ModTags.Biomes.HAS_COLD_MOSAIC), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN),
				new CustomTemplateStructure.Setup(
						CustomStructurePiece.VerticalPlacement.ON_OCEAN_FLOOR,
						asResourceList("mosaic/unknown_cold_mosaic"),
						AIR_IS_AIR,
						1.0F,
						true,
						1,
						Optional.empty(),
						Optional.of(archyRuleProcessor(Blocks.GRAVEL, Blocks.SUSPICIOUS_GRAVEL, ModLootTables.COLD_MOSAIC_ARCHAEOLOGY))
				)
		));
	}

	private static CappedProcessor archyRuleProcessor(Block candidateBlock, Block replacementBlock, ResourceKey<LootTable> lootTable) {
		return new CappedProcessor(new RuleProcessor(List.of(new ProcessorRule(new BlockMatchTest(candidateBlock), AlwaysTrueTest.INSTANCE, PosAlwaysTrueTest.INSTANCE, replacementBlock.defaultBlockState(), new AppendLoot(lootTable)))), ConstantInt.of(5));
	}

	@Contract("_ -> new")
	public static @Unmodifiable List<Identifier> asResourceList(String... set) {
		return Arrays.stream(set).map(Constants::prefix).toList();
	}
}
