package com.mod.mozaik.client.buttons;

import com.mod.mozaik.client.screens.MortarScreen;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2i;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class ClickableButton extends SpriteButton {
	protected int isPressed = 0;

	public ClickableButton(MortarScreen screen, Vector2i pos, SpriteSet spriteSet) {
		super(screen, pos, spriteSet);
	}

	@Override
	protected ResourceLocation getTexture() {
		if (this.isPressed()) return this.spriteSet.pressed();
		if (this.isBlocked() && this.spriteSet.alt() != null) return this.spriteSet.alt();
		return this.isHovered() ? this.spriteSet.hover() : this.spriteSet.normal();
	}

	@Override
	public void tick() {
		if (this.isPressed()) this.isPressed--;
	}

	@Override
	public void onPress() {
		if (this.isBlocked()) return;
		this.isPressed = 2;
		this.onUnblockedPress();
	}

	@Override
	public void playDownSound(SoundManager soundManager) {
		if (this.isBlocked()) return;
		super.playDownSound(soundManager);
	}

	@Override
	public boolean isHovered() {
		return super.isHovered() && !this.isBlocked();
	}

	@Override
	public boolean isPressed() {
		return this.isPressed > 0;
	}

	public boolean isBlocked() {
		return false;
	}

	public abstract void onUnblockedPress();
}
