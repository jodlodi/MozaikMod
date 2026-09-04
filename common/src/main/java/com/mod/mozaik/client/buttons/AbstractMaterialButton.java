package com.mod.mozaik.client.buttons;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.polyomino.ShardMaterial;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.atlas.sources.SingleFile;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

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
	protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		this.renderMaterial(graphics);

		if (this.tooltip && this.isHovered()) {
			this.extractTooltip(graphics, mouseX, mouseY);
		}
	}

	protected void renderMaterial(GuiGraphics graphics) {
		int x = this.getX();
		int y = this.getY();

		int color = -1;
		if (!this.screen.getShardSource().isCreative()) {
			int count = this.screen.getShardSource().getCount(this.getMaterial());
			if (count == 0) color = 0x44777777;
		}

		GraphicsRenderHelper.blit(graphics, this.getMaterialTexture(), x, y, 16, 16, color);
	}

	protected ResourceLocation getMaterialTexture() {
		return Constants.prefix("textures/item/" + this.getMaterial().location().getPath() + "_shards.png");
	}

	protected void extractTooltip(GuiGraphics graphics, int x, int y) {
		graphics.renderComponentTooltip(this.minecraft.font, Screen.getTooltipFromItem(this.minecraft, this.getItemStack()), x, y);
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput output) {
		output.add(NarratedElementType.TITLE, Component.translatable("narration.item", this.getItemStack().getHoverName()));
	}

	protected abstract ItemStack getItemStack();

	protected abstract ResourceKey<ShardMaterial> getMaterial();
}
