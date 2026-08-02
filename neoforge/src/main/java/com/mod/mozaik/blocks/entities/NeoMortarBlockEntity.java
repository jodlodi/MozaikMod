package com.mod.mozaik.blocks.entities;

import com.mod.mozaik.Polyomino;
import com.mod.mozaik.client.model.TesseraHelper;
import com.mod.mozaik.networking.bidirectional.UpdateGlueBidirectional;
import com.mod.mozaik.platform.Services;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.model.data.ModelProperty;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class NeoMortarBlockEntity extends MortarBlockEntity {
	public static final ModelProperty<List<Polyomino.PlainPolyomino>> PROPERTY = new ModelProperty<>();

	public NeoMortarBlockEntity(BlockPos pos, BlockState blockState) {
		super(pos, blockState);
	}

	@Override
	public void setPolyominos(List<Polyomino.PlainPolyomino> polyominos) {
		super.setPolyominos(polyominos);
		if (this.level instanceof ClientLevel clientLevel) {
			this.requestModelDataUpdate();
			clientLevel.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
		}
	}

	@Override
	public void requestModelDataUpdate() {
		super.requestModelDataUpdate();
	}

	@Override
	public ModelData getModelData() {
		return ModelData.builder()
				.with(PROPERTY, this.getPolyominos())
				.build();
	}

	public record ColorMap(Polyomino.PlainPolyomino[][] matrix) {

		public static ColorMap make(List<Polyomino.PlainPolyomino> list) {
			Polyomino.PlainPolyomino[][] matrix = new Polyomino.PlainPolyomino[16][16];
			list.forEach(polyomino -> {
				matrix[polyomino.gridX()][polyomino.gridY()] = polyomino;
				polyomino.allVoxels().forEach(voxel -> {
				});
			});
			return new ColorMap(matrix);
		}
	}
}
