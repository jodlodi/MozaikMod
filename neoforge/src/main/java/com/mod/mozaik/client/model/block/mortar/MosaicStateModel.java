package com.mod.mozaik.client.model.block.mortar;

import com.mod.mozaik.Constants;
import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.blocks.entities.NeoMortarBlockEntity;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.model.TesseraHelper;
import com.mod.mozaik.menus.MortarMenu;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.Tessera;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.DynamicBlockStateModel;
import net.neoforged.neoforge.client.model.block.CustomUnbakedBlockStateModel;
import net.neoforged.neoforge.model.data.ModelData;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

import java.util.*;

@NullMarked
public final class MosaicStateModel implements DynamicBlockStateModel {
	private final Map<Direction, MortarModelPart> mortarMap = new HashMap<>();

	public MosaicStateModel() {

	}

	@Override
	public Material.Baked particleMaterial() {
		return this.mortarMap.get(Direction.UP).particleMaterial();
	}

	@Override
	public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return this.particleMaterial();
	}

	@Override
	public int materialFlags() {
		return this.mortarMap.get(Direction.UP).materialFlags();
	}

	@Override
	public int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state) {
		return this.materialFlags();
	}

	@Override
	public Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
		return this;
	}

	@Override
	public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockStateModelPart> parts) {
		Direction facing = state.getValue(MortarBlock.FACING_ROTATED).getDirection();
		parts.add(this.mortarMap.get(facing));

		if (GraphicsRenderHelper.BAKER == null) return;

		ModelData data = level.getModelData(pos);
		List<Polyomino.PlacedPolyomino> input = data.get(NeoMortarBlockEntity.PROPERTY);
		if (input == null) return;
		List<Polyomino.PlacedPolyomino> copy = new ArrayList<>();

		Rotation blockRotation = state.getValue(MortarBlock.FACING_ROTATED).getRotation();
		input.forEach(placedPolyomino -> copy.add(MortarMenu.rotate(placedPolyomino, blockRotation)));

		ClientLevel clientLevel = Minecraft.getInstance().level;
		if (clientLevel == null) return;

		copy.forEach((@Nullable Polyomino.PlacedPolyomino polyomino) -> {
			if (polyomino == null) return;
			int x = polyomino.x();
			int y = polyomino.y();


			int index = -1;
			for (Tessera.PlacedTessera tessera : polyomino.polyomino().placedTessera()) {
				index++;
				int fx = x + tessera.x();
				int fy = y + tessera.y();
				parts.add(TesseraHelper.bakeTessera(clientLevel.registryAccess().get(polyomino.polyomino().material()).orElseThrow().value(), polyomino.polyomino().material().identifier().getPath(), facing, fx, fy, polyomino.polyomino().uuid().getMostSignificantBits(), index, tessera.tessera().shape()));
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

	public record Unbaked(Identifier model) implements CustomUnbakedBlockStateModel {

		public static final MapCodec<Unbaked> CODEC = RecordCodecBuilder.mapCodec(
				instance -> instance.group(
						Identifier.CODEC.fieldOf("model").forGetter(Unbaked::model)
				).apply(instance, Unbaked::new)
		);
		public static final Identifier ID = Constants.prefix("my_custom_model_loader");

		@Override
		public void resolveDependencies(Resolver resolver) {
			resolver.markDependency(this.model);
		}

		@Override
		public BlockStateModel bake(ModelBaker baker) {
			GraphicsRenderHelper.BAKER = baker;

			MosaicStateModel blockStateModel = new MosaicStateModel();
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