package com.mod.mozaik.client.buttons;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public abstract class ModButton extends AbstractButton {

	public ModButton(int x, int y, int width, int height, Component message) {
		super(x, y, width, height, message);
	}

	@Override
	public void onPress(@NonNull InputWithModifiers inputWithModifiers) {

	}

	@Override
	protected void extractContents(@NonNull GuiGraphicsExtractor guiGraphicsExtractor, int mouseX, int mouseY, float partialTick) {

	}

	@Override
	protected void updateWidgetNarration(@NonNull NarrationElementOutput narrationElementOutput) {

	}
}
