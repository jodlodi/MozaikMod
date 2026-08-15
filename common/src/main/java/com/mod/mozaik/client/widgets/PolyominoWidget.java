package com.mod.mozaik.client.widgets;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.PhaseRenderable;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.Tessera;
import com.mod.mozaik.polyomino.TesseraMaterial;
import com.mod.mozaik.util.FlatDirection;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@NullMarked
public class PolyominoWidget extends UnclickableWidget implements PhaseRenderable {
	public final MortarScreen screen;
	private final Polyomino.PlacedPolyomino placedPolyomino;

	public PolyominoWidget(MortarScreen screen, int x, int y, Polyomino.PlacedPolyomino placedPolyomino) {
		super(x, y, 0, 0);
		this.screen = screen;
		this.placedPolyomino = placedPolyomino;
	}

	public Polyomino.PlacedPolyomino getPlacedPolyomino() {
		return this.placedPolyomino;
	}

	public int gridX() {
		return this.placedPolyomino.x();
	}

	public int gridY() {
		return this.placedPolyomino.y();
	}

	public List<Tessera.PlacedTessera> placedTessera() {
		return this.placedPolyomino.polyomino().placedTessera();
	}

	@Override
	public void renderBelowItems(GraphicsRenderHelper graphics) {
		renderVoxels(graphics, this.placedPolyomino.polyomino(), this.placedPolyomino.polyomino().material(), this.getX(), this.getY());
	}

	public static void renderVoxels(GraphicsRenderHelper graphics, Polyomino polyomino, TesseraMaterial material, float x, float y) {
		graphics.pushPop(() -> {
			graphics.translate(x, y);

			AtomicInteger index = new AtomicInteger(-1);
			polyomino.placedTessera().forEach(tessera -> graphics.pushPop(() -> {
				index.incrementAndGet();
				graphics.translate(
						tessera.x() * Tessera.TESSERA_SIZE,
						tessera.y() * Tessera.TESSERA_SIZE
				);

				graphics.blitTessera(material, tessera.tessera(), polyomino.seed(), index.get());
			}));
		});
	}

	public static Identifier byaDirection(@Nullable FlatDirection direction, TesseraMaterial material) {
		if (direction == null)
			return Constants.prefix("textures/block/mural/" + material.getSerializedName() + "/polyomino.png");
		return switch (direction) {
			case UP -> Constants.prefix("textures/block/mural/" + material.getSerializedName() + "/bridge_up.png");
			case UP_RIGHT ->
					Constants.prefix("textures/block/mural/" + material.getSerializedName() + "/corner_up_right.png");
			case RIGHT ->
					Constants.prefix("textures/block/mural/" + material.getSerializedName() + "/bridge_right.png");
			case DOWN_RIGHT ->
					Constants.prefix("textures/block/mural/" + material.getSerializedName() + "/corner_down_right.png");
			case DOWN -> Constants.prefix("textures/block/mural/" + material.getSerializedName() + "/bridge_down.png");
			case DOWN_LEFT ->
					Constants.prefix("textures/block/mural/" + material.getSerializedName() + "/corner_down_left.png");
			case LEFT -> Constants.prefix("textures/block/mural/" + material.getSerializedName() + "/bridge_left.png");
			case UP_LEFT ->
					Constants.prefix("textures/block/mural/" + material.getSerializedName() + "/corner_up_left.png");
		};
	}
}
