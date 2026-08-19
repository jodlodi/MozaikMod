package com.mod.mozaik.client.buttons;

import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.client.widgets.HeldPolyominoWidget;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import com.mod.mozaik.polyomino.Tessera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import org.joml.Vector2f;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public class CreatePolyominoButton extends ModButton {
	public final static int SIZE = 32;
	private final MortarScreen screen;

	public CreatePolyominoButton(int x, int y, MortarScreen screen) {
		super(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE, Component.empty());
		this.screen = screen;
	}

	@Override
	public void onPress(InputWithModifiers inputWithModifiers) {
		Minecraft minecraft = Minecraft.getInstance();
		MouseHandler mouse = Objects.requireNonNull(minecraft).mouseHandler;
		double x = mouse.xpos() * (double) minecraft.getWindow().getGuiScaledWidth() / (double) minecraft.getWindow().getScreenWidth();
		double y = mouse.ypos() * (double) minecraft.getWindow().getGuiScaledHeight() / (double) minecraft.getWindow().getScreenHeight();

		Vector2f center = this.screen.getShape().getGridCenter();
		HeldPolyominoWidget widget = new HeldPolyominoWidget(this.screen, (int) (x - center.x * Tessera.TESSERA_SIZE), (int) (y - center.y * Tessera.TESSERA_SIZE), this.screen.getShape().copy());
		this.screen.setShape(this.screen.getShape().rebuild(this.screen.getPrimaryColor(), Objects.requireNonNull(Minecraft.getInstance().level).getRandom().nextLong()));

		this.screen.selected = this.screen.addRenderableWidget(widget);
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		Vector2f center = this.screen.getShape().getGridCenter();

		PolyominoWidget.fill(
				graphics,
				this.screen.getShape(),
				(int) (-center.x * Tessera.TESSERA_SIZE + this.getX() + SIZE * 0.5F + 1),
				(int) (-center.y * Tessera.TESSERA_SIZE + this.getY() + SIZE * 0.5F + 1),
				0x67222222
		);

		PolyominoWidget.renderVoxels(
				new GraphicsRenderHelper(graphics),
				this.screen.getShape(),
				(int) (-center.x * Tessera.TESSERA_SIZE + this.getX() + SIZE * 0.5F),
				(int) (-center.y * Tessera.TESSERA_SIZE + this.getY() + SIZE * 0.5F)
		);
	}
}
