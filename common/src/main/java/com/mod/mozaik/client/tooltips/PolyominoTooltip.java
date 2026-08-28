package com.mod.mozaik.client.tooltips;

import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.buttons.ShapeButton;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import com.mod.mozaik.items.components.ShardBagContents;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.ShardStack;
import com.mod.mozaik.polyomino.Tessera;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class PolyominoTooltip implements ClientTooltipComponent {
	private final Polyomino polyomino;
	private final int height;
	private final int width;

	public PolyominoTooltip(ShapeButton.ShapeTooltip contents) {
		this.polyomino = contents.polyomino();

		int maxX = 0, maxY = 0;
		for (Tessera.PlacedTessera tessera : this.polyomino.placedTessera()) {
			if (tessera.x() > maxX) maxX = tessera.x();
			if (tessera.y() > maxY) maxY = tessera.y();
		}

		this.width = (maxX + 1) * Tessera.TESSERA_SIZE;
		this.height = (maxY + 1) * Tessera.TESSERA_SIZE;
	}

	@Override
	public int getHeight(Font font) {
		return this.height;
	}

	@Override
	public int getWidth(Font font) {
		return this.width;
	}

	@Override
	public boolean showTooltipWithItemInHand() {
		return true;
	}

	@Override
	public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
		PolyominoWidget.renderVoxels(new GraphicsRenderHelper(graphics), this.polyomino, x, y);
	}
}
