package com.mod.mozaik.structure;

import com.mod.mozaik.Constants;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.List;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModStructureSets {
	public static final ResourceKey<StructureSet> MOSAICS = registerKey("mosaics");
	public static final ResourceKey<StructureSet> MONUMENTS = registerKey("monuments");

	private static ResourceKey<StructureSet> registerKey(String name) {
		return ResourceKey.create(Registries.STRUCTURE_SET, Constants.prefix(name));
	}

	public static void bootstrap(BootstrapContext<StructureSet> context) {
		HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

		context.register(MOSAICS, new StructureSet(List.of(
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.UNKNOWN_BADLANDS_MOSAIC), 1),
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.UNKNOWN_COLD_MOSAIC), 1),
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.UNKNOWN_DESERT_MOSAIC), 1),
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.UNKNOWN_END_MOSAIC), 1),
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.UNKNOWN_LUKEWARM_MOSAIC), 1),
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.UNKNOWN_ICE_SPIKES_MOSAIC), 1),
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.UNKNOWN_JAGGED_PEAK_MOSAIC), 1),
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.UNKNOWN_RIVER_MOSAIC), 1),
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.UNKNOWN_SNOWY_TAIGA_MOSAIC), 1),
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.UNKNOWN_WARPED_MOSAIC), 1),
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.UNKNOWN_WASTES_MOSAIC), 1)
		), createRandomSpread(120, 54, RandomSpreadType.LINEAR, 512814683, Optional.empty())));

		context.register(MONUMENTS, new StructureSet(List.of(
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.BUTTON_MONUMENT), 1),
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.BONE_MONUMENT), 1),
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.BUBBLE_MONUMENT), 1),
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.WORM_MONUMENT), 1),
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.CANE_MONUMENT), 1),
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.POINT_MONUMENT), 1),
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.HORN_MONUMENT), 1),
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.TREE_MONUMENT), 1),
				new StructureSet.StructureSelectionEntry(structures.getOrThrow(ModStructures.FORK_MONUMENT), 1)
		), createRandomSpread(40, 18, RandomSpreadType.LINEAR, 1898254337, Optional.empty())));
	}

	@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "deprecation"})
	public static RandomSpreadStructurePlacement createRandomSpread(int spacing, int separation, RandomSpreadType spreadType, int salt, Optional<StructurePlacement.ExclusionZone> zone) {
		return new RandomSpreadStructurePlacement(Vec3i.ZERO, StructurePlacement.FrequencyReductionMethod.DEFAULT, 1.0F, salt, zone, spacing, separation, spreadType);
	}
}
