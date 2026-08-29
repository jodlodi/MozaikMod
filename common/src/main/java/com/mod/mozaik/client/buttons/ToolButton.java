package com.mod.mozaik.client.buttons;

import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.client.screens.MozaikTool;
import com.mod.mozaik.client.screens.PersonalPreferences;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.options.VideoSettingsScreen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
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

		if (PersonalPreferences.getToolButtonHotkey().get()) {
			this.hotkey(graphics, Minecraft.getInstance().font, this.getX(), this.getY(), Component.empty().append(this.tool.getKeyMapping().getTranslatedKeyMessage()));
		}

		if (this.isHovered()) {
			graphics.setTooltipForNextFrame(Minecraft.getInstance().font, List.of(
					Component.translatable(this.tool.asTranslationString()),
					Component.translatable(SHORTCUT, Component.empty().append(this.tool.getKeyMapping().getTranslatedKeyMessage()).withStyle(ChatFormatting.AQUA))
			), Optional.empty(), mouseX, mouseY);
		}
	}

	private void hotkey(GuiGraphicsExtractor graphics, Font font, int x, int y, MutableComponent amount) {
		graphics.text(font, amount, x + 18 - font.width(amount), y + 10, -1, true);
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
