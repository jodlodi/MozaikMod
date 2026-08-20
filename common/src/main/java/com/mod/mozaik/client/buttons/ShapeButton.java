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
		return Constants.prefix("textures/gui/container/shapes/" + PrePolyominoShapes.values()[this.screen.templateFrom + this.index].name().toLowerCase(Locale.ROOT) + ".png");
	}

	@Override
	public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
		if (scrollY > 0) {
			MortarScreen.templateUpBy(this.screen, (int) scrollY);
			return true;
		} else if (scrollY < 0) {
			MortarScreen.templateDownBy(this.screen, (int) -scrollY);
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
		this.screen.template = this.screen.templateFrom + this.index;
		this.screen.setShape(PrePolyominoShapes.values()[this.screen.template].template.build(this.screen.getPrimaryColor(), Objects.requireNonNull(Minecraft.getInstance().level).getRandom().nextLong()));
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, this.getTexture(), this.getX(), this.getY(), 0, 0, 16, 16, 16, 16);
	}

	@Override
	public void onPress(InputWithModifiers inputWithModifiers) {
		this.screen.template = this.index;
	}
}
