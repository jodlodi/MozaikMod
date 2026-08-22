package com.mod.mozaik.client.buttons;

import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.client.screens.MozaikTool;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Optional;

@NullMarked
public class ToolButton extends SpriteButton {
	private final MozaikTool tool;
	public static String SHORTCUT = "tooltip.mozaik.hotkey";

	public ToolButton(MortarScreen screen, Vector2i pos, SpriteSet spriteSet, MozaikTool tool) {
		super(screen, pos, spriteSet);
		this.tool = tool;
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		super.extractContents(graphics, mouseX, mouseY, partialTick);

		if (this.isHovered()) {
			graphics.setTooltipForNextFrame(Minecraft.getInstance().font, List.of(
					Component.translatable(this.tool.asTranslationString()),
					Component.translatable(SHORTCUT, Component.empty().append(this.tool.getKeyMapping().getTranslatedKeyMessage()).withStyle(ChatFormatting.AQUA))
			), Optional.empty(), mouseX, mouseY);
		}
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
