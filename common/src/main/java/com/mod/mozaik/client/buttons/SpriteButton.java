package com.mod.mozaik.client.buttons;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.PhaseRenderable;
import com.mod.mozaik.client.screens.MortarScreen;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.joml.Vector2i;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public abstract class SpriteButton extends ModButton implements PhaseRenderable {
	protected final MortarScreen screen;
	protected final SpriteSet spriteSet;

	public SpriteButton(MortarScreen screen, Vector2i pos, SpriteSet spriteSet) {
		super(screen.getLeftPos() + pos.x, screen.getTopPos() + pos.y, spriteSet.width, spriteSet.height, Component.empty());
		this.screen = screen;
		this.spriteSet = spriteSet;
	}

	protected Identifier getTexture() {
		if (this.isPressed()) return this.spriteSet.pressed();
		return this.isHoveredOrFocused() ? this.spriteSet.hover() : this.spriteSet.normal();
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, this.getTexture(), this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
	}

	public void tick() {

	}

	@Override
	public abstract void onPress(InputWithModifiers inputWithModifiers);

	public abstract boolean isPressed();

	public record SpriteSet(Identifier normal, Identifier hover, Identifier pressed, @Nullable Identifier unable, int width, int height) {
		public static final SpriteSet UP_ARROW = new SpriteSet(
				Constants.prefix("textures/gui/container/up_arrow/unselected.png"),
				Constants.prefix("textures/gui/container/up_arrow/hovered.png"),
				Constants.prefix("textures/gui/container/up_arrow/pressed.png"),
				Constants.prefix("textures/gui/container/up_arrow/unable.png"),
				16,
				8
		);

		public static final SpriteSet DOWN_ARROW = new SpriteSet(
				Constants.prefix("textures/gui/container/down_arrow/unselected.png"),
				Constants.prefix("textures/gui/container/down_arrow/hovered.png"),
				Constants.prefix("textures/gui/container/down_arrow/pressed.png"),
				Constants.prefix("textures/gui/container/down_arrow/unable.png"),
				16,
				8
		);

		public static final SpriteSet CURSOR = new SpriteSet(
				Constants.prefix("textures/gui/container/cursor/unselected.png"),
				Constants.prefix("textures/gui/container/cursor/hovered.png"),
				Constants.prefix("textures/gui/container/cursor/pressed.png"),
				null,
				20,
				20
		);

		public static final SpriteSet CHISEL = new SpriteSet(
				Constants.prefix("textures/gui/container/chisel/unselected.png"),
				Constants.prefix("textures/gui/container/chisel/hovered.png"),
				Constants.prefix("textures/gui/container/chisel/pressed.png"),
				null,
				20,
				20
		);

		public static final SpriteSet SWAP = new SpriteSet(
				Constants.prefix("textures/gui/container/swap/unselected.png"),
				Constants.prefix("textures/gui/container/swap/hovered.png"),
				Constants.prefix("textures/gui/container/swap/pressed.png"),
				null,
				20,
				20
		);

		public static final SpriteSet PICKER = new SpriteSet(
				Constants.prefix("textures/gui/container/picker/unselected.png"),
				Constants.prefix("textures/gui/container/picker/hovered.png"),
				Constants.prefix("textures/gui/container/picker/pressed.png"),
				null,
				20,
				20
		);

		public static final SpriteSet WAND = new SpriteSet(
				Constants.prefix("textures/gui/container/wand/unselected.png"),
				Constants.prefix("textures/gui/container/wand/hovered.png"),
				Constants.prefix("textures/gui/container/wand/pressed.png"),
				null,
				20,
				20
		);

		public static final SpriteSet SELECT = new SpriteSet(
				Constants.prefix("textures/gui/container/select/unselected.png"),
				Constants.prefix("textures/gui/container/select/hovered.png"),
				Constants.prefix("textures/gui/container/select/pressed.png"),
				null,
				20,
				20
		);
	}
}
