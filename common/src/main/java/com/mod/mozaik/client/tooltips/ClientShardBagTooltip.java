package com.mod.mozaik.client.tooltips;

import com.mod.mozaik.items.components.ShardBagContents;
import com.mod.mozaik.polyomino.ShardStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ClientShardBagTooltip implements ClientTooltipComponent {
	private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/background");
	private static final int SLOT_MARGIN = 4;
	private static final int SLOT_SIZE = 24;
	private static final int GRID_WIDTH = 96;
	private static final Component BUNDLE_EMPTY_DESCRIPTION = Component.translatable("item.mozaik.bag.empty.description");
	private final ShardBagContents contents;

	public ClientShardBagTooltip(ShardBagContents contents) {
		this.contents = contents;
	}

	@Override
	public int getHeight() {
		return this.contents.isEmpty() ? getEmptyBundleBackgroundHeight(Minecraft.getInstance().font) : this.backgroundHeight();
	}

	@Override
	public int getWidth(Font font) {
		return GRID_WIDTH;
	}

	private static int getEmptyBundleBackgroundHeight(Font font) {
		return getEmptyBundleDescriptionTextHeight(font) + 8;
	}

	private int backgroundHeight() {
		return this.itemGridHeight() + 8;
	}

	private int itemGridHeight() {
		return this.gridSizeY() * SLOT_SIZE;
	}

	private static int getContentXOffset(int tooltipWidth) {
		return (tooltipWidth - GRID_WIDTH) / 2;
	}

	private int gridSizeY() {
		return (int)Math.ceil(((double)this.contents.size() + (double)1.0F) / (double)this.gridSizeX());
	}

	private int slotCount() {
		return Math.min(ShardBagContents.MAX_VISIBLE_SLOTS, this.contents.size());
	}

	public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
		int i = this.gridSizeX();
		int j = this.gridSizeY();
		guiGraphics.blitSprite(BACKGROUND_SPRITE, x, y, GRID_WIDTH, this.backgroundHeight());
		int k = 0;

		for(int l = 0; l < j; ++l) {
			for(int i1 = 0; i1 < i; ++i1) {
				int j1 = x + i1 * 18 + 1;
				int k1 = y + l * 20 + 1;
				this.renderSlot(j1, k1, k++, guiGraphics, font);
			}
		}

	}

	private void renderSlot(int x, int y, int itemIndex, GuiGraphics guiGraphics, Font font) {
		if (itemIndex >= this.contents.size()) {
			this.blit(guiGraphics, x, y, Texture.SLOT);
		} else {
			List<ShardStack> shownItems = this.getShownItems(this.contents.getNumberOfItemsToShow());
			int itemVisualOrderIndex = shownItems.size() - itemIndex;
			ItemStack itemstack = shownItems.get(itemVisualOrderIndex).create();
			this.blit(guiGraphics, x, y, ClientShardBagTooltip.Texture.SLOT);
			guiGraphics.renderItem(itemstack, x + 1, y + 1, itemIndex);
			guiGraphics.renderItemDecorations(font, itemstack, x + 1, y + 1);
			if (itemIndex == 0) {
				AbstractContainerScreen.renderSlotHighlight(guiGraphics, x + 1, y + 1, 0);
			}
		}
	}

	private List<ShardStack> getShownItems(int amountOfItemsToShow) {
		int lastToDisplay = Math.min(this.contents.size(), amountOfItemsToShow);
		return this.contents.items().subList(0, lastToDisplay);
	}

	private void blit(GuiGraphics guiGraphics, int x, int y, ClientShardBagTooltip.Texture texture) {
		guiGraphics.blitSprite(texture.sprite, x, y, 0, texture.w, texture.h);
	}

	private int gridSizeX() {
		return Math.max(2, (int)Math.ceil(Math.sqrt((double)this.contents.size() + (double)1.0F)));
	}

	private static void extractEmptyBundleDescriptionText(int x, int y, Font font, GuiGraphics graphics) {
		graphics.drawStringWithBackdrop(font, BUNDLE_EMPTY_DESCRIPTION, x, y, GRID_WIDTH, 0xffaaaaaa);
	}

	private static int getEmptyBundleDescriptionTextHeight(Font font) {
		return font.split(BUNDLE_EMPTY_DESCRIPTION, GRID_WIDTH).size() * 9;
	}

	@OnlyIn(Dist.CLIENT)
	enum Texture {
		BLOCKED_SLOT(ResourceLocation.withDefaultNamespace("container/bundle/blocked_slot"), 18, 20),
		SLOT(ResourceLocation.withDefaultNamespace("container/bundle/slot"), 18, 20);

		public final ResourceLocation sprite;
		public final int w;
		public final int h;

		Texture(ResourceLocation sprite, int w, int h) {
			this.sprite = sprite;
			this.w = w;
			this.h = h;
		}
	}
}
