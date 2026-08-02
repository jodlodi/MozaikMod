package com.mod.mozaik.client.buttons;

import com.mod.mozaik.Voxel;
import com.mod.mozaik.client.widgets.GridWidget;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class VoxelButton extends ModButton implements Voxel {
	public static final int TESSERA_SIZE = 10;

	public final PolyominoWidget polyomino;
	private @Nullable GridWidget grid = null;

	public VoxelButton(PolyominoWidget polyomino, int x, int y) {
		super(x, y, TESSERA_SIZE, TESSERA_SIZE, Component.empty());
		this.polyomino = polyomino;
	}

	@Override
	public int getX() {
		return this.polyomino.getX() + super.getX() * TESSERA_SIZE;
	}

	@Override
	public int getY() {
		return this.polyomino.getY() + super.getY() * TESSERA_SIZE;
	}

	@Override
	public int relativeX() {
		return super.getX();
	}

	@Override
	public int relativeY() {
		return super.getY();
	}

	@Override
	public void onPress(@NonNull InputWithModifiers inputWithModifiers) {
		this.polyomino.screen.selected = this.polyomino;
		this.polyomino.screen.polyominos.remove(this.polyomino);
	}

	public void setGrid(@Nullable GridWidget grid) {
		this.grid = grid;
	}

	public @Nullable GridWidget getGrid() {
		return this.grid;
	}

	@Override
	protected void extractContents(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {

	}
}
