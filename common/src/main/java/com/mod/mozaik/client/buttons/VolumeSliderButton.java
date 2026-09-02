package com.mod.mozaik.client.buttons;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.PhaseRenderable;
import com.mod.mozaik.client.screens.PersonalPreferences;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VolumeSliderButton extends ModButton implements PhaseRenderable {
	private static final int MIN_Y_OFFSET = 15;
	private static final int MAX_Y_OFFSET = 40;

	public VolumeSliderButton(int x, int y) {
		super(x, y, 16, 56, Component.empty());
	}

	@Override
	protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.renderWidget(graphics, mouseX, mouseY, partialTick);
		graphics.blit(Constants.prefix("textures/gui/container/volume.png"), this.getX(), this.getY(), 0.0F, 0.0F, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());

		float diff = MAX_Y_OFFSET - MIN_Y_OFFSET;
		int slider = Math.round(diff * PersonalPreferences.getVolume());
		int pos = this.getY() + MAX_Y_OFFSET - slider - 1;

		graphics.blit(Constants.prefix("textures/gui/container/slider.png"), this.getX() + 4, pos, 0.0F, 0.0F, 8, 3, 8, 3);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
		int offY = (int) Math.round(mouseY) - this.getY();
		int setter = Mth.clamp(offY, MIN_Y_OFFSET, MAX_Y_OFFSET) - MIN_Y_OFFSET;
		float diff = MAX_Y_OFFSET - MIN_Y_OFFSET;
		PersonalPreferences.voidSetVolume(1.0F - (setter / diff));
		return super.mouseDragged(mouseX, offY, button, dragX, dragY);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		int offY = (int) Math.round(mouseY) - this.getY();
		int setter = Mth.clamp(offY, MIN_Y_OFFSET, MAX_Y_OFFSET) - MIN_Y_OFFSET;
		float diff = MAX_Y_OFFSET - MIN_Y_OFFSET;
		PersonalPreferences.voidSetVolume(1.0F - (setter / diff));
		super.onClick(mouseX, offY);
	}

	@Override
	public void onRelease(double mouseX, double mouseY) {
		int offY = (int) Math.round(mouseY) - this.getY();
		int setter = Mth.clamp(offY, MIN_Y_OFFSET, MAX_Y_OFFSET) - MIN_Y_OFFSET;
		float diff = MAX_Y_OFFSET - MIN_Y_OFFSET;
		PersonalPreferences.voidSetVolume(1.0F - (setter / diff));
		super.onRelease(mouseX, offY);
	}
}
