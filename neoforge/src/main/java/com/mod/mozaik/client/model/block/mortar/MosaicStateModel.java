package com.mod.mozaik.client.model.block.mortar;

import com.mod.mozaik.Constants;
import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.blocks.entities.NeoMortarBlockEntity;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.model.TesseraHelper;
import com.mod.mozaik.polyomino.IPolyominoHolder;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.util.FlatDirection;
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
import org.joml.Vector2i;
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
		List<IPolyominoHolder.PlacedPolyomino> colorMap = data.get(NeoMortarBlockEntity.PROPERTY);
		if (colorMap == null) return;
		List<IPolyominoHolder.PlacedPolyomino> copy = new ArrayList<>(colorMap);

		copy.forEach((@Nullable IPolyominoHolder.PlacedPolyomino polyomino) -> {
			if (polyomino == null) return;
			int x = polyomino.x();
			int y = polyomino.y();
			List<Vector2i> dirs = polyomino.polyomino().placedTessera().stream().map(tessera -> new Vector2i(tessera.x(), tessera.y())).toList();

			int index = -1;
			for (Polyomino.PlacedTessera tessera : polyomino.polyomino().placedTessera()) {
				index++;
				int fx = x + tessera.x();
				int fy = y + tessera.y();

				for (FlatDirection direction : FlatDirection.cardinalClockwise()) {
					if (Polyomino.Builder.checkConnection(dirs, new Vector2i(tessera.x(), tessera.y()), direction)) {
						parts.add(TesseraHelper.bakeBridge(polyomino.polyomino().material(), facing, direction, fx, fy, polyomino.polyomino().seed(), index));

						if (!Polyomino.Builder.checkConnection(dirs, new Vector2i(tessera.x(), tessera.y()), direction.clockWise(1)) || !Polyomino.Builder.checkConnection(dirs, new Vector2i(tessera.x(), tessera.y()), direction.clockWise(2))) {
							parts.add(TesseraHelper.bakeNoCorner(polyomino.polyomino().material(), facing, direction, fx, fy, polyomino.polyomino().seed(), index));
						}
					} else {
						parts.add(TesseraHelper.bakeNoBridge(polyomino.polyomino().material(), facing, direction, fx, fy, polyomino.polyomino().seed(), index));
					}
				}

				for (FlatDirection direction : FlatDirection.subClockwise()) {
					if (Polyomino.Builder.checkConnection(dirs, new Vector2i(tessera.x(), tessera.y()), direction)) {
						boolean shouldExist = true;
						for (FlatDirection related : direction.getRelated()) {
							if (!Polyomino.Builder.checkConnection(dirs, new Vector2i(tessera.x(), tessera.y()), related))
								shouldExist = false;
						}
						if (!shouldExist) continue;

						parts.add(TesseraHelper.bakeBridge(polyomino.polyomino().material(), facing, direction, fx, fy, polyomino.polyomino().seed(), index));
					}
				}

				parts.add(TesseraHelper.bakeTessera(polyomino.polyomino().material(), facing, fx, fy, polyomino.polyomino().seed(), index));
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