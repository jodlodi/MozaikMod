package com.mod.mozaik.client.buttons;

import com.mod.mozaik.client.screens.MortarScreen;
import net.minecraft.client.input.InputWithModifiers;
import org.joml.Vector2i;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ToolButton extends SpriteButton {

	private final int mode;

	public ToolButton(MortarScreen screen, Vector2i pos, SpriteSet spriteSet, int mode) {
		super(screen, pos, spriteSet);
		this.mode = mode;
	}

	@Override
	public void onPress(InputWithModifiers inputWithModifiers) {
		this.screen.mode = this.mode;
	}

	@Override
	public boolean isPressed() {
		return this.screen.mode == this.mode;
	}
}
