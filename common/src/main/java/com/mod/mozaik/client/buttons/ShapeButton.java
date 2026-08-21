package com.mod.mozaik.client.buttons;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.PhaseRenderable;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.polyomino.PrePolyominoShapes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

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
		int dis = MortarScreen.templateFrom + this.index;
		return Constants.prefix("textures/gui/container/shapes/" + PrePolyominoShapes.values()[dis].name().toLowerCase(Locale.ROOT) + "/" + (dis == MortarScreen.template ? "pressed" : "unselected") + ".png");
	}

	@Override
	public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
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
		MortarScreen.template = MortarScreen.templateFrom + this.index;
		MortarScreen.setShape(PrePolyominoShapes.values()[MortarScreen.template].template.build(this.screen.getPrimaryColor(), Objects.requireNonNull(Minecraft.getInstance().level).getRandom().nextLong()));
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, this.getTexture(), this.getX() - 1, this.getY() - 1, 0, 0, 18, 18, 18, 18);
	}
}
