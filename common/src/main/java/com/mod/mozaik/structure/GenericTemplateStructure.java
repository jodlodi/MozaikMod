package com.mod.mozaik.structure;

import com.google.common.collect.ImmutableList;
import com.mod.mozaik.structure.piece.CustomStructurePiece;
import com.mod.mozaik.util.ModUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.CappedProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.List;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class GenericTemplateStructure extends Structure {
	protected final List<Setup> setups;

	public GenericTemplateStructure(StructureSettings settings, List<Setup> setups) {
		super(settings);
		this.setups = setups;
	}

	public GenericTemplateStructure(StructureSettings settings, Setup setup) {
		this(settings, List.of(setup));
	}

	@Override
	public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
		WorldgenRandom random = context.random();
		Setup pickedSetup = null;
		if (this.setups.size() > 1) {
			float f = 0.0F;

			for (Setup setup : this.setups) {
				f += setup.weight();
			}

			float weight = random.nextFloat();

			for (Setup setup : this.setups) {
				weight -= setup.weight() / f;
				if (weight < 0.0F) {
					pickedSetup = setup;
					break;
				}
			}
		} else {
			pickedSetup = this.setups.getFirst();
		}

		if (pickedSetup == null) {
			throw new IllegalStateException();
		} else {
			Setup finalSetup = pickedSetup;

			CustomStructurePiece.Properties properties = new CustomStructurePiece.Properties(
					sample(random, finalSetup.air_pocket_probability()),
					finalSetup.keepLiquids,
					this.terrainAdaptation(),
					finalSetup.groundLevelDelta(),
					finalSetup.processor()
			);

			ResourceLocation location = this.selectTemplate(finalSetup, random);

			StructureTemplate template = context.structureTemplateManager().getOrCreate(location);
			Rotation rotation = ModUtil.getRandom(Rotation.values(), random);
			Mirror mirror = Mirror.NONE;
			BlockPos pivot = new BlockPos(template.getSize().getX() / 2, 0, template.getSize().getZ() / 2);
			BoundingBox boundingBox = template.getBoundingBox(context.chunkPos().getWorldPosition(), rotation, pivot, mirror);
			BlockPos finalPos = this.adjustPos(finalSetup, context, properties, boundingBox).orElse(null);

			if (finalPos == null) return Optional.empty();

			return Optional.of(new GenerationStub(finalPos, builder ->
					this.putAndPlace(builder, finalPos, context, finalSetup, properties, location, rotation, mirror, pivot))
			);
		}
	}

	protected ResourceLocation selectTemplate(Setup finalSetup, WorldgenRandom random) {
		if (finalSetup.structure_locations().size() == 1) return finalSetup.structure_locations().getFirst();
		return finalSetup.structure_locations().get(random.nextInt(finalSetup.structure_locations().size()));
	}

	protected void putAndPlace(StructurePiecesBuilder builder, BlockPos finalPos, GenerationContext context, Setup finalSetup, CustomStructurePiece.Properties properties, ResourceLocation location, Rotation rotation, Mirror mirror, BlockPos pivot) {
		builder.addPiece(new CustomStructurePiece(context.structureTemplateManager(), finalPos, finalSetup.placement(), properties, location, rotation, mirror, pivot));
	}

	protected static boolean sample(WorldgenRandom random, float probability) {
		if (probability == 0.0F) return false;
		else if (probability == 1.0F) return true;
		else return random.nextFloat() < probability;
	}

	public Optional<BlockPos> adjustPos(Setup finalSetup, GenerationContext context, CustomStructurePiece.Properties properties, BoundingBox boundingBox) {
		WorldgenRandom random = context.random();

		ChunkGenerator chunkGenerator = context.chunkGenerator();
		LevelHeightAccessor heightAccessor = context.heightAccessor();
		RandomState randomState = context.randomState();
		BlockPos center = boundingBox.getCenter();
		int baseHeight = chunkGenerator.getBaseHeight(center.getX(), center.getZ(), finalSetup.placement().getHeightmap(), heightAccessor, randomState);
		int suitableY = this.findSuitableY(random, chunkGenerator, finalSetup, properties.airPocket(), baseHeight, boundingBox.getYSpan(), boundingBox, heightAccessor, randomState);
		if (finalSetup.placement == CustomStructurePiece.VerticalPlacement.ON_END_SURFACE && suitableY <= 20)
			return Optional.empty();
		return Optional.of(new BlockPos(center.getX(), suitableY, center.getZ()));
	}

	protected int findSuitableY(RandomSource random, ChunkGenerator chunkGenerator, Setup setup, boolean airPocket, int baseHeight, int ySpan, BoundingBox boundingBox, LevelHeightAccessor heightAccessor, RandomState randomState) {
		int height = heightAccessor.getMinBuildHeight() + 15;
		int finalHeight;
		switch (setup.placement) {
			case SUNKEN ->
					finalHeight = Math.min(chunkGenerator.getSeaLevel() - ySpan - 1, baseHeight + setup.depth.orElse(0));
			case BURIED -> finalHeight = Math.max(chunkGenerator.getSeaLevel() + 1, baseHeight) + setup.depth.orElse(0);
			case IN_THE_SKY -> {
				int top = heightAccessor.getMaxBuildHeight();
				return baseHeight + Mth.randomBetweenInclusive(random, (top - baseHeight) / 5, (top - baseHeight) / 4);
			}
			case IN_NETHER -> {
				if (airPocket) {
					finalHeight = Mth.randomBetweenInclusive(random, 32, 100);
				} else if (random.nextFloat() < 0.5F) {
					finalHeight = Mth.randomBetweenInclusive(random, 27, 29);
				} else {
					finalHeight = Mth.randomBetweenInclusive(random, 29, 100);
				}
			}
			case IN_MOUNTAIN -> finalHeight = getRandomWithinInterval(random, 70, baseHeight - ySpan);
			case UNDERGROUND -> finalHeight = getRandomWithinInterval(random, height, baseHeight - ySpan);
			case PARTLY_BURIED -> finalHeight = baseHeight - ySpan + Mth.randomBetweenInclusive(random, 2, 8);
			default -> finalHeight = baseHeight + setup.depth.orElse(0);
		}

		List<BlockPos> list1 = ImmutableList.of(new BlockPos(boundingBox.minX(), 0, boundingBox.minZ()), new BlockPos(boundingBox.maxX(), 0, boundingBox.minZ()), new BlockPos(boundingBox.minX(), 0, boundingBox.maxZ()), new BlockPos(boundingBox.maxX(), 0, boundingBox.maxZ()));
		List<NoiseColumn> list = list1.stream().map((blockPos) -> chunkGenerator.getBaseColumn(blockPos.getX(), blockPos.getZ(), heightAccessor, randomState)).toList();
		Heightmap.Types types = setup.placement.getHeightmap();

		int suitableY;
		for (suitableY = finalHeight; suitableY > height; --suitableY) {
			int i1 = 0;

			for (NoiseColumn noisecolumn : list) {
				BlockState blockstate = noisecolumn.getBlock(suitableY);
				if (types.isOpaque().test(blockstate)) {
					++i1;
					if (i1 == 3) {
						return suitableY;
					}
				}
			}
		}

		return suitableY;
	}

	private static int getRandomWithinInterval(RandomSource randomSource, int min, int max) {
		return min < max ? Mth.randomBetweenInclusive(randomSource, min, max) : max;
	}

	public record Setup(CustomStructurePiece.VerticalPlacement placement, List<ResourceLocation> structure_locations,
						float air_pocket_probability, float weight, boolean keepLiquids, int groundLevelDelta,
						Optional<Integer> depth, Optional<CappedProcessor> processor) {
		public static final Codec<Setup> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
				CustomStructurePiece.VerticalPlacement.CODEC.fieldOf("placement").forGetter(Setup::placement),
				ExtraCodecs.nonEmptyList(ResourceLocation.CODEC.listOf()).fieldOf("structure_locations").forGetter(Setup::structure_locations),
				Codec.floatRange(0.0F, 1.0F).fieldOf("air_pocket_probability").forGetter(Setup::air_pocket_probability),
				ExtraCodecs.POSITIVE_FLOAT.fieldOf("weight").forGetter(Setup::weight),
				Codec.BOOL.fieldOf("keep_liquids").forGetter(Setup::keepLiquids),
				Codec.INT.fieldOf("ground_level_delta").forGetter(Setup::groundLevelDelta),
				Codec.INT.optionalFieldOf("depth").forGetter(Setup::depth),
				CappedProcessor.CODEC.codec().optionalFieldOf("processor").forGetter(Setup::processor)
		).apply(instance, Setup::new));

		public Setup(CustomStructurePiece.VerticalPlacement placement, List<ResourceLocation> structure_locations, float air_pocket_probability, float weight, boolean keepLiquids, int groundLevelDelta) {
			this(placement, structure_locations, air_pocket_probability, weight, keepLiquids, groundLevelDelta, Optional.empty(), Optional.empty());
		}
	}
}