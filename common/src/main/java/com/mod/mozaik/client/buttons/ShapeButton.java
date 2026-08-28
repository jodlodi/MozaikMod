package com.mod.mozaik.client.buttons;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.PhaseRenderable;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.client.screens.PersonalPreferences;
import com.mod.mozaik.polyomino.PrePolyominoShapes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NullMarked;

import java.util.*;

@NullMarked
public class ShapeButton extends ModButton implements PhaseRenderable {
	protected final MortarScreen screen;
	private final int index;

	public ShapeButton(MortarScreen screen, int x, int y, int index) {
		super(x, y, 18, 18, Component.empty());
		this.screen = screen;
		this.index = index;
	}

	protected Identifier getTexture() {
		int dis = PersonalPreferences.minTemplate() + this.index;
		return Constants.prefix("textures/gui/container/shapes/" + PrePolyominoShapes.values()[dis].name().toLowerCase(Locale.ROOT) + "/" + (dis == PersonalPreferences.getTemplate() ? "pressed" : "unselected") + ".png");
	}

	@Override
	public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
		if (PersonalPreferences.getReverseScrollDirectionBars().get()) scrollY *= -1;

		if (scrollY > 0) {
			MortarScreen.templateUpBy((int) scrollY);
			return true;
		} else if (scrollY < 0) {
			MortarScreen.templateDownBy((int) -scrollY);
			return true;
		}

		return false;
	}

	@Override
	protected boolean isValidClickButton(MouseButtonInfo buttonInfo) {
		return buttonInfo.button() == MortarScreen.LEFT_CLICK || buttonInfo.button() == MortarScreen.RIGHT_CLICK;
	}

	@Override
	public void onClick(MouseButtonEvent event, boolean doubleClick) {
		PersonalPreferences.setTemplate(PersonalPreferences.minTemplate() + this.index);
		PersonalPreferences.setShape(PrePolyominoShapes.values()[PersonalPreferences.getTemplate()].template.build(PersonalPreferences.getPrimaryColor(), UUID.randomUUID()));
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, this.getTexture(), this.getX() - 1, this.getY() - 1, 0, 0, 18, 18, 18, 18);

		List<Integer> favSlots = new ArrayList<>();
		for (int i = 1; i <= 9; i++) {
			if (PersonalPreferences.getFavourite(i - 1).template().orElse(-1) == this.getShape()) {
				graphics.blit(RenderPipelines.GUI_TEXTURED, Constants.prefix("textures/gui/container/favourite.png"), this.getX() - 1, this.getY() - 1, 0, 0, 18, 18, 18, 18);
				favSlots.add(i);
			}
		}

		if (this.isHovered() && !favSlots.isEmpty()) {
			graphics.setTooltipForNextFrame(Minecraft.getInstance().font, List.of(
					Component.translatable(MaterialButton.FAVOURITE, Component.literal(favSlots.toString()).withStyle(ChatFormatting.AQUA))
			), Optional.empty(), mouseX, mouseY);
		}
	}

	public int getShape() {
		return PersonalPreferences.minTemplate() + this.index;
	}
}
