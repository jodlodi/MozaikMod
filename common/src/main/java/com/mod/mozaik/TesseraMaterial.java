package com.mod.mozaik;

import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum TesseraMaterial implements StringRepresentable {
	RED("red"),
	BLUE("blue"),
	MINT("mint"),
	ODD("odd"),
	PINK("pink"),
	YELLOW("yellow"),
	GLASS("glass");

	private static final RandomSource RANDOM = RandomSource.createThreadLocalInstance();

	private final String name;

	TesseraMaterial(String name) {
		this.name = name;
	}

	public int getColor(long seed, int index) {
		return 1;
	}

	@Override
	public String getSerializedName() {
		return this.name;
	}
}
