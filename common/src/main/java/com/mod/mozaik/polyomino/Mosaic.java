package com.mod.mozaik.polyomino;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record Mosaic(List<Polyomino.PlacedPolyomino> polyomino) {
	public static final Codec<Mosaic> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			Polyomino.PlacedPolyomino.CODEC.listOf().fieldOf("polyomino").forGetter(Mosaic::polyomino)
	).apply(recordCodecBuilder, Mosaic::new));
}
