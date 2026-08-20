package com.mod.mozaik.client.buttons;

import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.client.screens.MozaikTool;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import com.mod.mozaik.polyomino.Polyomino;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.network.chat.Component;
import org.joml.Vector2i;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
public class ToolButton extends SpriteButton {

	private final MozaikTool tool;

	public ToolButton(MortarScreen screen, Vector2i pos, SpriteSet spriteSet, MozaikTool tool) {
		super(screen, pos, spriteSet);
		this.tool = tool;
		this.setTooltip(Tooltip.create(Component.translatable(this.tool.asTranslationString())));
	}

	@Override
	public void onPress(InputWithModifiers inputWithModifiers) {
		this.screen.tool = this.tool;
		if (this.screen.selected.isEmpty()) return;
		switch (this.tool) {
			case CHISEL, SWAP, CURSOR -> {
				MozaikTool.useOn(this.screen, inputWithModifiers.hasShiftDown(), this.screen.selected, this.tool);
				this.screen.selected.clear();
			}
			default -> this.screen.selected.clear();
		}
	}

	@Override
	public boolean isPressed() {
		return this.screen.tool == this.tool;
	}
}
