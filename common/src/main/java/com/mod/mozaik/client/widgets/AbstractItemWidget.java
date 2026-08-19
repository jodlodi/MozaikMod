package com.mod.mozaik.client.widgets;

import com.mod.mozaik.client.screens.MortarScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class AbstractItemWidget extends AbstractWidget {
	protected final MortarScreen screen;
	protected final Minecraft minecraft;
	private final boolean decorations;
	private final boolean tooltip;
	private final boolean canBeSelected;

	public AbstractItemWidget(MortarScreen screen, int x, int y, boolean decorations, boolean tooltip, boolean canBeSelected) {
		super(x, y, 18, 18, Component.empty());
		this.screen = screen;
		this.canBeSelected = canBeSelected;
		this.minecraft = Minecraft.getInstance();
		this.decorations = decorations;
		this.tooltip = tooltip;
	}

	@Override
	protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		graphics.item(this.getItemStack(), this.getX(), this.getY(), 0);
		if (this.decorations) {
			graphics.itemDecorations(this.minecraft.font, this.getItemStack(), this.getX(), this.getY(), null);
		}

		if (this.canBeSelected && this.isHovered()) {
			graphics.outline(this.getX() - 1, this.getY() - 1, this.getWidth(), this.getHeight(), -1);
		}

		if (this.tooltip && this.isHovered()) {
			this.extractTooltip(graphics, mouseX, mouseY);
		}
	}

	protected void extractTooltip(GuiGraphicsExtractor graphics, int x, int y) {
		graphics.setTooltipForNextFrame(this.minecraft.font, this.getItemStack(), x, y);
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
		output.add(NarratedElementType.TITLE, Component.translatable("narration.item", this.getItemStack().getHoverName()));
	}

	protected abstract ItemStack getItemStack();
}
