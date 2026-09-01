package com.mod.mozaik.client.buttons;

import com.mod.mozaik.client.PhaseRenderable;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.client.screens.PersonalPreferences;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2i;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.List;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ToggleButton extends ModButton implements PhaseRenderable {
	protected final MortarScreen screen;
	protected final SpriteButton.SpriteSet spriteSet;
	protected final PersonalPreferences.ToggleOption setting;

	public ToggleButton(MortarScreen screen, Vector2i pos, PersonalPreferences.ToggleOption setting) {
		super(
				screen.getLeftPos() + pos.x,
				screen.getTopPos() + pos.y,
				SpriteButton.SpriteSet.TOGGLE.width() + 2 + Minecraft.getInstance().font.width(Component.translatable(setting.name())),
				SpriteButton.SpriteSet.TOGGLE.height(),
				Component.empty()
		);
		this.screen = screen;
		this.spriteSet = SpriteButton.SpriteSet.TOGGLE;
		this.setting = setting;
	}

	public boolean getSetting() {
		return this.setting.get();
	}

	protected ResourceLocation getTexture() {
		if (this.getSetting()) {
			return this.isHovered() && this.spriteSet.alt() != null
					? this.spriteSet.alt()
					: this.spriteSet.pressed();
		} else {
			return this.isHovered()
					? this.spriteSet.hover()
					: this.spriteSet.normal();
		}
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		graphics.blit(RenderPipelines.GUI_TEXTURED, this.getTexture(), this.getX(), this.getY(), 0, 0, SpriteButton.SpriteSet.TOGGLE.width(), this.getHeight(), SpriteButton.SpriteSet.TOGGLE.width(), this.getHeight());

		int color = this.isHovered() ? 0xFFFFFFFF : 0xFFD0D0D0;

		graphics.text(
				Minecraft.getInstance().font,
				Component.translatable(this.setting.name()),
				this.getX() + SpriteButton.SpriteSet.TOGGLE.width() + 2,
				this.getY() + this.getHeight() - Minecraft.getInstance().font.lineHeight,
				color,
				true
		);
	}

	@Override
	public void onPress(InputWithModifiers inputWithModifiers) {
		this.setting.set(!this.setting.get());
	}

}
