package com.mod.mozaik.client.tooltips;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.items.ShardBagItem;
import com.mod.mozaik.items.components.ShardBagContents;
import com.mod.mozaik.polyomino.ShardStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ClientShardBagTooltip implements ClientTooltipComponent {
	private static final ResourceLocation SLOT_HIGHLIGHT_BACK_SPRITE = Constants.prefix("textures/gui/shard_bag/slot_highlight_back.png");
	private static final ResourceLocation SLOT_HIGHLIGHT_FRONT_SPRITE = Constants.prefix("textures/gui/shard_bage/slot_highlight_front.png");
	private static final ResourceLocation SLOT_BACKGROUND_SPRITE = Constants.prefix("textures/gui/shard_bag/slot_background.png");
	private static final int SLOT_MARGIN = 4;
	private static final int SLOT_SIZE = 24;
	private static final int GRID_WIDTH = 96;
	private static final Component BUNDLE_EMPTY_DESCRIPTION = Component.translatable("item.mozaik.bag.empty.description");
	private final ShardBagContents contents;

	public ClientShardBagTooltip(ShardBagItem.ShardBagTooltip contents) {
		this.contents = contents.contents();
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
		return Mth.positiveCeilDiv(this.slotCount(), 4);
	}

	private int slotCount() {
		return Math.min(ShardBagContents.MAX_VISIBLE_SLOTS, this.contents.size());
	}

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics graphics) {
		int w = this.getWidth(Minecraft.getInstance().font);
		int h = this.getHeight();

		if (this.contents.isEmpty()) {
			extractEmptyBundleTooltip(font, x, y, w, h, graphics);
		} else {
			this.extractBundleWithItemsTooltip(font, x, y, w, h, graphics);
		}
	}

	private static void extractEmptyBundleTooltip(Font font, int x, int y, int w, int h, GuiGraphics graphics) {
		int left = x + getContentXOffset(w);
		extractEmptyBundleDescriptionText(left, y, font, graphics);
	}

	private void extractBundleWithItemsTooltip(Font font, int x, int y, int w, int h, GuiGraphics graphics) {
		boolean isOverflowing = this.contents.size() > ShardBagContents.MAX_VISIBLE_SLOTS;
		List<ShardStack> shownItems = this.getShownItems(this.contents.getNumberOfItemsToShow());
		int xStartPos = x + getContentXOffset(w) + GRID_WIDTH;
		int yStartPos = y + this.gridSizeY() * SLOT_SIZE;
		int slotNumber = 1;

		for (int rowNumber = 1; rowNumber <= this.gridSizeY(); rowNumber++) {
			for (int columnNumber = 1; columnNumber <= 4; columnNumber++) {
				int drawX = xStartPos - columnNumber * SLOT_SIZE;
				int drawY = yStartPos - rowNumber * SLOT_SIZE;
				if (shouldRenderSurplusText(isOverflowing, columnNumber, rowNumber)) {
					extractCount(drawX, drawY, this.getAmountOfHiddenItems(shownItems), font, graphics);
				} else if (shouldRenderItemSlot(shownItems, slotNumber)) {
					this.extractSlot(slotNumber, drawX, drawY, shownItems, slotNumber, font, graphics);
					slotNumber++;
				}
			}
		}

		this.extractSelectedItemTooltip(font, graphics, x, y, w);
	}

	private List<ShardStack> getShownItems(int amountOfItemsToShow) {
		int lastToDisplay = Math.min(this.contents.size(), amountOfItemsToShow);
		return this.contents.items().subList(0, lastToDisplay);
	}

	private static boolean shouldRenderSurplusText(boolean isOverflowing, int column, int row) {
		return isOverflowing && column * row == 1;
	}

	private static boolean shouldRenderItemSlot(List<ShardStack> shownItems, int slotNumber) {
		return shownItems.size() >= slotNumber;
	}

	private int getAmountOfHiddenItems(List<ShardStack> shownItems) {
		return this.contents.items().stream().skip(shownItems.size()).mapToInt(ShardStack::count).sum();
	}

	private void extractSlot(int slotNumber, int drawX, int drawY, List<ShardStack> shownItems, int slotIndex, Font font, GuiGraphics graphics) {
		int itemVisualOrderIndex = shownItems.size() - slotNumber;
		boolean hasHighlight = itemVisualOrderIndex == this.contents.getSelectedItemIndex();
		ItemStack item = shownItems.get(itemVisualOrderIndex).create();
		if (hasHighlight) {
			GraphicsRenderHelper.blit(graphics, SLOT_HIGHLIGHT_BACK_SPRITE, drawX, drawY, SLOT_SIZE, SLOT_SIZE, -1);
		} else {
			GraphicsRenderHelper.blit(graphics, SLOT_BACKGROUND_SPRITE, drawX, drawY, SLOT_SIZE, SLOT_SIZE, -1);
		}

		graphics.renderItem(item, drawX + SLOT_MARGIN, drawY + SLOT_MARGIN, slotIndex);
		graphics.renderItemDecorations(font, item, drawX + SLOT_MARGIN, drawY + SLOT_MARGIN);
		if (hasHighlight) {
			GraphicsRenderHelper.blit(graphics, SLOT_HIGHLIGHT_FRONT_SPRITE, drawX, drawY, SLOT_SIZE, SLOT_SIZE, -1);
		}
	}

	private static void extractCount(int drawX, int drawY, int hiddenItemCount, Font font, GuiGraphics graphics) {
		graphics.drawCenteredString(font, "+" + hiddenItemCount, drawX + 12, drawY + 10, -1);
	}

	private void extractSelectedItemTooltip(Font font, GuiGraphics graphics, int x, int y, int w) {
		ShardStack selectedItem = this.contents.getSelectedItem();
		if (selectedItem != null) {
			ItemStack itemStack = selectedItem.create();
			Component selectedItemName = itemStack.getHoverName();
			int textWidth = font.width(selectedItemName.getVisualOrderText());
			int centerTooltip = x + w / 2 - 12;
			graphics.renderTooltip(
					font,
					List.of(selectedItemName.getVisualOrderText()),
					DefaultTooltipPositioner.INSTANCE,
					centerTooltip - textWidth / 2,
					y - 15
			);
		}
	}

	private static void extractEmptyBundleDescriptionText(int x, int y, Font font, GuiGraphics graphics) {
		graphics.drawStringWithBackdrop(font, BUNDLE_EMPTY_DESCRIPTION, x, y, GRID_WIDTH, 0xffaaaaaa);
	}

	private static int getEmptyBundleDescriptionTextHeight(Font font) {
		return font.split(BUNDLE_EMPTY_DESCRIPTION, GRID_WIDTH).size() * 9;
	}
}