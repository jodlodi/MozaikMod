package com.mod.mozaik.client.buttons;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.PhaseRenderable;
import com.mod.mozaik.client.screens.MortarScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2i;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class SpriteButton extends ModButton implements PhaseRenderable {
	protected final MortarScreen screen;
	protected final SpriteSet spriteSet;

	public SpriteButton(MortarScreen screen, Vector2i pos, SpriteSet spriteSet) {
		super(screen.getLeftPos() + pos.x, screen.getTopPos() + pos.y, spriteSet.width, spriteSet.height, Component.empty());
		this.screen = screen;
		this.spriteSet = spriteSet;
	}

	protected ResourceLocation getTexture() {
		if (this.isPressed()) return this.spriteSet.pressed();
		return this.isHoveredOrFocused() ? this.spriteSet.hover() : this.spriteSet.normal();
	}

	@Override
	protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		graphics.blit(this.getTexture(), this.getX(), this.getY(), 0, 0, this.getWidth(), this.getHeight(), this.getWidth(), this.getHeight());
	}

	@Override
	public abstract void onPress();

	public abstract boolean isPressed();

	public record SpriteSet(ResourceLocation normal, ResourceLocation hover, ResourceLocation pressed,
							@Nullable ResourceLocation alt, int width, int height) {
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

		public static final SpriteSet FLIP_VERTICAL = new SpriteSet(
				Constants.prefix("textures/gui/container/flip_vertical/unselected.png"),
				Constants.prefix("textures/gui/container/flip_vertical/hovered.png"),
				Constants.prefix("textures/gui/container/flip_vertical/pressed.png"),
				null,
				18,
				18
		);

		public static final SpriteSet FLIP_HORIZONTAL = new SpriteSet(
				Constants.prefix("textures/gui/container/flip_horizontal/unselected.png"),
				Constants.prefix("textures/gui/container/flip_horizontal/hovered.png"),
				Constants.prefix("textures/gui/container/flip_horizontal/pressed.png"),
				null,
				18,
				18
		);

		public static final SpriteSet ROTATE_180 = new SpriteSet(
				Constants.prefix("textures/gui/container/rotate_180/unselected.png"),
				Constants.prefix("textures/gui/container/rotate_180/hovered.png"),
				Constants.prefix("textures/gui/container/rotate_180/pressed.png"),
				null,
				18,
				18
		);

		public static final SpriteSet ROTATE_270 = new SpriteSet(
				Constants.prefix("textures/gui/container/rotate_270/unselected.png"),
				Constants.prefix("textures/gui/container/rotate_270/hovered.png"),
				Constants.prefix("textures/gui/container/rotate_270/pressed.png"),
				null,
				18,
				18
		);

		public static final SpriteSet ROTATE_90 = new SpriteSet(
				Constants.prefix("textures/gui/container/rotate_90/unselected.png"),
				Constants.prefix("textures/gui/container/rotate_90/hovered.png"),
				Constants.prefix("textures/gui/container/rotate_90/pressed.png"),
				null,
				18,
				18
		);

		public static final SpriteSet TOGGLE = new SpriteSet(
				Constants.prefix("textures/gui/container/toggle/unselected.png"),
				Constants.prefix("textures/gui/container/toggle/hovered.png"),
				Constants.prefix("textures/gui/container/toggle/pressed.png"),
				Constants.prefix("textures/gui/container/toggle/pressed_hovered.png"),
				12,
				12
		);

		public static final SpriteSet LOCK_ACCEPT = new SpriteSet(
				Constants.prefix("textures/gui/container/lock_accept/unselected.png"),
				Constants.prefix("textures/gui/container/lock_accept/hovered.png"),
				Constants.prefix("textures/gui/container/lock_accept/pressed.png"),
				null,
				18,
				18
		);

		public static final SpriteSet LOCK_CANCEL = new SpriteSet(
				Constants.prefix("textures/gui/container/lock_cancel/unselected.png"),
				Constants.prefix("textures/gui/container/lock_cancel/hovered.png"),
				Constants.prefix("textures/gui/container/lock_cancel/pressed.png"),
				null,
				18,
				18
		);
	}
}
