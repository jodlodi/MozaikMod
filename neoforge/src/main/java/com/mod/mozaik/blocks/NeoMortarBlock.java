package com.mod.mozaik.blocks;

import net.minecraft.world.item.DyeColor;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NeoMortarBlock extends MortarBlock {
	public NeoMortarBlock(DyeColor color, Properties properties) {
		super(color, properties);
	}
}
