package com.mod.mozaik;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.Rotation;
import org.joml.Vector2f;
import org.joml.Vector3i;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
public interface Polyomino<T extends Voxel> {
	List<T> allVoxels();

	int gridX();

	int gridY();

	int color();

	Polyomino<T> rotate(Rotation rotation);

	Polyomino<T> mirror();

	long seed();

	default Vector2f getGridCenter() {
		float x = 0;
		float y = 0;

		for (Voxel voxel : this.allVoxels()) {
			x += voxel.relativeX();
			y += voxel.relativeY();
		}

		return new Vector2f(x / this.allVoxels().size() + 0.5F, y / this.allVoxels().size() + 0.5F);
	}

	default PlainPolyomino asPlain() {
		return new PlainPolyomino(this.allVoxels().stream().map(Voxel::asPlain).toList(), this.gridX(), this.gridY(), this.color(), this.seed());
	}

	record PlainPolyomino(List<Voxel.PlainVoxel> allVoxels, int gridX, int gridY, int color, long seed) implements Polyomino<Voxel.PlainVoxel> {
		public static final Codec<PlainPolyomino> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
				Voxel.PlainVoxel.CODEC.listOf().fieldOf("voxel_info").forGetter(PlainPolyomino::allVoxels),
				Codec.INT.fieldOf("grid_x").forGetter(PlainPolyomino::gridX),
				Codec.INT.fieldOf("grid_y").forGetter(PlainPolyomino::gridY),
				Codec.INT.fieldOf("color").forGetter(PlainPolyomino::color),
				Codec.LONG.fieldOf("seed").forGetter(PlainPolyomino::seed)
		).apply(recordCodecBuilder, PlainPolyomino::new));


		public PlainPolyomino(long seed, Voxel.PlainVoxel... info) {
			this(List.of(info), 0, 0, 0, 0);
		}

		@Override
		public PlainPolyomino rotate(Rotation rotation) {
			List<Voxel.PlainVoxel> voxels = new ArrayList<>();

			this.allVoxels().forEach(voxel -> {
				Vector3i vec = new Vector3i(voxel.relativeX(), voxel.relativeY(), 0);
				Vector3i rotated = rotation.rotation().rotate(vec);
				voxels.add(new Voxel.PlainVoxel(rotated.x, rotated.y));
			});

			return new PlainPolyomino(voxels, this.gridX(), this.gridY(), this.color(), this.seed());
		}

		@Override
		public Polyomino<Voxel.PlainVoxel> mirror() {
			List<Voxel.PlainVoxel> voxels = new ArrayList<>();

			this.allVoxels().forEach(voxel -> {
				voxels.add(new Voxel.PlainVoxel(voxel.relativeX() * -1, voxel.relativeY()));
			});

			return new PlainPolyomino(voxels, this.gridX(), this.gridY(), this.color(), this.seed());
		}
	}

	enum PolyominoShape {
		SQUARE(new PlainPolyomino(
				Minecraft.getInstance().level.getRandom().nextLong(),
				new Voxel.PlainVoxel(0, 0),
				new Voxel.PlainVoxel(1, 0),
				new Voxel.PlainVoxel(0, 1),
				new Voxel.PlainVoxel(1, 1)
		)),
		PLUS(new PlainPolyomino(
				Minecraft.getInstance().level.getRandom().nextLong(),
				new Voxel.PlainVoxel(1, 0),
				new Voxel.PlainVoxel(0, 1),
				new Voxel.PlainVoxel(1, 1),
				new Voxel.PlainVoxel(2, 1),
				new Voxel.PlainVoxel(1, 2)
		)),
		BLOB(new PlainPolyomino(
				Minecraft.getInstance().level.getRandom().nextLong(),
				new Voxel.PlainVoxel(1, 0),
				new Voxel.PlainVoxel(2, 0),
				new Voxel.PlainVoxel(0, 1),
				new Voxel.PlainVoxel(1, 1),
				new Voxel.PlainVoxel(2, 1),
				new Voxel.PlainVoxel(0, 2),
				new Voxel.PlainVoxel(1, 2)
		)),
		THUMBS_UP(new PlainPolyomino(
				Minecraft.getInstance().level.getRandom().nextLong(),
				new Voxel.PlainVoxel(1, 0),
				new Voxel.PlainVoxel(0, 1),
				new Voxel.PlainVoxel(1, 1),
				new Voxel.PlainVoxel(0, 2),
				new Voxel.PlainVoxel(1, 2)
		)),
		L(new PlainPolyomino(
				Minecraft.getInstance().level.getRandom().nextLong(),
				new Voxel.PlainVoxel(0, 0),
				new Voxel.PlainVoxel(1, 0),
				new Voxel.PlainVoxel(0, 1)
		)),
		MIDDLE_FINGER(new PlainPolyomino(
				Minecraft.getInstance().level.getRandom().nextLong(),
				new Voxel.PlainVoxel(1, 0),
				new Voxel.PlainVoxel(0, 1),
				new Voxel.PlainVoxel(1, 1),
				new Voxel.PlainVoxel(2, 1),
				new Voxel.PlainVoxel(0, 2),
				new Voxel.PlainVoxel(1, 2),
				new Voxel.PlainVoxel(2, 2)
		)),
		I(new PlainPolyomino(
				Minecraft.getInstance().level.getRandom().nextLong(),
				new Voxel.PlainVoxel(0, 0),
				new Voxel.PlainVoxel(0, 1),
				new Voxel.PlainVoxel(0, 2)
		)),
		T(new PlainPolyomino(
				Minecraft.getInstance().level.getRandom().nextLong(),
				new Voxel.PlainVoxel(0, 0),
				new Voxel.PlainVoxel(0, 1),
				new Voxel.PlainVoxel(1, 1),
				new Voxel.PlainVoxel(0, 2)
		)),
		Z(new PlainPolyomino(
				Minecraft.getInstance().level.getRandom().nextLong(),
				new Voxel.PlainVoxel(0, 0),
				new Voxel.PlainVoxel(0, 1),
				new Voxel.PlainVoxel(1, 1),
				new Voxel.PlainVoxel(1, 2)
		)),
		C(new PlainPolyomino(
				Minecraft.getInstance().level.getRandom().nextLong(),
				new Voxel.PlainVoxel(0, 0),
				new Voxel.PlainVoxel(1, 0),
				new Voxel.PlainVoxel(0, 1),
				new Voxel.PlainVoxel(0, 2),
				new Voxel.PlainVoxel(1, 2)
		));

		public final PlainPolyomino template;

		PolyominoShape(PlainPolyomino template) {
			this.template = template;
		}
	}
}
