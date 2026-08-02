package com.mod.mozaik.client.widgets;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public abstract class UnclickableWidget extends AbstractWidget {
	public UnclickableWidget(int x, int y, int sizeX, int sizeY) {
		super(x, y, sizeX, sizeY, Component.empty());
	}

	@Override
	protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {

	}

	@Override
	protected void updateWidgetNarration(@NonNull NarrationElementOutput narrationElementOutput) {

	}

	@Override
	public boolean isFocused() {
		return false;
	}

	@Override
	public boolean mouseClicked(@NonNull MouseButtonEvent event, boolean doubleClick) {
		return false;
	}
}
