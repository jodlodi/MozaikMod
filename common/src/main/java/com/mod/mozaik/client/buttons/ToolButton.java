package com.mod.mozaik.client.buttons;

import com.mod.mozaik.client.screens.MortarScreen;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ToolButton extends SpriteButton {

	private final MortarScreen.Tool tool;

	public ToolButton(MortarScreen screen, Vector2i pos, SpriteSet spriteSet, MortarScreen.Tool tool) {
		super(screen, pos, spriteSet);
		this.tool = tool;
		this.setTooltip(Tooltip.create(Component.translatable(this.tool.asTranslationString())));
	}

	@Override
	public void onPress(InputWithModifiers inputWithModifiers) {
		this.screen.tool = this.tool;
	}

	@Override
	public boolean isPressed() {
		return this.screen.tool == this.tool;
	}
}
