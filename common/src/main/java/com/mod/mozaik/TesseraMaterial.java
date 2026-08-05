package com.mod.mozaik;

import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@NullMarked
public enum TesseraMaterial implements StringRepresentable {
	STONE(1L, 1),

	RED(1L, 1),
	BLUE(1L, 1),
	MINT(1L, 1),
	ODD(1L, 1),
	PINK(1L, 1),
	YELLOW(1L, 1),
	GLASS(1L, 1);

	private static final RandomSource RANDOM = RandomSource.createThreadLocalInstance();

	private final List<Identifier> spriteSheets = new ArrayList<>();
	private final long seed;

	TesseraMaterial(long seed, int textureCount) {
		this.seed = seed;
		for (int i = 0; i < textureCount; i++) {
			this.spriteSheets.add(pathToTessera(this.getSerializedName(), i + 1));
		}
	}

	private static Identifier pathToTessera(String name, int number) {
		return Constants.prefix("textures/block/mural/" + name + "/gui_" + number +  ".png");
	}

	public Identifier getColor(long polySeed, int index) {
		if (this.spriteSheets.size() == 1) return this.spriteSheets.getFirst();
		RANDOM.setSeed(this.seed + polySeed + index);
		return this.spriteSheets.get(RANDOM.nextInt(this.spriteSheets.size()));
	}

	@Override
	public String getSerializedName() {
		return this.name().toLowerCase(Locale.ROOT);
	}
}
