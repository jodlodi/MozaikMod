package com.mod.mozaik.polyomino;

import com.mod.mozaik.Constants;
import com.mod.mozaik.reg.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Mirror;
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

		public PlacedPolyomino mirror(Mirror mirror) {
			if (mirror == Mirror.NONE) return this;

			List<Tessera.PlacedTessera> placedTessera = new ArrayList<>();

			this.polyomino().placedTessera().forEach(voxel -> {
						Vector2i rotated = mirror == Mirror.FRONT_BACK ? new Vector2i(voxel.x(), -voxel.y()) : new Vector2i(-voxel.x(), voxel.y());
						placedTessera.add(new Tessera.PlacedTessera(new Tessera(voxel.tessera().shape().mirror(mirror)), rotated.x(), rotated.y()));
					}
			);

			Vector2i finalPos = mirror == Mirror.FRONT_BACK ? new Vector2i(this.x(), -this.y() + 15) : new Vector2i(-this.x() + 15, this.y());

			return new Polyomino.PlacedPolyomino(
					new Polyomino(placedTessera, this.polyomino().material(), this.polyomino().uuid()),
					finalPos.x(),
					finalPos.y()
			);
		}
	}

}
