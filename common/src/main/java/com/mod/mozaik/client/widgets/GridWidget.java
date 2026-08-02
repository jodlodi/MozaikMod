package com.mod.mozaik.client.widgets;

import com.mod.mozaik.Voxel;
import com.mod.mozaik.client.buttons.VoxelButton;
import net.minecraft.client.input.MouseButtonEvent;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.Optional;

public class GridWidget extends UnclickableWidget implements Voxel {
	private @Nullable VoxelButton voxel;
	private final int relativeX;
	private final int relativeY;

	public GridWidget(int x, int y, int relativeX, int relativeY) {
		super(x, y, VoxelButton.TESSERA_SIZE, VoxelButton.TESSERA_SIZE);
		this.relativeX = relativeX;
		this.relativeY = relativeY;
	}

	@Override
	public int relativeX() {
		return this.relativeX;
	}

	@Override
	public int relativeY() {
		return this.relativeY;
	}

	public @Nullable VoxelButton getVoxel() {
		return this.voxel;
	}

	public void setVoxel(@Nullable VoxelButton voxel) {
		this.voxel = voxel;
	}

	@Override
	public boolean mouseClicked(@NonNull MouseButtonEvent event, boolean doubleClick) {
		if (this.voxel == null) return false;
		if (this.voxel.mouseClicked(event, doubleClick)) {
			this.voxel.polyomino.voxels.forEach(button -> {
				Optional.ofNullable(button.getGrid()).ifPresent(gridWidget -> gridWidget.setVoxel(null));
				button.setGrid(null);
			});
			return true;
		}
		return false;
	}
}
