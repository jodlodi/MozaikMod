package com.mod.mozaik.blocks.entities;

import com.mod.mozaik.polyomino.Polyomino;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;
import net.neoforged.neoforge.model.data.ModelProperty;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class NeoMortarBlockEntity extends MortarBlockEntity {
	public static final ModelProperty<List<Polyomino.PlacedPolyomino>> PROPERTY = new ModelProperty<>();

	public NeoMortarBlockEntity(BlockPos pos, BlockState blockState) {
		super(pos, blockState);
	}

	@Override
	public void setPolyomino(List<Polyomino.PlacedPolyomino> polyominos) {
		super.setPolyomino(polyominos);
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
				.with(PROPERTY, this.getPolyomino())
				.build();
	}
}
