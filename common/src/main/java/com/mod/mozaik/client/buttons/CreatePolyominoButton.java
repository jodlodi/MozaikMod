package com.mod.mozaik.client.buttons;

import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.client.screens.MozaikTool;
import com.mod.mozaik.client.screens.PersonalPreferences;
import com.mod.mozaik.client.widgets.HeldPolyominoWidget;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.polyomino.Tessera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import org.joml.Vector2f;
import org.jspecify.annotations.NullMarked;

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
		MouseHandler mouse = minecraft.mouseHandler;
		double x = mouse.xpos() * (double) minecraft.getWindow().getGuiScaledWidth() / (double) minecraft.getWindow().getScreenWidth();
		double y = mouse.ypos() * (double) minecraft.getWindow().getGuiScaledHeight() / (double) minecraft.getWindow().getScreenHeight();

		Vector2f center = PersonalPreferences.getShape().getGridCenter();
		HeldPolyominoWidget widget = new HeldPolyominoWidget(this.screen, (int) (x - center.x * Tessera.TESSERA_SIZE), (int) (y - center.y * Tessera.TESSERA_SIZE), PersonalPreferences.getShape().copy());
		PersonalPreferences.setShape(PersonalPreferences.getShape().rebuild(PersonalPreferences.getPrimaryColor()));

		this.screen.carried.clear();
		this.screen.carried.add(this.screen.addRenderableWidget(widget));
		this.screen.tool = MozaikTool.CURSOR;
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		Vector2f center = PersonalPreferences.getShape().getGridCenter();

		int color = -1;
		if (!this.screen.getShardSource().isCreative()) {
			int count = this.screen.getShardSource().getCount(PersonalPreferences.getPrimaryColor());
			if (count == 0) color = 0x77777777;
		}

		PolyominoWidget.fill(
				new GraphicsRenderHelper(graphics),
				PersonalPreferences.getShape(),
				(int) (-center.x * Tessera.TESSERA_SIZE + this.getX() + SIZE * 0.5F + 1),
				(int) (-center.y * Tessera.TESSERA_SIZE + this.getY() + SIZE * 0.5F + 1),
				0x67222222
		);

		PolyominoWidget.renderVoxels(
				new GraphicsRenderHelper(graphics),
				PersonalPreferences.getShape(),
				(int) (-center.x * Tessera.TESSERA_SIZE + this.getX() + SIZE * 0.5F),
				(int) (-center.y * Tessera.TESSERA_SIZE + this.getY() + SIZE * 0.5F),
				color
		);

		if (this.isHovered()) {
			this.extractTooltip(graphics, mouseX, mouseY);
		}

		graphics.pose().pushMatrix();
		this.itemCount(graphics, Minecraft.getInstance().font, this.getX(), this.getY(), this.getCount());
		graphics.pose().popMatrix();
	}

	private void itemCount(GuiGraphicsExtractor graphics, Font font, int x, int y, String amount) {
		Vector2f center = PersonalPreferences.getShape().getGridCenter();
		graphics.text(font, amount, (int) (center.x) + x + 19 - 2 - font.width(amount) + 14, (int) (center.y) + y + 6 + 3 + 14, -1, true);
	}

	protected String getCount() {
		return this.screen.getShardSource().isCreative() ? "∞" : String.valueOf(this.screen.getShardSource().getCount(PersonalPreferences.getPrimaryColor()));
	}

	protected void extractTooltip(GuiGraphicsExtractor graphics, int x, int y) {
		graphics.setTooltipForNextFrame(Minecraft.getInstance().font, ShardItem.SHARDS.get(PersonalPreferences.getPrimaryColor()).getDefaultInstance(), x, y);
	}
}
