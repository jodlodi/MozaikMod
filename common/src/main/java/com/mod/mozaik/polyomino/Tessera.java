package com.mod.mozaik.polyomino;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record Tessera(TesseraShape shape) {
	public static final int TESSERA_SIZE = 10;

	private Tessera(int ordinal) {
		this(TesseraShape.values()[ordinal]);
	}

	public static final Codec<Tessera> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			Codec.INT.fieldOf("shape").forGetter(tessera -> tessera.shape.ordinal())
	).apply(recordCodecBuilder, Tessera::new));

	public int getU() {
		return this.shape.getU();
	}

	public int getV() {
		return this.shape.getV();
	}

	public record PlacedTessera(Tessera tessera, int x, int y) {
		public static final Codec<PlacedTessera> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
				Tessera.CODEC.fieldOf("tessera").forGetter(PlacedTessera::tessera),
				Codec.INT.fieldOf("x").forGetter(PlacedTessera::x),
				Codec.INT.fieldOf("y").forGetter(PlacedTessera::y)
		).apply(recordCodecBuilder, PlacedTessera::new));
	}

}
