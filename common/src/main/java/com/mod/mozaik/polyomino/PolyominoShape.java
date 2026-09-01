package com.mod.mozaik.polyomino;

import com.mod.mozaik.client.screens.PersonalPreferences;
import com.mod.mozaik.reg.ModRegistries;
import com.mod.mozaik.util.FlatDirection;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import org.joml.Vector2i;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PolyominoShape {
	private final List<List<Boolean>> grid = new ArrayList<>();
	private int count = 0;

	public PolyominoShape() {

	}

	public static Optional<Polyomino> tryBuild(ResourceKey<PolyominoShape> key) {
		return tryBuild(key, PersonalPreferences.getPrimaryColor());
	}

	public static Optional<Polyomino> tryBuild(ResourceKey<PolyominoShape> key, ResourceKey<ShardMaterial> material) {
		return tryBuild(key, material, UUID.randomUUID());
	}

	public static Optional<Polyomino> tryBuild(ResourceKey<PolyominoShape> key, ResourceKey<ShardMaterial> material, UUID uuid) {
		try {
			return Optional.of(Objects.requireNonNull(Minecraft.getInstance().getConnection()).registryAccess().registry(ModRegistries.ModKeys.POLYOMINO_SHAPE).orElseThrow().get(key).build(material, uuid));
		} catch (Exception ignored) {

		}
		return Optional.empty();
	}

	public int getCount() {
		return this.count;
	}

	public PolyominoShape addLine(Boolean... line) {
		this.grid.add(List.of(line));
		for (boolean check : line) {
			if (check) this.count++;
		}
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
