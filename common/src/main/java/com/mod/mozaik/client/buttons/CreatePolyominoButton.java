package com.mod.mozaik.client.buttons;

import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.client.screens.MozaikTool;
import com.mod.mozaik.client.screens.PersonalPreferences;
import com.mod.mozaik.client.widgets.HeldPolyominoWidget;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.polyomino.Tessera;
import com.mod.mozaik.reg.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import org.joml.Vector2f;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CreatePolyominoButton extends ModButton {
	public final static int SIZE = 32;
	private final MortarScreen screen;

	public CreatePolyominoButton(int x, int y, MortarScreen screen) {
		super(x - SIZE / 2, y - SIZE / 2, SIZE, SIZE, Component.empty());
		this.screen = screen;
	}

	public boolean canPress() {
		if (this.screen.getShardSource().isCreative()) return true;
		if (this.screen.getShardSource().getCount(PersonalPreferences.getPrimaryColor()) <= 0) return false;
		return !this.screen.noPoly(PersonalPreferences.getPolyominoShape());
	}

	@Override
	public void onPress(InputWithModifiers inputWithModifiers) {
		if (!this.canPress()) return;
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
	public void playDownSound(SoundManager soundManager) {
		if (this.canPress()) playButtonClickSound(soundManager, Holder.direct(ModSounds.PICK_SHARD.get()));
	}

	public static void playButtonClickSound(SoundManager soundManager, Holder<SoundEvent> soundEvent) {
		soundManager.play(SimpleSoundInstance.forUI(soundEvent.value(), 1.0F, PersonalPreferences.getVolume()));
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		Vector2f center = PersonalPreferences.getShape().getGridCenter();

		int color = this.canPress() ? -1 : 0x77777777;

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
		if (this.screen.getShardSource().isCreative() && !PersonalPreferences.getCreativeInfinity().get()) return "";
		if (this.screen.getShardSource().isCreative()) return "∞";
		if (this.screen.noPoly(PersonalPreferences.getPolyominoShape())) return "✕";
		return String.valueOf(this.screen.getShardSource().getCount(PersonalPreferences.getPrimaryColor()));
	}

	protected void extractTooltip(GuiGraphicsExtractor graphics, int x, int y) {
		graphics.setTooltipForNextFrame(Minecraft.getInstance().font, ShardItem.SHARDS.get(PersonalPreferences.getPrimaryColor()).getDefaultInstance(), x, y);
	}
}
