package com.mod.mozaik.polyomino;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record Mozaik(List<Polyomino.PlacedPolyomino> placedPolyomino) {
	public static final Codec<Mozaik> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			Polyomino.PlacedPolyomino.CODEC.listOf().fieldOf("placedPolyomino").forGetter(Mozaik::placedPolyomino)
	).apply(recordCodecBuilder, Mozaik::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, Mozaik> STREAM_CODEC = StreamCodec.ofMember(
			(stack, byteBuf) -> byteBuf.writeJsonWithCodec(CODEC, stack),
			byteBuf -> byteBuf.readJsonWithCodec(CODEC)
	);
}
