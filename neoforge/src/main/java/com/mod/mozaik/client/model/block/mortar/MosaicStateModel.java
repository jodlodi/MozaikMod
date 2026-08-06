package com.mod.mozaik.client.model.block.mortar;

import com.mod.mozaik.*;
import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.blocks.entities.NeoMortarBlockEntity;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.model.TesseraHelper;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import net.neoforged.neoforge.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.*;

@NullMarked
// The state model representing the baked block state
public final class MosaicStateModel implements DynamicBlockStateModel {
	private final Map<Direction, MortarModelPart> mortarMap = new HashMap<>();

	public MosaicStateModel() {

	}

	// Sets the particle material
	// While it needs to be implemented, any actual logic should be delegated to the level-aware version
	@Override
	public Material.Baked particleMaterial() {
		return this.mortarMap.get(Direction.UP).particleMaterial();
	}

	@Override
	public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		// Override this if you want to use the level to determine what particle to render
		return this.particleMaterial();
	}

	// The flags of the materials backing the quads.
	// While it needs to be implemented, any actual logic should be delegated to the level-aware version
	@Override
	public int materialFlags() {
		return this.mortarMap.get(Direction.UP).materialFlags();
	}

	@Override
	public int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		// Override this if you want to use the level to determine what material flags the model has
		return this.materialFlags();
	}

	// This effectively acts as a key to reuse geometry previous produced. This should generally be as deterministic as possible.
	@Override
	public Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
		return this;
	}

	// Method responsible for collecting the parts to be rendered. Parameters in this method are:
	// - The getter for the blocks and tints, usually the level.
	// - The position of the block to render.
	// - The state of the block.
	// - A random instance.
	// - This list of model parts to be rendered. Add your model parts here.
	@Override
	public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
		// If you want the block rendered to be dependent on the block entity (e.g., your block entity implements `BlockEntity#getModelData`)
		// You can call `BlockAndTintGetter#getModelData` with the block position
		// You can read the property using `get` with the `ModelProperty` key
		// Remember that your block entity should call `BlockEntity#requestModelDataUpdate` to sync the model data to the client

		Direction facing = state.getValue(MortarBlock.FACING);
		parts.add(this.mortarMap.get(facing));

		if (GraphicsRenderHelper.BAKER == null) return;

		ModelData data = level.getModelData(pos);
		List<Polyomino.PlainPolyomino> colorMap = data.get(NeoMortarBlockEntity.PROPERTY);
		if (colorMap == null) return;
		List<Polyomino.PlainPolyomino> copy = new ArrayList<>(colorMap);

		copy.forEach((@Nullable Polyomino.PlainPolyomino polyomino) -> {
			if (polyomino == null) return;
			int x = polyomino.gridX();
			int y = polyomino.gridY();

			int index = -1;
			for (Voxel.PlainVoxel voxel : polyomino.allVoxels()) {
				index++;
				int fx = x + voxel.relativeX();
				int fy = y + voxel.relativeY();

				for (FlatDirection direction : FlatDirection.cardinalClockwise()) {
					if (PolyominoWidget.checkConnection(polyomino, voxel, direction).isPresent()) {
						parts.add(TesseraHelper.bakeBridge(TesseraMaterial.values()[polyomino.color()], facing, direction, fx, fy, polyomino.seed(), index));

						if (PolyominoWidget.checkConnection(polyomino, voxel, direction.clockWise(1)).isEmpty() || PolyominoWidget.checkConnection(polyomino, voxel, direction.clockWise(2)).isEmpty()) {
							parts.add(TesseraHelper.bakeNoCorner(TesseraMaterial.values()[polyomino.color()], facing, direction, fx, fy, polyomino.seed(), index));
						}
					} else {
						parts.add(TesseraHelper.bakeNoBridge(TesseraMaterial.values()[polyomino.color()], facing, direction, fx, fy, polyomino.seed(), index));
					}
				}

				for (FlatDirection direction : FlatDirection.subClockwise()) {
					if (PolyominoWidget.checkConnection(polyomino, voxel, direction).isPresent()) {
						boolean shouldExist = true;
						for (FlatDirection related : direction.getRelated()) {
							if (PolyominoWidget.checkConnection(polyomino, voxel, related).isEmpty())
								shouldExist = false;
						}
						if (!shouldExist) continue;

						parts.add(TesseraHelper.bakeBridge(TesseraMaterial.values()[polyomino.color()], facing, direction, fx, fy, polyomino.seed(), index));
					}
				}

				parts.add(TesseraHelper.bakeTessera(TesseraMaterial.values()[polyomino.color()], facing, fx, fy, polyomino.seed(), index));
			}
		});
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (MosaicStateModel) obj;
		return Objects.equals(this.mortarMap, that.mortarMap);
	}

	@Override
	public int hashCode() {
		return Objects.hash(mortarMap.get(Direction.UP));
	}

	@Override
	public String toString() {
		return "MyBlockStateModel[" +
				"model=" + mortarMap.get(Direction.UP) + ']';
	}

	// The unbaked model that is read from the block state json
	public record Unbaked(Identifier model) implements CustomUnbakedBlockStateModel {

		// The codec to register
		public static final MapCodec<Unbaked> CODEC = RecordCodecBuilder.mapCodec(
				instance -> instance.group(
						Identifier.CODEC.fieldOf("model").forGetter(Unbaked::model)
				).apply(instance, Unbaked::new)
		);
		public static final Identifier ID = Constants.prefix("my_custom_model_loader");

		@Override
		public void resolveDependencies(Resolver resolver) {
			// Mark any models used by the state model
			resolver.markDependency(this.model);
		}

		@Override
		public BlockStateModel bake(ModelBaker baker) {
			GraphicsRenderHelper.BAKER = baker;

			MosaicStateModel blockStateModel = new MosaicStateModel();
			// Bake the model parts and pass into the block state model
			for (Direction direction : Direction.values()) {
				blockStateModel.mortarMap.put(direction, new MortarModelPart.Unbaked(this.model, new MortarModelPart.MyModelState(direction)).bake(baker));
			}
			return blockStateModel;
		}

		@Override
		public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
			return CODEC;
		}
	}
}