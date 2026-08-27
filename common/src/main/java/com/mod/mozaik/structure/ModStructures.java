package com.mod.mozaik.structure;

import com.mod.mozaik.Constants;
import com.mod.mozaik.structure.piece.CustomStructurePiece;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSpawnOverride;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@NullMarked
public class ModStructures {
	public static final int AIR_IS_AIR = 1;
	public static final int AIR_IS_REPLACED = 0;

	public static final ResourceKey<Structure> END_TEST = registerKey("end_test");

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
		context.register(END_TEST, new CustomTemplateStructure(
				structure(holdergetter.getOrThrow(BiomeTags.HAS_END_CITY), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN),
				new CustomTemplateStructure.Setup(
						CustomStructurePiece.VerticalPlacement.ON_LAND_SURFACE,
						asResourceList("end_test"),
						AIR_IS_AIR,
						1.0F,
						false,
						2
				)
		));
	}

	@Contract("_ -> new")
	public static @Unmodifiable List<Identifier> asResourceList(String... set) {
		return Arrays.stream(set).map(Constants::prefix).toList();
	}
}
