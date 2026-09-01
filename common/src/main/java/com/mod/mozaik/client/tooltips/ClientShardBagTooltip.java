package com.mod.mozaik.client.tooltips;

import com.mod.mozaik.items.ShardBagItem;
import com.mod.mozaik.items.components.ShardBagContents;
import com.mod.mozaik.polyomino.ShardStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ClientShardBagTooltip implements ClientTooltipComponent {
	private static final ResourceLocation SLOT_HIGHLIGHT_BACK_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/slot_highlight_back");
	private static final ResourceLocation SLOT_HIGHLIGHT_FRONT_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/slot_highlight_front");
	private static final ResourceLocation SLOT_BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("container/bundle/slot_background");
	private static final int SLOT_MARGIN = 4;
	private static final int SLOT_SIZE = 24;
	private static final int GRID_WIDTH = 96;
	private static final Component BUNDLE_EMPTY_DESCRIPTION = Component.translatable("item.mozaik.bag.empty.description");
	private final ShardBagContents contents;

	public ClientShardBagTooltip(ShardBagItem.ShardBagTooltip contents) {
		this.contents = contents.contents();
	}

	@Override
	public int getHeight(Font font) {
		return this.contents.isEmpty() ? getEmptyBundleBackgroundHeight(font) : this.backgroundHeight();
	}

	@Override
	public int getWidth(Font font) {
		return GRID_WIDTH;
	}

	@Override
	public boolean showTooltipWithItemInHand() {
		return true;
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
	public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
		if (this.contents.isEmpty()) {
			extractEmptyBundleTooltip(font, x, y, w, h, graphics);
		} else {
			this.extractBundleWithItemsTooltip(font, x, y, w, h, graphics);
		}
	}

	private static void extractEmptyBundleTooltip(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
		int left = x + getContentXOffset(w);
		extractEmptyBundleDescriptionText(left, y, font, graphics);
	}

	private void extractBundleWithItemsTooltip(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
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

	private static boolean shouldRenderItemSlot(List<? extends ItemInstance> shownItems, int slotNumber) {
		return shownItems.size() >= slotNumber;
	}

	private int getAmountOfHiddenItems(List<ShardStack> shownItems) {
		return this.contents.items().stream().skip(shownItems.size()).mapToInt(ItemInstance::count).sum();
	}

	private void extractSlot(int slotNumber, int drawX, int drawY, List<ShardStack> shownItems, int slotIndex, Font font, GuiGraphicsExtractor graphics) {
		int itemVisualOrderIndex = shownItems.size() - slotNumber;
		boolean hasHighlight = itemVisualOrderIndex == this.contents.getSelectedItemIndex();
		ItemStack item = shownItems.get(itemVisualOrderIndex).create();
		if (hasHighlight) {
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_BACK_SPRITE, drawX, drawY, SLOT_SIZE, SLOT_SIZE);
		} else {
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_BACKGROUND_SPRITE, drawX, drawY, SLOT_SIZE, SLOT_SIZE);
		}

		graphics.item(item, drawX + SLOT_MARGIN, drawY + SLOT_MARGIN, slotIndex);
		graphics.itemDecorations(font, item, drawX + SLOT_MARGIN, drawY + SLOT_MARGIN);
		if (hasHighlight) {
			graphics.blitSprite(RenderPipelines.GUI_TEXTURED, SLOT_HIGHLIGHT_FRONT_SPRITE, drawX, drawY, SLOT_SIZE, SLOT_SIZE);
		}
	}

	private static void extractCount(int drawX, int drawY, int hiddenItemCount, Font font, GuiGraphicsExtractor graphics) {
		graphics.centeredText(font, "+" + hiddenItemCount, drawX + 12, drawY + 10, -1);
	}

	private void extractSelectedItemTooltip(Font font, GuiGraphicsExtractor graphics, int x, int y, int w) {
		ShardStack selectedItem = this.contents.getSelectedItem();
		if (selectedItem != null) {
			ItemStack itemStack = selectedItem.create();
			Component selectedItemName = itemStack.getStyledHoverName();
			int textWidth = font.width(selectedItemName.getVisualOrderText());
			int centerTooltip = x + w / 2 - 12;
			ClientTooltipComponent selectedItemNameTooltip = ClientTooltipComponent.create(selectedItemName.getVisualOrderText());
			graphics.tooltip(
					font,
					List.of(selectedItemNameTooltip),
					centerTooltip - textWidth / 2,
					y - 15,
					DefaultTooltipPositioner.INSTANCE,
					itemStack.get(DataComponents.TOOLTIP_STYLE)
			);
		}
	}

	private static void extractEmptyBundleDescriptionText(int x, int y, Font font, GuiGraphicsExtractor graphics) {
		graphics.textWithWordWrap(font, BUNDLE_EMPTY_DESCRIPTION, x, y, GRID_WIDTH, 0xffaaaaaa);
	}

	private static int getEmptyBundleDescriptionTextHeight(Font font) {
		return font.split(BUNDLE_EMPTY_DESCRIPTION, GRID_WIDTH).size() * 9;
	}
}
