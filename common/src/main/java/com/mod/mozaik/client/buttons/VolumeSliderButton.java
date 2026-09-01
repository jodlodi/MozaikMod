package com.mod.mozaik.client.buttons;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.PhaseRenderable;
import com.mod.mozaik.client.screens.PersonalPreferences;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.joml.Vector2i;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class VolumeSliderButton extends ModButton implements PhaseRenderable {
	private static final int MIN_Y_OFFSET = 15;
	private static final int MAX_Y_OFFSET = 40;

	public VolumeSliderButton(int x, int y) {
		super(x, y, 16, 56, Component.empty());
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		super.extractContents(graphics, mouseX, mouseY, partialTick);
		graphics.blit(RenderPipelines.GUI_TEXTURED, Constants.prefix("textures/gui/container/volume.png"), this.getX(), this.getY(), 0.0F, 0.0F, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());

		float diff = MAX_Y_OFFSET - MIN_Y_OFFSET;
		int slider = Math.round(diff * PersonalPreferences.getVolume());
		int pos = this.getY() + MAX_Y_OFFSET - slider - 1;

		graphics.blit(RenderPipelines.GUI_TEXTURED, Constants.prefix("textures/gui/container/slider.png"), this.getX() + 4, pos, 0.0F, 0.0F, 8, 3, 8, 3);
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
		int mouseY = (int) Math.round(event.y()) - this.getY();
		int setter = Mth.clamp(mouseY, MIN_Y_OFFSET, MAX_Y_OFFSET) - MIN_Y_OFFSET;
		float diff = MAX_Y_OFFSET - MIN_Y_OFFSET;
		PersonalPreferences.voidSetVolume(1.0F - (setter / diff));
		return super.mouseDragged(event, dx, dy);
	}

	@Override
	public void onClick(MouseButtonEvent event, boolean doubleClick) {
		int mouseY = (int) Math.round(event.y()) - this.getY();
		int setter = Mth.clamp(mouseY, MIN_Y_OFFSET, MAX_Y_OFFSET) - MIN_Y_OFFSET;
		float diff = MAX_Y_OFFSET - MIN_Y_OFFSET;
		PersonalPreferences.voidSetVolume(1.0F - (setter / diff));
		super.onClick(event, doubleClick);
	}

	@Override
	public void onRelease(MouseButtonEvent event) {
		int mouseY = (int) Math.round(event.y()) - this.getY();
		int setter = Mth.clamp(mouseY, MIN_Y_OFFSET, MAX_Y_OFFSET) - MIN_Y_OFFSET;
		float diff = MAX_Y_OFFSET - MIN_Y_OFFSET;
		PersonalPreferences.voidSetVolume(1.0F - (setter / diff));
		super.onRelease(event);
	}
}
