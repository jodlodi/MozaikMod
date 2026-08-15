package com.mod.mozaik.polyomino;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public interface IPolyominoHolder {
	List<PlacedPolyomino> getPolyomino();

	record PlacedPolyomino(Polyomino polyomino, int x, int y) {
		public static final Codec<PlacedPolyomino> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
				Polyomino.CODEC.fieldOf("polyomino").forGetter(PlacedPolyomino::polyomino),
				Codec.INT.fieldOf("x").forGetter(PlacedPolyomino::x),
				Codec.INT.fieldOf("y").forGetter(PlacedPolyomino::y)
		).apply(recordCodecBuilder, PlacedPolyomino::new));
	}

	enum PolyominoShapes {
		SQUARE(new Polyomino.Builder()
				.addLine(true, true)
				.addLine(true, true)
		),
		PLUS(new Polyomino.Builder()
				.addLine(false, true, false)
				.addLine(true, true, true)
				.addLine(false, true, false)
		),
		BLOB(new Polyomino.Builder()
				.addLine(true, true, false)
				.addLine(true, true, true)
				.addLine(false, true, true)
		),
		R_THUMBS_UP(new Polyomino.Builder()
				.addLine(true, false)
				.addLine(true, true)
				.addLine(true, true)
		),
		L_THUMBS_UP(new Polyomino.Builder()
				.addLine(false, true)
				.addLine(true, true)
				.addLine(true, true)
		),
		L(new Polyomino.Builder()
				.addLine(true, false)
				.addLine(true, true)
		),
		MIDDLE_FINGER(new Polyomino.Builder()
				.addLine(false, true, false)
				.addLine(true, true, true)
				.addLine(true, true, true)
		),
		I(new Polyomino.Builder()
				.addLine(true)
				.addLine(true)
				.addLine(true)
		),
		T(new Polyomino.Builder()
				.addLine(true, true, true)
				.addLine(false, true, false)
				.addLine(false, true, false)
		),
		Z(new Polyomino.Builder()
				.addLine(true, true, false)
				.addLine(false, true, true)
		),
		S(new Polyomino.Builder()
				.addLine(false, true, true)
				.addLine(true, true, false)
		),
		C(new Polyomino.Builder()
				.addLine(true, true)
				.addLine(true, false)
				.addLine(true, true)
		);

		public final Polyomino.Builder template;

		PolyominoShapes(Polyomino.Builder template) {
			this.template = template;
		}
	}
}
