package com.mod.mozaik.client.buttons;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.PhaseRenderable;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NullMarked;

import java.util.function.BiConsumer;

@NullMarked
public abstract class SpriteButton extends ModButton implements PhaseRenderable {
	private final Identifier normal;
	private final Identifier hover;
	private final Identifier pressed;
	private final Identifier unable;

	private int isPressed = 0;

	protected SpriteButton(int x, int y, Identifier normal, Identifier hover, Identifier pressed, Identifier unable, int width, int height) {
		super(x, y, width, height, Component.empty());
		this.normal = normal;
		this.hover = hover;
		this.pressed = pressed;
		this.unable = unable;
	}

	public SpriteButton(int x, int y, SpriteSet spriteSet) {
		super(x, y, spriteSet.width, spriteSet.height, Component.empty());
		this.normal = spriteSet.normal;
		this.hover = spriteSet.hover;
		this.pressed = spriteSet.pressed;
		this.unable = spriteSet.unable;
	}

	public static SpriteButton createArrow(int x, int y, Identifier location, Identifier hover, BiConsumer<SpriteButton, InputWithModifiers> onPress) {
		int width = 23;
		int height = 13;
		return new SpriteButton(x - width / 2, y - height / 2, location, hover, location, location, width, height) {
			@Override
			public void onUnblockedPress(InputWithModifiers inputWithModifiers) {
				onPress.accept(this, inputWithModifiers);
			}
		};
	}

	protected Identifier getTexture() {
		if (this.isPressed > 0) return this.pressed;
		if (this.isBlocked()) return this.unable;
		return this.isHovered() ? this.hover : this.normal;
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, this.getTexture(), this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
	}

	public void tick() {
		if (this.isPressed > 0) this.isPressed--;
	}

	@Override
	public final void onPress(InputWithModifiers inputWithModifiers) {
		if (this.isBlocked()) return;
		this.isPressed = 2;
		this.onUnblockedPress(inputWithModifiers);
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

	public abstract void onUnblockedPress(InputWithModifiers inputWithModifiers);

	public boolean isBlocked() {
		return false;
	}

	public record SpriteSet(Identifier normal, Identifier hover, Identifier pressed, Identifier unable, int width, int height) {
		private static final Identifier UP_UNSELECTED = Constants.prefix("textures/gui/container/up_unselected.png");
		private static final Identifier UP_HOVERED = Constants.prefix("textures/gui/container/up_hovered.png");
		private static final Identifier UP_PRESSED = Constants.prefix("textures/gui/container/up_pressed.png");
		private static final Identifier UP_UNABLE = Constants.prefix("textures/gui/container/up_unable.png");

		private static final Identifier DOWN_UNSELECTED = Constants.prefix("textures/gui/container/down_unselected.png");
		private static final Identifier DOWN_HOVERED = Constants.prefix("textures/gui/container/down_hovered.png");
		private static final Identifier DOWN_PRESSED = Constants.prefix("textures/gui/container/down_pressed.png");
		private static final Identifier DOWN_UNABLE = Constants.prefix("textures/gui/container/down_unable.png");

		public static SpriteSet UP_ARROW = new SpriteSet(UP_UNSELECTED, UP_HOVERED, UP_PRESSED, UP_UNABLE, 16, 8);
		public static SpriteSet DOWN_ARROW = new SpriteSet(DOWN_UNSELECTED, DOWN_HOVERED, DOWN_PRESSED, DOWN_UNABLE, 16, 8);
	}
}
