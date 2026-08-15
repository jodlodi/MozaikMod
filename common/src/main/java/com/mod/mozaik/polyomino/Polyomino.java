package com.mod.mozaik.polyomino;

import com.mod.mozaik.util.FlatDirection;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public record Polyomino(List<PlacedTessera> placedTessera, TesseraMaterial material, long seed) {

	private Polyomino(List<PlacedTessera> placedTessera, int ordinal, long seed) {
		this(placedTessera, TesseraMaterial.values()[ordinal], seed);
	}

	public static final Codec<Polyomino> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			PlacedTessera.CODEC.listOf().fieldOf("placedTessera").forGetter(Polyomino::placedTessera),
			Codec.INT.fieldOf("material").forGetter(material -> material.material.ordinal()),
			Codec.LONG.fieldOf("seed").forGetter(Polyomino::seed)
	).apply(recordCodecBuilder, Polyomino::new));

	public record PlacedTessera(Tessera tessera, int x, int y) {
		public static final Codec<PlacedTessera> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
				Tessera.CODEC.fieldOf("polyomino").forGetter(PlacedTessera::tessera),
				Codec.INT.fieldOf("x").forGetter(PlacedTessera::x),
				Codec.INT.fieldOf("y").forGetter(PlacedTessera::y)
		).apply(recordCodecBuilder, PlacedTessera::new));
	}

	public Vector2f getGridCenter() {
		float x = 0;
		float y = 0;

		for (PlacedTessera voxel : this.placedTessera) {
			x += voxel.x();
			y += voxel.y();
		}

		return new Vector2f(x / this.placedTessera.size() + 0.5F, y / this.placedTessera.size() + 0.5F);
	}

	public static class Builder {
		private final List<List<Boolean>> grid = new ArrayList<>();

		public Builder() {

		}

		public Builder addLine(Boolean... line) {
			this.grid.add(List.of(line));
			return this;
		}

		public Polyomino build(TesseraMaterial material, long seed) {
			int x = 0;
			int y = 0;

			List<Vector2i> tessera = new ArrayList<>();

			for (List<Boolean> horizontal : this.grid) {
				for (Boolean here : horizontal) {
					if (here) tessera.add(new Vector2i(x, y));
					x++;
				}
				x = 0;
				y++;
			}

			List<PlacedTessera> placedTessera = tessera.stream().map(vector2i -> {
				List<FlatDirection> connections = new ArrayList<>();

				for (FlatDirection direction : FlatDirection.cardinalClockwise()) {
					if (checkConnection(tessera, vector2i, direction)) {
						connections.add(direction);
					}
				}

				for (FlatDirection direction : FlatDirection.subClockwise()) {
					if (checkConnection(tessera, vector2i, direction)) {
						boolean shouldExist = true;
						for (FlatDirection related : direction.getRelated()) {
							if (!checkConnection(tessera, vector2i, related)) shouldExist = false;
						}
						if (!shouldExist) continue;

						connections.add(direction);
					}
				}

				return new PlacedTessera(new Tessera(Tessera.Shape.get(connections)), vector2i.x(), vector2i.y());
			}).toList();

			return new Polyomino(placedTessera, material, seed);
		}

		public static boolean checkConnection(List<Vector2i> tessera, Vector2i voxel, FlatDirection direction) {
			return tessera.stream().anyMatch(relative -> {
				int diffX = relative.x() - voxel.x();
				int diffY = relative.y() - voxel.y();
				return diffX == direction.getRelativeX() && diffY == direction.getRelativeY();
			});
		}
	}
}
