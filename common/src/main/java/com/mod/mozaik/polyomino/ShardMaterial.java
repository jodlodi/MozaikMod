package com.mod.mozaik.polyomino;

import com.mod.mozaik.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record ShardMaterial(Type type, int shades) {
	private static final RandomSource RANDOM = RandomSource.createNewThreadLocalInstance();

	public ResourceLocation getGuiSheet(String type, long polySeed, int index) {
		if (this.shades == 1) return Constants.prefix(type + "/" + 0);
		return Constants.prefix(type + "/" + this.randomIndex(polySeed, index));
	}

	public int getBlockId(long polySeed, int index) {
		if (this.shades == 1) return 0;
		return this.randomIndex(polySeed, index);
	}

	private int randomIndex(long polySeed, int index) {
		RANDOM.setSeed(polySeed + index * 250L);
		return (RANDOM.nextInt(Integer.MAX_VALUE) + index) % this.shades;
	}

	public enum Type {
		NORMAL,
		GLOW,
		GLASS
	}
}
