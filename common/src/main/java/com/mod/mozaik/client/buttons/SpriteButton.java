package com.mod.mozaik.client.buttons;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

import java.util.function.BiConsumer;

public class SpriteButton extends ModButton {
	private final Identifier location;
	private final Identifier hover;

	protected SpriteButton(int x, int y, Identifier location, Identifier hover, int width, int height) {
		super(x, y, width, height, Component.empty());
		this.location = location;
		this.hover = hover;
	}

	public static SpriteButton createArrow(int x, int y, Identifier location, Identifier hover, BiConsumer<SpriteButton, InputWithModifiers> onPress) {
		int width = 23;
		int height = 13;
		return new SpriteButton(x - width / 2, y - height / 2, location, hover, width, height) {
			@Override
			public void onPress(@NonNull InputWithModifiers inputWithModifiers) {
				onPress.accept(this, inputWithModifiers);
			}
		};
	}

	protected Identifier getTexture() {
		return this.isHovered() ? this.hover : this.location;
	}

	@Override
	protected void extractContents(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, this.getTexture(), this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
	}
}
