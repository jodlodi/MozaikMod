package com.mod.mozaik.client.buttons;

import com.mod.mozaik.Polyomino;
import com.mod.mozaik.TesseraMaterial;
import com.mod.mozaik.Voxel;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import org.joml.Vector2f;
import org.jspecify.annotations.NonNull;

import java.util.Objects;

public class CreatePolyominoButton extends ModButton {
	public final static int SIZE = 32;
	private final Polyomino.PlainPolyomino template;
	private final MortarScreen screen;
	private int color = 0;

	public CreatePolyominoButton(int x, int y, MortarScreen screen, Polyomino.PlainPolyomino template) {
		super(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE, Component.empty());
		this.template = template;
		this.screen = screen;
	}

	@Override
	public void onPress(@NonNull InputWithModifiers inputWithModifiers) {
		Minecraft minecraft = Minecraft.getInstance();
		MouseHandler mouse = Objects.requireNonNull(minecraft).mouseHandler;
		double x = mouse.xpos() * (double) minecraft.getWindow().getGuiScaledWidth() / (double) minecraft.getWindow().getScreenWidth();
		double y = mouse.ypos() * (double) minecraft.getWindow().getGuiScaledHeight() / (double) minecraft.getWindow().getScreenHeight();

		Vector2f center = this.template.getGridCenter();
		PolyominoWidget widget = new PolyominoWidget(this.screen, (int) (x - center.x * VoxelButton.TESSERA_SIZE), (int) (y - center.y * VoxelButton.TESSERA_SIZE), this.color, this.template.seed());

		for (Voxel.PlainVoxel voxel : this.template.allVoxels()) {
			widget = widget.withVoxel(voxel.relativeX(), voxel.relativeY());
		}

		this.screen.selected = this.screen.addRenderableWidget(widget);
	}

	@Override
	protected void extractContents(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		Vector2f center = this.template.getGridCenter();

		PolyominoWidget.renderVoxels(
				new GraphicsRenderHelper(graphics),
				this.template,
				TesseraMaterial.values()[this.color],
				-center.x * VoxelButton.TESSERA_SIZE + this.getX() + SIZE * 0.5F,
				-center.y * VoxelButton.TESSERA_SIZE + this.getY() + SIZE * 0.5F
		);
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getColor() {
		return this.color;
	}
}
