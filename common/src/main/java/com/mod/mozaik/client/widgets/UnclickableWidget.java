package com.mod.mozaik.client.widgets;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class UnclickableWidget extends AbstractWidget {
	public UnclickableWidget(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY, Component.empty());
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

	}

	@Override
	public boolean isFocused() {
		return false;
	}

	@Override
	protected boolean isValidClickButton(int button) {
		return false;
	}
}
