package com.mod.mozaik.client.buttons;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.polyomino.ShardMaterial;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class AbstractMaterialButton extends ModButton {
	protected final MortarScreen screen;
	protected final Minecraft minecraft;
	private final boolean tooltip;

	public AbstractMaterialButton(MortarScreen screen, int x, int y, boolean tooltip) {
		super(x, y, 18, 18, Component.empty());
		this.screen = screen;
		this.minecraft = Minecraft.getInstance();
		this.tooltip = tooltip;
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		this.renderMaterial(graphics);

		if (this.tooltip && this.isHovered()) {
			this.extractTooltip(graphics, mouseX, mouseY);
		}
	}

	protected void renderMaterial(GuiGraphicsExtractor graphics) {
		int x = this.getX();
		int y = this.getY();

		int color = -1;
		if (!this.screen.getShardSource().isCreative()) {
			int count = this.screen.getShardSource().getCount(this.getMaterial());
			if (count == 0) color = 0x44777777;
		}

		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.getMaterialTexture(), 16, 16, 0, 0, x, y, 16, 16, color);
	}

	protected ResourceLocation getMaterialTexture() {
		return Constants.prefix(this.getMaterial().identifier().getPath() + "/shard");
	}

	protected void extractTooltip(GuiGraphicsExtractor graphics, int x, int y) {
		graphics.setTooltipForNextFrame(this.minecraft.font, this.getItemStack(), x, y);
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
		output.add(NarratedElementType.TITLE, Component.translatable("narration.item", this.getItemStack().getHoverName()));
	}

	protected abstract ItemStack getItemStack();

	protected abstract ResourceKey<ShardMaterial> getMaterial();
}
