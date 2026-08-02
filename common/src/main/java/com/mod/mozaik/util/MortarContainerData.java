package com.mod.mozaik.util;

import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import net.minecraft.world.inventory.ContainerData;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class MortarContainerData implements ContainerData {
	private final MortarBlockEntity blockEntity;

	public MortarContainerData(MortarBlockEntity blockEntity) {
		this.blockEntity = blockEntity;
	}

	@Override
	public int get(int i) {
		return switch (i) {
			case 0 -> this.blockEntity.getBlockPos().getX();
			case 1 -> this.blockEntity.getBlockPos().getY();
			case 2 -> this.blockEntity.getBlockPos().getZ();
			default -> this.blockEntity.getPolyominos().size();
		};
	}

	@Override
	public void set(int i, int i1) {

	}

	@Override
	public int getCount() {
		return 3;
	}
}
