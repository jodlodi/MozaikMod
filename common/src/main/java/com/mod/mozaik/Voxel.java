package com.mod.mozaik;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public interface Voxel {
	int relativeX();

	int relativeY();

	default PlainVoxel asPlain() {
		return new PlainVoxel(this.relativeX(), this.relativeY());
	}

	record PlainVoxel(int relativeX, int relativeY) implements Voxel {
		public static final Codec<PlainVoxel> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
				Codec.INT.fieldOf("relative_x").forGetter(PlainVoxel::relativeX),
				Codec.INT.fieldOf("relative_y").forGetter(PlainVoxel::relativeY)
		).apply(recordCodecBuilder, PlainVoxel::new));
	}
}
