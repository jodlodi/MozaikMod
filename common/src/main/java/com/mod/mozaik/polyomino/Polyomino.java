package com.mod.mozaik.polyomino;

import com.mod.mozaik.Constants;
import com.mod.mozaik.reg.ModRegistries;
import com.mod.mozaik.reg.ModShardMaterials;
import com.mod.mozaik.reg.ResourceSupplier;
import com.mod.mozaik.util.FlatDirection;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceKey;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NullMarked
public record Polyomino(List<Tessera.PlacedTessera> placedTessera, ResourceKey<ShardMaterial> material, UUID uuid) {
	public static Polyomino EMPTY = new Polyomino(List.of(), ResourceKey.create(ModRegistries.ModKeys.SHARD_MATERIAL, Constants.prefix("")), UUID.randomUUID());

	public Polyomino rebuild(ResourceKey<ShardMaterial> material) {
		return new Polyomino(this.placedTessera(),material, UUID.randomUUID());
	}

	public Polyomino copy() {
		return new Polyomino(this.placedTessera, this.material, this.uuid);
	}

	public static final Codec<Polyomino> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			Tessera.PlacedTessera.CODEC.listOf().fieldOf("placedPolyomino").forGetter(Polyomino::placedTessera),
			ResourceKey.codec(ModRegistries.ModKeys.SHARD_MATERIAL).fieldOf("material").forGetter(Polyomino::material),
			UUIDUtil.LENIENT_CODEC.fieldOf("uuid").forGetter(Polyomino::uuid)
	).apply(recordCodecBuilder, Polyomino::new));

	public Vector2f getGridCenter() {
		float x = 0;
		float y = 0;

		for (Tessera.PlacedTessera voxel : this.placedTessera) {
			x += voxel.x();
			y += voxel.y();
		}

		return new Vector2f(x / this.placedTessera.size() + 0.5F, y / this.placedTessera.size() + 0.5F);
	}

	public record PlacedPolyomino(Polyomino polyomino, int x, int y) {
		public static final Codec<PlacedPolyomino> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
				Polyomino.CODEC.fieldOf("polyomino").forGetter(PlacedPolyomino::polyomino),
				Codec.INT.fieldOf("x").forGetter(PlacedPolyomino::x),
				Codec.INT.fieldOf("y").forGetter(PlacedPolyomino::y)
		).apply(recordCodecBuilder, PlacedPolyomino::new));
	}

	public static class Builder {
		private final List<List<Boolean>> grid = new ArrayList<>();

		public Builder() {

		}

		public Builder addLine(Boolean... line) {
			this.grid.add(List.of(line));
			return this;
		}

		public Polyomino build(ResourceKey<ShardMaterial> material, UUID uuid) {
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

			List<Tessera.PlacedTessera> placedTessera = tessera.stream().map(vector2i -> {
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

				return new Tessera.PlacedTessera(new Tessera(TesseraShape.get(connections)), vector2i.x(), vector2i.y());
			}).toList();

			return new Polyomino(placedTessera, material, uuid);
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
