package com.mod.mozaik.client.screens;

import com.mod.mozaik.client.widgets.HeldPolyominoWidget;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.TesseraMaterial;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringRepresentable;
import org.joml.Vector2i;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

@NullMarked
public enum MozaikTool implements StringRepresentable {
	CHISEL,
	CURSOR,
	SWAP,
	PICKER,
	WAND,
	SELECT;

	public static void useOn(MortarScreen screen, boolean shift, List<PolyominoWidget> list, MozaikTool tool) {
		if (list.isEmpty()) return;
		switch (tool) {
			case CURSOR -> {
				screen.carried.clear();
				Vector2i average = new Vector2i();
				for (PolyominoWidget widget : list) {
					average.add(widget.gridX(), widget.gridY());
				}
				average.div(list.size());

				for (PolyominoWidget widget : list) {
					Polyomino.PlacedPolyomino polyomino = widget.getPlacedPolyomino();
					screen.carried.add(new HeldPolyominoWidget(screen, widget.getX(), widget.getY(), new Polyomino.PlacedPolyomino(polyomino.polyomino(), polyomino.x() - average.x, polyomino.y() - average.y)));
					screen.addRenderableWidget(screen.carried.getLast());
					screen.polyominos.remove(widget);
					screen.removeWidget(widget);
				}
				screen.markChanged();
			}
			case CHISEL -> {
				for (PolyominoWidget widget : list) {
					screen.polyominos.remove(widget);
					screen.removeWidget(widget);
				}
				screen.markChanged();
			}
			case SWAP -> {
				for (PolyominoWidget widget : list) {
					PolyominoWidget newWidget = new PolyominoWidget(
							screen,
							widget.getX(),
							widget.getY(),
							new Polyomino.PlacedPolyomino(new Polyomino(widget.getPlacedPolyomino().polyomino().placedTessera(), screen.getPrimaryColor(), Objects.requireNonNull(Minecraft.getInstance().level).getRandom().nextLong()), widget.gridX(), widget.gridY())
					);
					screen.polyominos.remove(widget);
					screen.removeWidget(widget);

					screen.polyominos.add(newWidget);
					screen.addRenderableWidget(newWidget);
				}
				screen.markChanged();
			}
			case PICKER -> {
				for (PolyominoWidget widget : list) {
					if (shift) {
						MortarScreen.setShape(widget.getPlacedPolyomino().polyomino().rebuild(screen.getPrimaryColor(), Objects.requireNonNull(Minecraft.getInstance().level).getRandom().nextLong()));
					} else {
						screen.setPrimaryColor(widget.getPlacedPolyomino().polyomino().material());
					}
				}
				screen.tool = SWAP;
			}
			case WAND -> {
				for (PolyominoWidget widget : list) {
					TesseraMaterial material = widget.getPlacedPolyomino().polyomino().material();
					if (!shift) screen.selected.clear();
					screen.selected.addAll(screen.polyominos.stream().filter(polyominoWidget ->
							!screen.selected.contains(polyominoWidget) && polyominoWidget.getPlacedPolyomino().polyomino().material() == material
					).toList());
				}
			}
			case SELECT -> {
			}
		}
	}

	public String asTranslationString() {
		return "tooltip.mozaik.tool." + this.getSerializedName();
	}

	@Override
	public String getSerializedName() {
		return this.name().toLowerCase(Locale.ROOT);
	}
}
