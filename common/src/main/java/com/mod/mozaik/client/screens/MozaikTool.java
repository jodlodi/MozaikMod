package com.mod.mozaik.client.screens;

import com.mod.mozaik.client.ModKeyMappings;
import com.mod.mozaik.client.widgets.HeldPolyominoWidget;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.TesseraMaterial;
import net.minecraft.client.KeyMapping;
import net.minecraft.util.StringRepresentable;
import org.joml.Vector2i;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Locale;

@NullMarked
public enum MozaikTool implements StringRepresentable {
	CHISEL(ModKeyMappings.CHISEL),
	CURSOR(ModKeyMappings.CURSOR),
	SWAP(ModKeyMappings.SWAP),
	PICKER(ModKeyMappings.PICKER),
	WAND(ModKeyMappings.WAND),
	SELECT(ModKeyMappings.SELECT);

	private final KeyMapping keyMapping;

	MozaikTool(KeyMapping keyMapping) {
		this.keyMapping = keyMapping;
	}

	public KeyMapping getKeyMapping() {
		return this.keyMapping;
	}

	public static void useOn(MortarScreen screen, boolean shift, PolyominoWidget widget, MozaikTool tool) {
		if (!screen.selected.isEmpty() && screen.selected.contains(widget) && tool != SELECT && tool != PICKER && tool != WAND) {
			MozaikTool.useOn(screen, shift, screen.selected, tool);
			screen.selected.clear();
		} else {
			if (tool != WAND && !shift) screen.selected.clear();
			useOn(screen, shift, List.of(widget), tool);
		}
	}

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
							new Polyomino.PlacedPolyomino(new Polyomino(widget.getPlacedPolyomino().polyomino().placedTessera(), PersonalPreferences.getPrimaryColor(), MortarScreen.randomSeed()), widget.gridX(), widget.gridY())
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
						PersonalPreferences.setShape(widget.getPlacedPolyomino().polyomino().rebuild(PersonalPreferences.getPrimaryColor()));
					} else {
						PersonalPreferences.setPrimaryColor(screen, widget.getPlacedPolyomino().polyomino().material());
					}
				}
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
