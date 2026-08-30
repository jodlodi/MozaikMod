package com.mod.mozaik.structure.piece;

import com.mod.mozaik.Constants;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;
import java.util.Optional;

@NullMarked
public class CustomStructurePiece extends TemplateStructurePiece {
	private final VerticalPlacement verticalPlacement;
	private final Properties properties;
	protected final StructureTemplateManager structureManager;
	private final BlockPos originalPlacement;
	private final BoundingBox originalBox;

	public CustomStructurePiece(HolderLookup.Provider registries, StructureTemplateManager templateManager, BlockPos pos, VerticalPlacement verticalPlacement, Properties properties, Identifier location, Rotation rotation, Mirror mirror, BlockPos blockPos) {
		super(ModStructurePieces.CUSTOM_STRUCTURE_PIECE.get(), 0, templateManager, location, location.toString(), makeSettings(registries, mirror, rotation, blockPos, properties), pos);
		this.verticalPlacement = verticalPlacement;
		this.properties = properties;

		this.structureManager = templateManager;
		this.originalPlacement = this.templatePosition;
		this.originalBox = clone(this.boundingBox);
	}

	public CustomStructurePiece(StructurePieceSerializationContext context, CompoundTag tag) {
		super(ModStructurePieces.CUSTOM_STRUCTURE_PIECE.get(), tag, context.structureTemplateManager(), (location) -> makeSettings(context.registryAccess(), context.structureTemplateManager(), tag, location));
		this.verticalPlacement = VerticalPlacement.byName(tag.getString("VerticalPlacement").orElseThrow());
		this.properties = Properties.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, tag.get("Properties"))).getOrThrow();

		this.structureManager = context.structureTemplateManager();
		this.originalPlacement = this.templatePosition;
		this.originalBox = clone(this.boundingBox);
	}

	@Override
	protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
		super.addAdditionalSaveData(context, tag);
		tag.putString("Rotation", this.placeSettings.getRotation().name());
		tag.putString("Mirror", this.placeSettings.getMirror().name());
		tag.putString("VerticalPlacement", this.verticalPlacement.getName());
		Properties.CODEC.encodeStart(NbtOps.INSTANCE, this.properties).resultOrPartial(Constants.LOG::error).ifPresent((tag1) -> tag.put("Properties", tag1));
	}

	private static StructurePlaceSettings makeSettings(HolderLookup.Provider registries, StructureTemplateManager templateManager, CompoundTag tag, Identifier location) {
		StructureTemplate structuretemplate = templateManager.getOrCreate(location);
		BlockPos blockpos = new BlockPos(structuretemplate.getSize().getX() / 2, 0, structuretemplate.getSize().getZ() / 2);
		return makeSettings(registries, Mirror.valueOf(tag.getString("Mirror").orElseThrow()), Rotation.valueOf(tag.getString("Rotation").orElseThrow()), blockpos, Properties.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE, tag.get("Properties"))).getOrThrow());
	}

	private static StructurePlaceSettings makeSettings(HolderLookup.Provider registries, Mirror mirror, Rotation rotation, BlockPos pos, Properties properties) {
		HolderLookup.RegistryLookup<Block> blocks = registries.lookupOrThrow(Registries.BLOCK);

		StructurePlaceSettings structurePlaceSettings = new StructurePlaceSettings()
				.setRotation(rotation)
				.setMirror(mirror)
				.setRotationPivot(pos)
				.addProcessor(properties.airPocket ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR)
				.setLiquidSettings(properties.keepLiquids ? LiquidSettings.APPLY_WATERLOGGING : LiquidSettings.IGNORE_WATERLOGGING)
				.addProcessor(new ProtectedBlockProcessor(blocks.getOrThrow(BlockTags.FEATURES_CANNOT_REPLACE)));

		properties.processor().ifPresent(structurePlaceSettings::addProcessor);

		return structurePlaceSettings;
	}

	@Override
	public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator chunkGenerator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {
		super.postProcess(level, manager, chunkGenerator, random, boundingBox, chunkPos, pos);
		this.templatePosition = this.originalPlacement;
		this.boundingBox = clone(this.originalBox);
		this.placeSettings.setBoundingBox(this.boundingBox);
	}

	private static BoundingBox clone(BoundingBox box) {
		return new BoundingBox(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
	}

	public Properties getProperties() {
		return this.properties;
	}

	@Override
	protected void handleDataMarker(String s, BlockPos pos, ServerLevelAccessor levelAccessor, RandomSource random, BoundingBox boundingBox) {
	}

	public record Properties(boolean airPocket, boolean keepLiquids, TerrainAdjustment adjustment, int groundLevelDelta,
							 Optional<CappedProcessor> processor) {
		public static final Codec<Properties> CODEC = RecordCodecBuilder.create((instance) ->
				instance.group(
						Codec.BOOL.fieldOf("air_pocket").forGetter(Properties::airPocket),
						Codec.BOOL.fieldOf("keep_liquids").forGetter(Properties::keepLiquids),
						TerrainAdjustment.CODEC.fieldOf("adjustment").forGetter(Properties::adjustment),
						Codec.INT.fieldOf("ground_level_delta").forGetter(Properties::groundLevelDelta),
						CappedProcessor.MAP_CODEC.codec().optionalFieldOf("processor").forGetter(Properties::processor)
				).apply(instance, Properties::new));
	}

	public enum VerticalPlacement implements StringRepresentable {
		ON_LAND_SURFACE("on_land_surface", Heightmap.Types.WORLD_SURFACE_WG),
		ON_END_SURFACE("on_end_surface", Heightmap.Types.WORLD_SURFACE_WG),
		ON_OCEAN_FLOOR("on_ocean_floor", Heightmap.Types.OCEAN_FLOOR_WG),
		IN_THE_SKY("in_the_sky", Heightmap.Types.WORLD_SURFACE_WG),
		IN_NETHER("in_nether", Heightmap.Types.WORLD_SURFACE_WG),
		BURIED("buried", Heightmap.Types.WORLD_SURFACE_WG),
		SUNKEN("sunken", Heightmap.Types.OCEAN_FLOOR_WG),

		PARTLY_BURIED("partly_buried", Heightmap.Types.WORLD_SURFACE_WG),
		IN_MOUNTAIN("in_mountain", Heightmap.Types.WORLD_SURFACE_WG),
		UNDERGROUND("underground", Heightmap.Types.WORLD_SURFACE_WG);

		public static final EnumCodec<VerticalPlacement> CODEC = StringRepresentable.fromEnum(VerticalPlacement::values);
		private final String name;
		private final Heightmap.Types heightmap;

		VerticalPlacement(String name, Heightmap.Types heightmap) {
			this.name = name;
			this.heightmap = heightmap;
		}

		public String getName() {
			return this.name;
		}

		public static VerticalPlacement byName(String name) {
			return Objects.requireNonNull(CODEC.byName(name));
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}

		public Heightmap.Types getHeightmap() {
			return heightmap;
		}
	}
}