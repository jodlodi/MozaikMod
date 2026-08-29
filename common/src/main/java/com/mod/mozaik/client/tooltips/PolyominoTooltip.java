package com.mod.mozaik.client.tooltips;

import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import com.mod.mozaik.items.PolyominoItem;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.Tessera;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PolyominoTooltip implements ClientTooltipComponent {
	private final Polyomino polyomino;
	private final int height;
	private final int width;

	public PolyominoTooltip(PolyominoItem.ShapeTooltip contents) {
		this.polyomino = contents.polyomino();

		int maxX = 0, maxY = 0;
		for (Tessera.PlacedTessera tessera : this.polyomino.placedTessera()) {
			if (tessera.x() > maxX) maxX = tessera.x();
			if (tessera.y() > maxY) maxY = tessera.y();
		}

		this.width = (maxX + 1) * Tessera.TESSERA_SIZE;
		this.height = (maxY + 1) * Tessera.TESSERA_SIZE + 2; //Too short otherwise, with this there's 3 pixels of tooltip in each direction
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
