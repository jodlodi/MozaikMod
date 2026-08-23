package com.mod.mozaik.client.widgets;

import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.PhaseRenderable;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.Tessera;
import com.mod.mozaik.util.FlatDirection;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
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

	@Override
	public void renderBelowItems(GraphicsRenderHelper graphics) {
		renderVoxels(graphics, this.placedPolyomino.polyomino(), this.getX(), this.getY());
	}

	public static void renderVoxels(GraphicsRenderHelper graphics, Polyomino polyomino, int x, int y) {
		renderVoxels(graphics, polyomino, x, y, -1);
	}

	public static void renderVoxels(GraphicsRenderHelper graphics, Polyomino polyomino, int x, int y, int color) {
		graphics.pushPop(() -> {
			graphics.translate(x, y);

			AtomicInteger index = new AtomicInteger(-1);
			polyomino.placedTessera().forEach(tessera -> graphics.pushPop(() -> {
				index.incrementAndGet();
				graphics.translate(
						tessera.x() * Tessera.TESSERA_SIZE,
						tessera.y() * Tessera.TESSERA_SIZE
				);

				graphics.blitTessera(polyomino.material(), tessera.tessera(), polyomino.uuid().getMostSignificantBits(), index.get(), color);
			}));
		});
	}

	public static void fill(GraphicsRenderHelper graphics, Polyomino polyomino, int x, int y, int color) {
		int minSize = 8;
		polyomino.placedTessera().forEach(tessera -> {
			int xMin = x + tessera.x() * Tessera.TESSERA_SIZE + 1;
			int yMin = y + tessera.y() * Tessera.TESSERA_SIZE + 1;
			int xMax = xMin + minSize;
			int yMax = yMin + minSize;

			graphics.fill(xMin, yMin, xMax, yMax, color);

			tessera.tessera().shape().getCheck().forEach(flatDirection -> {
				switch (flatDirection) {
					case UP -> graphics.fill(xMin, yMin - 1, xMax, yMin, color);
					case RIGHT -> graphics.fill(xMax, yMin, xMax + 1, yMax, color);
					case DOWN -> graphics.fill(xMin, yMax, xMax, yMax + 1, color);
					case LEFT -> graphics.fill(xMin - 1, yMin, xMin, yMax, color);
					case UP_RIGHT -> graphics.fill(xMax, yMin - 1, xMax + 1, yMin, color);
					case DOWN_RIGHT -> graphics.fill(xMax, yMax, xMax + 1, yMax + 1, color);
					case DOWN_LEFT -> graphics.fill(xMin - 1, yMax, xMin, yMax + 1, color);
					case UP_LEFT -> graphics.fill(xMin - 1, yMin - 1, xMin, yMin, color);
				}
			});
		});
	}

	public static void selection(GraphicsRenderHelper graphics, Polyomino polyomino, int x, int y) {
		int minSize = 8;
		for (Tessera.PlacedTessera tessera : polyomino.placedTessera()) {
			int xMin = x + tessera.x() * Tessera.TESSERA_SIZE + 1;
			int yMin = y + tessera.y() * Tessera.TESSERA_SIZE + 1;
			int xMax = xMin + minSize;
			int yMax = yMin + minSize;

			List<FlatDirection> missing = new ArrayList<>();

			for (FlatDirection direction : FlatDirection.values()) {
				if (!tessera.tessera().shape().getCheck().contains(direction)) missing.add(direction);
			}

			missing.forEach(flatDirection -> {
				switch (flatDirection) {
					case UP -> graphics.selection(xMin, yMin + 1, xMax, yMin);
					case RIGHT -> graphics.selection(xMax, yMin, xMax - 1, yMax);
					case DOWN -> graphics.selection(xMin, yMax, xMax, yMax - 1);
					case LEFT -> graphics.selection(xMin + 1, yMin, xMin, yMax);
					case UP_RIGHT -> {
						int count = 0;
						for (FlatDirection related : flatDirection.getRelated()) {
							if (!missing.contains(related)) {
								if (related == FlatDirection.UP) {
									graphics.selection(xMax - 1, yMin - 2, xMax, yMin - 1);
									count++;
								} else if (related == FlatDirection.RIGHT) {
									graphics.selection(xMax + 1, yMin, xMax + 2, yMin + 1);
									count++;
								}
							}
						}
						if (count == 2) {
							graphics.selection(xMax - 1, yMin - 1, xMax, yMin);
							graphics.selection(xMax - 1, yMin, xMax + 1, yMin + 1);
						}
					}
					case DOWN_RIGHT -> {
						int count = 0;
						for (FlatDirection related : flatDirection.getRelated()) {
							if (!missing.contains(related)) {
								if (related == FlatDirection.DOWN) {
									graphics.selection(xMax, yMax, xMax - 1, yMax + 2);
									count++;
								} else if (related == FlatDirection.RIGHT) {
									graphics.selection(xMax, yMax, xMax + 2, yMax - 1);
									count++;
								}
							}
						}
						if (count == 2) {
							graphics.selection(xMax, yMax, xMax - 1, yMax - 1);
						}
					}
					case DOWN_LEFT -> {
						for (FlatDirection related : flatDirection.getRelated()) {
							if (!missing.contains(related)) {
								if (related == FlatDirection.DOWN) {
									graphics.selection(xMin + 1, yMax, xMin, yMax + 2);
								} else if (related == FlatDirection.LEFT) {
									graphics.selection(xMin + 1, yMax - 1, xMin, yMax);
								}
							}
						}
					}
					case UP_LEFT -> {
						for (FlatDirection related : flatDirection.getRelated()) {
							if (!missing.contains(related)) {
								if (related == FlatDirection.UP) {
									graphics.selection(xMin + 1, yMin + 1, xMin, yMin);
								} else if (related == FlatDirection.LEFT) {
									graphics.selection(xMin - 2, yMin + 1, xMin, yMin);
								}
							}
						}
					}
				}
			});
		}
	}
}
